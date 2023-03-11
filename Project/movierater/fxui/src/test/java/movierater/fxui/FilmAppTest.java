package movierater.fxui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import movierater.core.Film;
import movierater.core.FilmList;
import movierater.core.User;
import movierater.json.UserPersistence;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testfx.framework.junit5.ApplicationTest;

@TestMethodOrder(OrderAnnotation.class)
class FilmAppTest extends ApplicationTest {

  private LoginPageController loginController;
  private static final User testuser = new User("testuser");
  private static final DataAccess mockAccess = mock(DataAccess.class);
  private static final UserPersistence userPersistence = new UserPersistence();

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage_Test.fxml"));
    final Parent root = loader.load();
    this.loginController = loader.getController();
    stage.setTitle("MovieRater - App_Test");
    stage.setScene(new Scene(root));
    stage.show();
  }

  /* Setup for running tests in headless mode */
  @BeforeAll
  public static void setupHeadless() {
    if (Boolean.getBoolean("headless")) {
      System.setProperty("testfx.robot", "glass");
      System.setProperty("testfx.headless", "true");
      System.setProperty("prism.order", "sw");
      System.setProperty("prism.text", "t2k");
      System.setProperty("java.awt.headless", "true");
    }
  }

  @BeforeAll
  public static void setUp() {
    FilmList filmList = new FilmList();
    Film testFilm1 = new Film("testFilm1", 2000, 1, "comment1", true);
    Film testFilm2 = new Film("testFilm2", 2001, 2, "comment2", true);
    filmList.addFilm(testFilm1);
    filmList.addFilm(testFilm2);
    testuser.addFilmList("filmList", filmList);
    try (Writer writer =
        new FileWriter(
            System.getProperty("user.home") + "/saveFile_testuser.json", StandardCharsets.UTF_8)) {
      userPersistence.writeUser(writer, testuser);
    } catch (IOException e) {
      System.out.println("Could not set up save file");
    }
  }

  @BeforeAll
  public static void setUpMockito() throws IOException, InterruptedException {
    when(mockAccess.getUserFromFile("testuser")).thenReturn(testuser);
    when(mockAccess.getUserFromFile("newuser")).thenThrow(IOException.class);
    when(mockAccess.createUser("newuser")).thenReturn(new User("newuser"));
    doNothing().when(mockAccess).postUserToFile(testuser);
  }

  @BeforeEach
  public void setMockAccess() {
    loginController.setDataAccess(mockAccess);
  }

  @Test
  public void testControllerInitialize() {
    assertNotNull(this.loginController);
  }

  /* Tests for newUser with mockAccess */
  @Nested
  class TestNewUserMock {

    @Test
    public void testNewUserLoginAndDelete() {
      clickOn("#userNameInput").write("newUser").clickOn("#loginButton");
      clickOn("OK");
      assertTrue(lookup("#filmTableView").query().isVisible());

      clickOn("#deleteUserButton");
      clickOn("Cancel");
      assertTrue(lookup("#filmTableView").query().isVisible());

      clickOn("#deleteUserButton");
      clickOn("OK");
      clickOn("OK");
      assertTrue(lookup("#userNameInput").query().isVisible());
    }

    @Test
    public void testInvalidLogin() {
      clickOn("#userNameInput").write("newUser1").clickOn("#loginButton");
      clickOn("OK");
      assertTrue(lookup("#userNameInput").query().isVisible());
    }
  }

  /* Tests for testUser with mockAccess */
  @Nested
  class testUserMock {

    @BeforeEach
    public void loginValid() {
      clickOn("#userNameInput").write("testUser");
      clickOn("#loginButton");
    }

    @Test
    public void testLogOut() {
      clickOn("#logOutButton");
      assertTrue(lookup("#userNameInput").query().isVisible());
    }

    @Test
    public void testAddEditAndDeleteFilm() {
      clickOn("#addTitleText").write("Film3");
      clickOn("#addYearText").write("2003");
      clickOn("#addRatingText").write("5");
      clickOn("#addCommentText").write("interested");
      clickOn("#addFilmButton");

      Film newFilm = (Film) lookup("#filmTableView").queryTableView().getItems().get(2);
      // Sjekker at tableview har like mange filmer som filmList
      assertEquals(3, lookup("#filmTableView").queryTableView().getItems().size());
      // Sjekker at newFilm har "testFilm3" som tittel
      assertEquals("Film3", newFilm.getTitle());
      // Sjekker at newFilm har 2003 som år
      assertEquals(2003, newFilm.getYear());
      // Sjekker at newFilm har 3 som rating
      assertEquals(5, newFilm.getRating());
      // Sjekker comment
      assertEquals("interested", newFilm.getComment());
      // Sjekker at newFilm er sett
      assertFalse(newFilm.isSeen());

      // Prøver å redigere info i tabellen
      doubleClickOn("Film3").write("edited Film3").press(KeyCode.ENTER).release(KeyCode.ENTER);
      doubleClickOn("2003").write("2004").press(KeyCode.ENTER).release(KeyCode.ENTER);
      doubleClickOn("5").write("2").press(KeyCode.ENTER).release(KeyCode.ENTER);
      doubleClickOn("interested").write("bad movie").press(KeyCode.ENTER).release(KeyCode.ENTER);
      doubleClickOn("false").write("true").press(KeyCode.ENTER).release(KeyCode.ENTER);
      // Sjekker at filmen er blitt redigert
      Film film = (Film) lookup("#filmTableView").queryTableView().getItems().get(2);
      assertEquals("edited Film3", film.getTitle());
      assertEquals(2004, film.getYear());
      assertEquals(2, film.getRating());
      assertEquals("bad movie", film.getComment());
      assertTrue(film.isSeen());

      clickOn("#deleteFilmButton");
      // Sjekker at tableview har 2 filmer igjen
      assertEquals(2, lookup("#filmTableView").queryTableView().getItems().size());
    }

    @Test
    public void testEditWithInvalidInput() {
      // Prøver å redigere info i tabellen
      doubleClickOn("testFilm1")
          .press(KeyCode.BACK_SPACE)
          .press(KeyCode.ENTER)
          .release(KeyCode.ENTER);
      clickOn("OK");
      doubleClickOn("2000").write("3").press(KeyCode.ENTER).release(KeyCode.ENTER);
      clickOn("OK");
      doubleClickOn("1").write("99").press(KeyCode.ENTER).release(KeyCode.ENTER);
      clickOn("OK");
      // Sjekker at filmen ikke er blitt redigert
      Film film = (Film) lookup("#filmTableView").queryTableView().getItems().get(0);
      assertEquals("testFilm1", film.getTitle());
      assertEquals(2000, film.getYear());
      assertEquals(1, film.getRating());
    }

    @Test
    public void testAddFilmWithInvalidInput() {
      // Prøver å legge til med feil input
      clickOn("#addTitleText").write("testFilm");
      clickOn("#addYearText").write("a");
      clickOn("#addRatingText").write("b");
      clickOn("#addFilmButton");
      clickOn("OK");
      // Sjekker at ingen nye filmer ble lagt til
      assertEquals(2, lookup("#filmTableView").queryTableView().getItems().size());
      // Prøver igjen, men rating fortsatt bokstav og ikke nummer
      clickOn("#addYearText").press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE).write("2000");
      clickOn("#addFilmButton");
      clickOn("OK");
      // Sjekker at ingen ny filmer ble lagt til
      assertEquals(2, lookup("#filmTableView").queryTableView().getItems().size());
      // Prøver med ny rating, men ikke mellom 0-10
      clickOn("#addRatingText").press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE).write("99");
      clickOn("#addFilmButton");
      clickOn("OK");
      // Sjekker at ingen nye filmer ble lagt til
      assertEquals(2, lookup("#filmTableView").queryTableView().getItems().size());
    }

    @Test
    public void testAddFilmListAreadyExists() {
      clickOn("#inputFilmList").write("filmList");
      clickOn("#addFilmListButton");
      clickOn("OK");
      // Sjekker at filmlisten ikke ble overskrevet
      assertEquals(2, lookup("#filmTableView").queryTableView().getItems().size());
    }
  }

  /* Tests for direct access and remote without server */
  @Nested
  class testAccess {

    @Test
    public void testDirectAddDeleteFilmListAndDeleteUser() {
      clickOn("#userNameInput").write("testUser");
      clickOn("#accessDropDown");
      clickOn("Direct").clickOn("#loginButton");

      clickOn("#inputFilmList").write("testList");
      clickOn("#addFilmListButton");
      assertEquals("testList", lookup("#dropDownList").queryComboBox().getValue());

      clickOn("#dropDownList").clickOn("filmList");
      assertEquals(2, lookup("#filmTableView").queryTableView().getItems().size());

      clickOn("#dropDownList").clickOn("testList");
      clickOn("#deleteFilmListButton");
      clickOn("Cancel");
      assertEquals("testList", lookup("#dropDownList").queryComboBox().getValue());

      clickOn("#deleteFilmListButton");
      clickOn("OK");
      assertNotEquals("testList", lookup("#dropDownList").queryComboBox().getValue());

      clickOn("#deleteUserButton");
      clickOn("OK").clickOn("OK");
      assertTrue(lookup("#userNameInput").query().isVisible());
    }

    /* This test will fail if server is running*/
    @Test
    public void testRemoteNotRunningServer() {
      clickOn("#userNameInput").write("testUser");
      clickOn("#accessDropDown");
      clickOn("Remote").clickOn("#loginButton").clickOn("OK");
      assertTrue(lookup("#userNameInput").query().isVisible());
    }
  }
}
