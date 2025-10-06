# Rapport Tâche 2
- Jonathan Beaulieu
- Joaquim Sandler-Soussy

## Classes choisies
- [PintList.java](../../../../../main/java/com/graphhopper/util/PointList.java)
### [`reverse()`](./PointListTest.java)
#### Intention:

#### Choix des données

#### Oracle?

## Tests effectuées


## Analyse de mutation avec PiTest
### Rapport avant changements
![Rapport avant tests](./resources/PitTest_Avant.png)
### Rapport après ajout des tests
![Rapport avant tests](./resources/PitTest_Apres.png)
### Mutants trouvés


## Java-Faker
- Le test "testAddPointListJavaFaker" se veut simple et évite au maximum les dépendances 
externes pour maximiser l'encapsulation.
- Nous créons d'abord une `PointList` source avec une capacité initiale de 3 points 3D et 3 tableaux
"lats", "lons" et "eles" qui nous servent de reference pour les assertions.
- On génère ensuite 3 points avec precision de 6 décimales pour la latitude et la longitude et 
2 décimales pour l'élévation, dans les intervalles indiquées dans chaque test respectivement, et on 
ajoute les lon, lat et ele dans leur tableau.  
- On teste ensuite la fonction add(PointList) en ajoutant la liste source dans une nouvelle liste 
cible. 
- On utilise pour finir assertEquals pour comparer les lat, lon et ele de la liste cible avec les 
tableaux contenant les lat, lon et ele de base.
- Les tableaux lats, lons, eles garantissent que les assertions se basent sur les valeurs d’entrée 
originales, pas l’implémentation de PointList.

## 


# Template
(à retirer avant la remise)
```
### [Nom du test](URL)
#### Intention:

#### Choix des données

#### Oracle
```
