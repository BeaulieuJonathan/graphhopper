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
      key: ${{ runner.os }}-pitest-${{ matrix.java-version }}
```

Cette étape permet de récupérer les rapports de Pitest d'une exécution précédente. Cela nous permettra de comparer avec le score de mutation de cette version ou de ne pas les comparer une aucune version antérieure n'est trouvée.

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

Cette étape calcule le nombre de mutants éliminés et le nombre total pour les étapes futures. Elle est éxecutée seulement si un cache a été trouvé à l'étape précédente. Les valeurs calculés sont mises en output.

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

Cette étape utilise l'action [test-summary](https://github.com/test-summary/action) pour générer un rapport de tests qui va être affiché dans le sommaire de l'action. L'intention est, à la base, d'avoir une méthode pour trouver si au moins un test a échoué afin d'assurer un [travail de qualité](https://www.youtube.com/watch?v=dQw4w9WgXcQ).

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
        newMutantsTotal=$(grep -ce . web-api/target/pit-reports/mutations.csv)

        if [ $(($newMutantsTotal * ${{ steps.oldScore.outputs.killedMutants }} )) -gt $(( $newKilledScore * ${{ steps.oldScore.outputs.totalMutants }})) ]; then
            echo "Job failed because mutation coveraged decreased." >> $GITHUB_STEP_SUMMARY
            echo "Previous score: ${{ steps.oldScore.outputs.killedMutants }}/${{ steps.oldScore.outputs.totalMutants }}"
            echo "New score: $newKilledScore/$newMutantsTotal" >> $GITHUB_STEP_SUMMARY
            exit 1
        fi
```

Cette étape compare le score de mutation du commit avec celui calculé précédement (si un cache pour la rapport pit a été trouvé). Pour comparer un score qui n'est pas une fraction, la comparaison est effectuée en balançant le dénominateur des deux côtés. Ainsi, on peut vérifier le prédicat suivant pour savoir si le score de mutation a diminué.

$$
\frac{killedMutants}{totalMutants}
>
\frac{newKilledScore}{newKilledTotal}
\\ {} \\
\rightarrow
 \frac{newKilledTotal \times killedMutants}{newKilledTotal \times totalMutants}
 >
\frac{newKilledScore \times totalMutants}{newKilledTotal \times totalMutants}
$$

Si c'est le cas on termine l'exécution avec `exit 1` et puisque dénominateur est le même des deux côtés on a qu'à comparer le numérateur.

## Tests avec les mocks

Ajout de 2 tests mockito pour la classe GHResponse et 1 pour la classe PointList.

```java
@Test
public void mockitoTest_HasErrorsResponsePath() {
    GHResponse response = new GHResponse();
    ResponsePath path = mock(ResponsePath.class);

    when(path.hasErrors()).thenReturn(true);
    response.add(path);

    assertTrue(response.hasErrors());
}

@Test
void mockitoTest_GetDebugInfo() {
    GHResponse response = new GHResponse();

    response.addDebugInfo("Info");

    ResponsePath path = mock(ResponsePath.class);
    when(path.getDebugInfo()).thenReturn("pathInfo");

    response.add(path);

    String debug = response.getDebugInfo();

    assertEquals("Info; pathInfo", debug);
}

@Test
public void testPointListWithMockedPointAccess() {
    PointAccess point = mock(PointAccess.class);

    when(point.getLat(0)).thenReturn(42.0);
    when(point.getLon(0)).thenReturn(67.0);

    PointList testedList = new PointList(3,false);

    testedList.add(point,0);

    assertEquals(42.0,testedList.getLat(0));
    assertEquals(67.0,testedList.getLon(0));

}
```

### Justification du choix des classes testées:
Nous avons choisi de tester les classes GHResponse et PointList car elles semblaient spécialement propices à
des tests mockitos au vu de leurs dépendances d'autres classe. De plus, elles sont très interessantes.

### Choix des classes simulées et définition des mocks
Pour GHResponse, il n'y a pas vraiment de choix à faire, il faut simuler ResponsePath puisque toutes 
les données sur l'itininéraire sont contenues dans cette classe. Étant donné que GHResponse a des fonctions
qui prenne des attributs de ResponsePath en arguments, ResponsePath était la classe à simuler.

Pour PointList, le plus important est de tester les getters getLat() et getLon() de GHPoint. Nous 
aurions aussi pu tester getEle() de GHPoint3D. Nous imposons l'altitude et la longitude de notre mock
de GHPoint. Nous attendons les même valeurs lorsqu'on les demande au getters testés.

### Choix des valeurs simulées
Pour la classe GHResponse, deux des fonctions qui étaient le plus pertinentes à tester avec Mockito 
sont d'après nous getDebugInfo() et  hasErrors. 

Pour hasErrors(), il faut simuler que l'objet path de ResponsePath a bien des erreurs.
Ensuite, nouis vérifions que la fonciton hasErrors de GHResponse rend bien le même verdict.

Pour getDebugInfo(), nous vérifions que la concaténation entre les infos se passe correctement.
Nous simulons donc un ajout de l'info "pathInfo" de l'objet ReponsePath.
Nous vérifions que le string renvoyé pas getDebugInfo est bel est bien la concaténation des infos 
avant et après l'ajout.

Les valeurs de l'atitude et longitude ne sont pas du tout choisies au hasard.
Bien au contraire, ce sont les coordonnées de la ville natale de Rick Astley:
Newton-Le-Willos!
