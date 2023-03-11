# Integrationtests

Hensikten med integrasjonstestingen er for å teste at de individuelle modulene fungerer i samspill med hverandre for at applikasjonen skal fungere i en helhet. Dette innebærer testing av core, fxui og restserver i lag. I hovedsak sjekker testene at fxui-et får til å sende og hente data til/fra REST API-serveren som dermed kan lagre og tilgjengeliggjøre dataen. 

Jacoco er ikke konfigurert for integrasjonenstestene så vi har derfor ingen dekningsgrad å vise for de nevnte testene. Testene går gjennom et bruksscenario av appen der en ny bruker logger inn, legger til filmliste, legger til film, sletter filmlista og sletter brukeren til slutt. 
