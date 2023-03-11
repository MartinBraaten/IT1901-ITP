# CORE
Core-pakken inneholder domenelogikken, som består av klassene:

- Film.java, lager objekter av typen film med tittel, år, rating, kommentar og sett/ikke sett.

- FilmList.java, inneholder en liste over filmobjekter.

- User.java har et brukernavn og en hashmap som kan inneholde flere filmlister.


#### Json:
Core modulen inneholder også JSON-pakken som inneholder logikken for fillagring. 
Vi har kun en klasse: 

- UserPersistence.java, denne bruker Jackson-tillegget for å lese/skrive til og fra JSON-format. 

### Klassediagram core
Under har vi klassediagram for Core-modulen:

![Core](../../docs/UML/release3/ClassDiagrams/Core_ClassDiagram.png)
