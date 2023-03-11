
// Hvis denne filen kommenteres ut vil de vanlige testene kjøre som de skal, mens 
// hvis den står som den er nå vil mvn test fungere som den skal. 
// Får ikke til å kjøre begge delene samtidig. 
module martinis.calc {

    requires javafx.controls;
    requires javafx.fxml;

    opens martinis.calc to javafx.graphics, javafx.fxml;
}
