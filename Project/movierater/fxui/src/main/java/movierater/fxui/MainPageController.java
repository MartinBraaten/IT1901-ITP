package movierater.fxui;

import java.io.IOException;
import java.time.YearMonth;
import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;
import movierater.core.Film;
import movierater.core.FilmList;
import movierater.core.User;

public class MainPageController {

  private User user = new User();
  private final ObservableList<Film> tableViewFilmList = FXCollections.observableArrayList();
  private DataAccess dataAccess;

  @FXML TableView<Film> filmTableView;
  @FXML TableColumn<Film, String> title;
  @FXML TableColumn<Film, String> comment;
  @FXML TableColumn<Film, Integer> rating;
  @FXML TableColumn<Film, Integer> year;
  @FXML TableColumn<Film, Boolean> seen;
  @FXML TextField addTitleText;
  @FXML TextField addYearText;
  @FXML TextField addRatingText;
  @FXML TextField addCommentText;
  @FXML TextField inputFilmList;
  @FXML Label userNameLabel;
  @FXML CheckBox seenFilmCheckBox;
  @FXML ComboBox<String> dropDownList;
  @FXML Button addFilmButton;
  @FXML Button deleteFilmButton;
  @FXML Button logOutButton;
  @FXML Button addFilmListButton;
  @FXML Button deleteFilmListButton;
  @FXML Button deleteUserButton;

  /** Initializes the main page. */
  @FXML
  private void initialize() {
    dropDownList.setVisibleRowCount(5);
  }

  /**
   * Sets up the mainPage and sets usernamelabel to 'userName'.
   *
   * @param userName the username
   */
  void setUp(String userName) {
    updateDropDownList(null);
    userNameLabel.setText(userName);
    setUpFilmTableView();
    updateFilmTableView(dropDownList.getValue());
    disableButtons();
  }

  /** Updates the TableView when changing filmList */
  @FXML
  private void handleChangeListAction() {
    String filmListKey = dropDownList.getValue();
    updateFilmTableView(filmListKey);
  }

  @FXML
  private void handleAddFilmListAction() throws IOException, InterruptedException {
    String filmListInput = inputFilmList.getText().trim();
    if (user.getFilmListMap().get(filmListInput) == null) {
      user.addFilmList(filmListInput, new FilmList());
      updateDropDownList(filmListInput);
      inputFilmList.clear();
      dataAccess.postUserToFile(user);
    } else {
      showErrorAlert(filmListInput + " already exists.");
    }
  }

  @FXML
  private void handleDeleteFilmListAction() throws IOException, InterruptedException {
    String listToDelete = dropDownList.getValue();
    // Alert to confirm delete
    Alert alert =
        new Alert(
            AlertType.CONFIRMATION,
            "Are you sure you want to delete " + "'" + listToDelete + "'" + "?");
    alert
        .showAndWait()
        .filter(response -> response == ButtonType.OK)
        .ifPresent(
            response -> {
              user.deleteFilmList(listToDelete);
              updateDropDownList(null);
            });
    dataAccess.postUserToFile(user);
  }

  @FXML
  private void handleAddFilmAction() throws IOException, InterruptedException {
    int yearInput;
    int ratingInput;
    String filmListKey = dropDownList.getValue();

    try {
      yearInput = Integer.parseInt(addYearText.getText().trim());
    } catch (Exception e) {
      showErrorAlert("Year must be number");
      return;
    }

    try {
      ratingInput = Integer.parseInt(addRatingText.getText().trim());
    } catch (Exception e) {
      showErrorAlert("Rating must be a number between 0 and 10");
      return;
    }

    try {
      Film film =
          new Film(
              addTitleText.getText().trim(),
              yearInput,
              ratingInput,
              addCommentText.getText().trim(),
              seenFilmCheckBox.isSelected());
      user.addFilmToFilmList(film, filmListKey);
      System.out.println("Added " + film.getTitle() + " to " + filmListKey);
      dataAccess.postUserToFile(user);
      updateFilmTableView(filmListKey);
      clearTextField();
    } catch (IllegalArgumentException e) {
      showErrorAlert(e.getMessage());
    }
  }

  @FXML
  private void handleDeleteFilmAction() throws IOException, InterruptedException {
    Film film = filmTableView.getSelectionModel().getSelectedItem();
    String filmListKey = dropDownList.getValue();

    if (film != null) {
      System.out.println("Deleted " + film.getTitle() + " from " + filmListKey);
      user.getFilmList(filmListKey).removeFilm(film);
    }
    updateFilmTableView(filmListKey);
    dataAccess.postUserToFile(user);
  }

  @FXML
  private void handleDeleteUserAction() {
    String userName = user.getUserName();
    Alert alert =
        new Alert(
            AlertType.CONFIRMATION,
            "Are you sure you want to delete the user " + "'" + userName + "'" + "?");
    alert
        .showAndWait()
        .filter(response -> response == ButtonType.OK)
        .ifPresent(
            response -> {
              try {
                dataAccess.deleteUser(this.user);
                System.out.println("User '" + userName + "' was deleted");
                showInfoAlert("User '" + userName + "' was deleted");
                handleLogOutAction();
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
  }

  /** Log out user and return to login page. */
  @FXML
  private void handleLogOutAction() throws IOException {
    Stage stage = (Stage) logOutButton.getScene().getWindow();
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginPage.fxml")));
    stage.setTitle("MovieRater - Login");
    stage.setScene(new Scene(root));
    stage.centerOnScreen();
  }

  /** Updates the TableView to show FilmList with selected filmListKey */
  private void updateFilmTableView(String filmListKey) {
    ObservableList<Film> viewList = filmTableView.getItems();
    viewList.clear();
    if (this.user.getFilmList(filmListKey) != null) {
      for (Film film : this.user.getFilmList(filmListKey)) {
        viewList.add(film);
      }
      filmTableView.setItems(viewList);
    } else {
      System.out.println("Empty tableview");
    }
  }

  /** Updates the dropdown list (ComboBox) with new selection. */
  private void updateDropDownList(String newSelection) {
    dropDownList.getItems().clear();
    dropDownList.getItems().addAll(user.getFilmListMap().keySet());
    if (newSelection != null) {
      dropDownList.setValue(newSelection);
    } else {
      dropDownList.getSelectionModel().selectFirst();
    }
  }

  /** Clears the text field after adding a film. */
  private void clearTextField() {
    addTitleText.clear();
    addRatingText.clear();
    addYearText.clear();
    addCommentText.clear();
    seenFilmCheckBox.setSelected(false);
  }

  /** Sets up the filmTableView with each cell. */
  private void setUpFilmTableView() {

    title.setCellValueFactory(new PropertyValueFactory<>("title"));
    year.setCellValueFactory(new PropertyValueFactory<>("year"));
    rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
    comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
    seen.setCellValueFactory(new PropertyValueFactory<>("seen"));
    filmTableView.setItems(tableViewFilmList);

    // Make each cell editable
    editCell();
  }

  /** Sets the username, label. Shows error message if invalid username. */
  void setUserName(String userName) {
    // Set username with filmlist. Show error message if invalid username
    try {
      user = new User(userName);
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid username");
      showErrorAlert(e.getMessage());
    }
  }

  void setUser(User user) {
    this.user = user;
  }

  String getUserName() {
    return this.user.getUserName();
  }

  void setDataAccess(DataAccess dataAccess) {
    this.dataAccess = dataAccess;
  }

  /**
   * Shows error message.
   *
   * @param message message
   */
  void showErrorAlert(String message) {
    Alert errorAlert = new Alert(AlertType.ERROR);
    errorAlert.setTitle("Error message");
    errorAlert.setContentText(message);
    errorAlert.showAndWait();
  }

  /**
   * Shows info message.
   *
   * @param message message
   */
  void showInfoAlert(String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Disables deleteFilmButton if no film is selected. Disables deleteFilmListButton if there are
   * less than 2 lists. Disables addFilmButton if no title text, year or rating is entered. Disables
   * addFilmListButton if no filmListName is entered.
   */
  private void disableButtons() {

    deleteFilmButton
        .disableProperty()
        .bind(filmTableView.getSelectionModel().selectedItemProperty().isNull());

    deleteFilmListButton.disableProperty().bind(Bindings.size(dropDownList.getItems()).lessThan(2));

    addFilmButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> addTitleText.getText().trim().isEmpty(), addTitleText.textProperty()));
    addFilmButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> addYearText.getText().trim().isEmpty(), addYearText.textProperty()));
    addFilmButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> addRatingText.getText().trim().isEmpty(), addRatingText.textProperty()));
    addFilmListButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> inputFilmList.getText().trim().isEmpty(), inputFilmList.textProperty()));
  }

  /** Makes each cell editable. */
  private void editCell() {
    filmTableView.setEditable(true);
    title.setCellFactory(TextFieldTableCell.forTableColumn());
    year.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    rating.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    comment.setCellFactory(TextFieldTableCell.forTableColumn());
    seen.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));

    title.setOnEditCommit(
        (TableColumn.CellEditEvent<Film, String> event) -> {
          TablePosition<Film, String> pos = event.getTablePosition();
          String newTitle = event.getNewValue();
          int row = pos.getRow();
          Film film = event.getTableView().getItems().get(row);
          try {
            film.setTitle(newTitle);
            dataAccess.postUserToFile(user);
          } catch (Exception e) {
            showErrorAlert("Title cannot be empty");
            filmTableView.refresh();
          }
        });

    year.setOnEditCommit(
        (TableColumn.CellEditEvent<Film, Integer> event) -> {
          TablePosition<Film, Integer> pos = event.getTablePosition();
          Integer newYear = event.getNewValue();
          int row = pos.getRow();
          Film film = event.getTableView().getItems().get(row);
          try {
            film.setYear(newYear);
            dataAccess.postUserToFile(user);
          } catch (Exception e) {
            showErrorAlert(
                "Invalid year, must be between 1888 and " + (YearMonth.now().getYear() + 5));
            filmTableView.refresh();
          }
        });

    rating.setOnEditCommit(
        (TableColumn.CellEditEvent<Film, Integer> event) -> {
          TablePosition<Film, Integer> pos = event.getTablePosition();
          Integer newRating = event.getNewValue();
          int row = pos.getRow();
          Film film = event.getTableView().getItems().get(row);
          try {
            film.setRating(newRating);
            dataAccess.postUserToFile(user);
          } catch (Exception e) {
            showErrorAlert("Invalid rating, must be a single digit between 0 and 10");
            filmTableView.refresh();
          }
        });

    comment.setOnEditCommit(
        (TableColumn.CellEditEvent<Film, String> event) -> {
          TablePosition<Film, String> pos = event.getTablePosition();
          String newComment = event.getNewValue();
          int row = pos.getRow();
          Film film = event.getTableView().getItems().get(row);
          film.setComment(newComment);
          try {
            dataAccess.postUserToFile(user);
          } catch (Exception e) {
            showErrorAlert("Failed to edit comment");
          }
        });

    seen.setOnEditCommit(
        (TableColumn.CellEditEvent<Film, Boolean> event) -> {
          TablePosition<Film, Boolean> pos = event.getTablePosition();
          Boolean newSeen = event.getNewValue();
          int row = pos.getRow();
          Film film = event.getTableView().getItems().get(row);
          film.setSeen(newSeen);
          try {
            dataAccess.postUserToFile(user);
          } catch (Exception e) {
            showErrorAlert("Failed to edit seen");
          }
        });
  }
}
