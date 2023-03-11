[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2022/gr2234/gr2234)

## Gruppe 34 (gr2234)

## Beskrivelse
Kodingsprosjektet ligger i mappen movierater og er bygget med maven. 

For informasjon om kodeprosjektet, les her: [movierater](movierater/README.md).

Dokumentasjon for første innleveringen kan finnes her: [release1](docs/release1.md).

Dokumentasjon for andre innlevering kan finnes her: [release2](docs/release2.md).

Dokumentasjon for tredje innlevering kan finnes her: [release3](docs/release3.md).


## Kjøring av prosjektet
Naviger i terminalen til movierater-mappen for å kunne kjøre ***mvn clean install***. 
```
cd movierater
mvn clean install
```

Deretter kan man kjøre appen med:
```
mvn javafx:run -f fxui
```
Appen kan enten benytte "Direct access" eller "Remote access". 
"Direct access" tar ikke i bruk restserver, mens "Remote access" krever at restserver kjører. 
Restserveren kan kjøres i en ny terminal med kommandoen: 
```
mvn spring-boot:run -f restserver
```
## Testing av koden

For å kun teste koden, kan følgende kommando kjøres fra movierater-mappe (restserveren må ikke kjøres samtidig):

```
mvn clean verify
```
JaCoCo vil deretter generere en rapport (index.html) som finnes i hver modul sin undermappe /target/site/jacoco/index.html. 

Det vil også genereres en rapport for modulene som hver modul er avhengig av i /target/site/jacoco-aggregate/index.html.

Integrationtests-modulen inneholder kun en jacoco-aggregate rapport som dekker hele prosjektet. 

**Merknad**: Det kan være behov for å kjøre testene på nytt dersom JaCoCo ikke får til å generere testrapport for alle modulene. Gjelder særlig hvis appen kjøres fra Gitpod for første gang. 

Dersom man ønsker at fxui-testene skal kjøre i bakgrunnen, kan headless-profilen benyttes:
```
mvn clean verify -P headless
```

Denne vil benytte JavaFX 12 og gi warning, men alle testene skal likevel kunne kjøre uten problemer. 
## Lage et shippable produkt

For å bygge prosjektet kan man skrive følgende kommandoer i terminalen fra movierater-mappen: 

```
mvn javafx:jlink -f fxui 
```
Til slutt bruker du kommandoen:
```
mvn jpackage:jpackage -f fxui
```
Du kan da finne nedlastingen til appen under fxui/target/dist.



