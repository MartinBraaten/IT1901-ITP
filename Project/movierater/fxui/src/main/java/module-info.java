module movierater.fxui {
  requires transitive com.fasterxml.jackson.databind;
  requires javafx.base;
  requires javafx.fxml;
  requires javafx.graphics;
  requires javafx.controls;
  requires transitive movierater.core;
  requires java.net.http;

  opens movierater.fxui to
      javafx.graphics,
      javafx.fxml;
}
