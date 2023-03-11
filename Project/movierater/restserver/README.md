# Restserver
Reserveren til Movierater er bygd opp med Spring Boot og består av følgende klasser:

- RestserverApplication: har som funksjon å starte opp serverappen.
- RestserverController: tar i mot HTTP-forespørsler og håndterer disse. Vi har metoder for GET, POST og DELETE. 
- RestserverService: tilbyr lagring og henting av user-objekter fra json-fil og benyttes av RestserverControlleren.   

Testmappen inneholder tester for restserveren. Disse benytter seg av MockMvc for å mocke HTTP-forespørsler og sjekker at vi får riktig respons av serveren. 

### Klassediagram restserver
Under ser vi klassediagrammet for restserver-pakken. 

![Klassediagram](../../docs/UML/release3/ClassDiagrams/Restserver_ClassDiagram.png)


