package movierater.fxui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import movierater.core.Film;
import movierater.core.FilmList;
import movierater.core.User;
import movierater.json.UserPersistence;
import movierater.restserver.RestserverApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.SpringApplication;
import org.testfx.framework.junit5.ApplicationTest;

public class MovieraterAppIT extends ApplicationTest {

  private static final String path = System.getProperty("user.home") + "/saveFile_";

  /** Starts the server before testing. */
  @BeforeAll
  public static void startServer() {
    SpringApplication.run(RestserverApplication.class);
  }

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage_IT.fxml"));
    final Parent root = loader.load();
    LoginPageController loginPageController = loader.getController();
    DataAccess dataAccess = new RemoteAccess(new URI("http://localhost:8080/movierater/"));
    loginPageController.setDataAccess(dataAccess);
    stage.setTitle("MovieRater - App_Test");
    stage.setScene(new Scene(root));
    stage.show();
  }

  /** Deletes the json file after tests. */
  @AfterAll
  public static void tearDown() {
    new File(path + "newuser.json").delete();
  }

  /**
   * Helper method to get user from file for testing.
   *
   * @return User
   */
  private User getUser() {
    try (Reader reader = new FileReader(path + "newuser" + ".json", StandardCharsets.UTF_8)) {
      UserPersistence userPersistence = new UserPersistence();
      return userPersistence.readUser(reader);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Nested
  @TestMethodOrder(OrderAnnotation.class)
  class loginNewUser {

    @BeforeEach
    public void testLogin() {
      clickOn("#userNameInput").write("newuser");
      clickOn("#accessDropDown").clickOn("Remote");
      clickOn("#loginButton");
    }

    @Test
    @Order(1)
    public void testAddFilmListAndFilm() {
      clickOn("OK");
      assertTrue(lookup("#filmTableView").query().isVisible());

      clickOn("#inputFilmList").write("TestList");
      clickOn("#addFilmListButton");
      clickOn("#addTitleText").write("testFilm3");
      clickOn("#addYearText").write("2002");
      clickOn("#addRatingText").write("3");
      clickOn("#addCommentText").write("comment3");
      clickOn("#addFilmButton");

      Film newFilm = (Film) lookup("#filmTableView").queryTableView().getItems().get(0);
      // Sjekker at tableview har like mange filmer som filmList
      assertEquals(1, lookup("#filmTableView").queryTableView().getItems().size());
      // Sjekker at newFilm har "testFilm3" som tittel
      assertEquals("testFilm3", newFilm.getTitle());
      // Sjekker at newFilm har 2003 som år
      assertEquals(2002, newFilm.getYear());
      // Sjekker at newFilm har 3 som rating
      assertEquals(3, newFilm.getRating());
      // Sjekker comment
      assertEquals("comment3", newFilm.getComment());
      // Sjekker at newFilm er sett
      assertFalse(newFilm.isSeen());
      // Sjekker at lagret filmliste inneholder rett film
      User user = getUser();
      if (user != null) {
        FilmList filmList = user.getFilmList("TestList");
        Film film = filmList.getFilmList().get(0);
        assertEquals("testFilm3", film.getTitle());
        assertEquals(2002, film.getYear());
        assertEquals(3, film.getRating());
        assertEquals("comment3", film.getComment());
        assertFalse(film.isSeen());
      }
    }

    @Test
    @Order(2)
    public void testDeleteFilm() {
      clickOn("testFilm3");
      clickOn("#deleteFilmButton");
      assertEquals(0, lookup("#filmTableView").queryTableView().getItems().size());
      User user = getUser();
      if (user != null) {
        FilmList filmList = user.getFilmList("TestList");
        assertEquals(0, filmList.getFilmList().size());
      }
    }

    @Test
    @Order(3)
    public void deleteFilmList() {
      clickOn("#deleteFilmListButton");
      clickOn("OK");
      assertEquals(1, lookup("#dropDownList").queryComboBox().getItems().size());
      User user = getUser();
      if (user != null) {
        assertEquals(1, user.getFilmListMap().size());
      }
    }

    @Test
    @Order(4)
    public void testDeleteUser() {
      clickOn("#deleteUserButton");
      clickOn("OK");
      clickOn("OK");
      assertTrue(lookup("#userNameInput").query().isVisible());
    }
  }
}
