package movierater.fxui;

import java.io.IOException;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FilmApp extends Application {

  /**
   * Starts the application.
   *
   * @param stage stage object
   */
  @Override
  public void start(Stage stage) throws IOException {
    Parent parent =
        FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginPage.fxml")));
    stage.setScene(new Scene(parent));
    stage.setTitle("MovieRater - Login");
    stage.show();
    stage.centerOnScreen();
  }

  public static void main(String[] args) {
    launch();
  }
}
