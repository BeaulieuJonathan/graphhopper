# Rapport Tâche 3

- Jonathan Beaulieu
- Joaquim Sandler-Soussy

## Modifications du Github Actions

Cette section décrit les étapes ajoutés dans le workflow [`build.yml`](./../../../../../../../.github/workflows/build.yml) et l'intention de chacune.

### Cache mutations

```yml
- name: Cache mutations
  id: mutations
  uses: actions/cache@v4
  with:
      path: web-api/target/pit-reports
      key: ${{ runner.os }}-pitest
```

Cette étape permet de récuppérer les rapports de Pitest d'une exécition précédente. Cela nous permettra de comparer avec le score de mutation de cette version ou de ne pas comparer s'il n'y a pas de version antérieur trouvée.

### Calculate Mutation Score

```yml
- name: Calculate Mutation Score
  if: steps.mutations.outputs.cache-hit == 'true'
  id: oldScore
  run: |
      killedMutants=$(grep -e "KILLED" -c web-api/target/pit-reports/mutations.csv)
      totalMutants=$(grep -ce . web-api/target/pit-reports/mutations.csv)
      echo "killedMutants=$killedMutants" >> $GITHUB_OUTPUT
      echo "totalMutants=$totalMutants" >> $GITHUB_OUTPUT
```

Cette étape calcule le nombre de mutants éliminés et le nombre total pour les étapes futurs. Elle est éxecuté seulement si un cache a été trouvé à l'étape précédente. Les valeurs calculés sont mises en output.

### Mutation Coverage in web-api

```yml
- name: Mutation Coverage ${{ matrix.java-version }} in web-api
  run: mvn -f ./web-api test-compile org.pitest:pitest-maven:mutationCoverage
```

Puisque Pitest ne peut pas être utilisé dans un projet multi-modules, nous avons décidé d'exécuter les tests de mutation seulement dans le module `web-api` où nous avons ajouté des tests.

### Make report

```yml
- name: Make report
  id: report
  uses: test-summary/action@v2
  with:
      paths: "**/target/surefire-reports/TEST-*.xml"
  if: always()
```

Cette étape utilise une action pour générer un rapport de tests qui va être affiché dans le sommaire de l'action. L'intention est, à la base, d'avoir une méthode pour trouver si au moins un test a échoué afin [d'assurer la qualité du travail](https://www.youtube.com/watch?v=dQw4w9WgXcQ).

### Special effect if failed tests

```yml
- name: Special Effect If failed tests
  if: always()
  run: |
      failed=${{ steps.report.outputs.failed }}
      if [ $failed -gt 0 ]; then
          echo "It seems a test has failed! [This link](https://www.youtube.com/watch?v=dQw4w9WgXcQ) provides additionnal information." >> $GITHUB_STEP_SUMMARY
      fi
```

Cette action sert à ajouter un lien bien connu comme élément humoristique dans le CI lorsqu'un test échoue.

### Compare Mutations

```yml
- name: Compare mutations
  if: steps.mutations.outputs.cache-hit == 'true'
  run: |
      newKilledScore=$(grep -c -e "KILLED" web-api/target/pit-reports/mutations.csv)
      newKilledTotal=$(grep -ce . web-api/target/pit-reports/mutations.csv)
        
      if [ $(($newKilledTotal * ${{ steps.oldScore.outputs.killedMutants }} )) -gt $(( $newKilledScore * ${{ steps.oldScore.outputs.totalMutants }})) ]; then
          exit 1
      fi
```

Cette étape compare le score de mutation du commit avec celui calculé précédement. Pour éviter les fractions, la comparaison est effectués en balançant le dénominateur des deux côtés. Ainsi, on peut vérifier le prédicat suivant pour savoir si le score de mutation a diminué.
$$ \frac{newKilledScore}{newKilledTotal} < \frac{killedMutants}{totalMutants}
\\ {} \\ \rightarrow \frac{newKilledScore \times totalMutants}{newKilledTotal \times totalMutants} < \frac{killedMutants \times newKilledTotal}{totalMutants \times newKilledTotal} $$

Si c'est le cas on termine l'exécution avec `exit 1`.

## Tests avec les mocks
