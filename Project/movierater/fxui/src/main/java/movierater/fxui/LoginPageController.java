package movierater.fxui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginPageController {

  @FXML private TextField userNameInput;
  @FXML private Button loginButton;
  @FXML private MainPageController mainPageController = new MainPageController();
  @FXML private ComboBox<String> accessDropDown = new ComboBox<>();

  private DataAccess dataAccess;

  /** Initializes the login page. */
  @FXML
  void initialize() throws URISyntaxException {
    setComboBox();
    disableButtonIfEmpty();
  }

  /** Sets the combo box with the available access options. */
  @FXML
  private void setComboBox() {
    ObservableList<String> options = FXCollections.observableArrayList("Remote", "Direct");
    accessDropDown.getItems().addAll(options);
  }

  /**
   * Method for handling log in button click. Sets access type based on what the user has selected.
   */
  @FXML
  private void handleLogInAction() throws Exception {
    String access = accessDropDown.getValue();

    if (access != null) {
      if (access.equals("Remote")) {
        dataAccess = new RemoteAccess(new URI("http://localhost:8080/movierater/"));
      } else if (access.equals("Direct")) {
        dataAccess = new LocalAccess(System.getProperty("user.home") + "/saveFile_");
      }
    }
    openMainPage();
  }

  /** Opens the main page. */
  private void openMainPage() throws IOException {

    String userName = userNameInput.getText().toLowerCase();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
    Parent root = loader.load();
    mainPageController = loader.getController();

    // Sets username, shows error message (called inside mainpagecontroller) if invalid username
    mainPageController.setUserName(userName);

    // Check if username was valid
    if (mainPageController.getUserName() != null) {
      // If username is valid, set the dataaccess
      mainPageController.setDataAccess(dataAccess);
      // Try to set user to read(get) from data access
      try {
        mainPageController.setUser(dataAccess.getUserFromFile(userName));
        // If it does not work, then:
      } catch (Exception e) {
        // Try to create new user from data access and set that user
        try {
          mainPageController.setUser(dataAccess.createUser(userName));
          mainPageController.showInfoAlert("New user '" + userName + "' created!");
          System.out.println("New user '" + userName + "' created!");
          // If createUser() throws error, then show error message and return
        } catch (Exception e2) {
          mainPageController.showErrorAlert("Could not access");
          return;
        }
      }
      mainPageController.setUp(userName);
      Stage stage = (Stage) userNameInput.getScene().getWindow();
      stage.setScene(new Scene(root));
      stage.setTitle("MovieRater");
      stage.centerOnScreen();
    }
  }

  /** Disables the login button if the username input is empty. */
  private void disableButtonIfEmpty() {
    loginButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> userNameInput.getText().trim().isEmpty(), userNameInput.textProperty()));
  }

  /** Method for setting data access type. */
  void setDataAccess(DataAccess dataAccess) {
    this.dataAccess = dataAccess;
  }
}
