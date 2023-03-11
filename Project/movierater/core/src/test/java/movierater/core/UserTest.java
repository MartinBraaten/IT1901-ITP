package movierater.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class UserTest {

  @Test
  public void testConstructor() {
    User user = new User("testUser");
    assertNotNull(user);
    assertEquals("testUser", user.getUserName());
  }

  @Test
  public void testEmptyConstructor() {
    User user = new User();
    assertNotNull(user);
    assertNull(user.getUserName());
  }

  @Test
  public void testSetGetUserName() {
    User user = new User();
    user.setUserName("testUser");
    assertEquals("testUser", user.getUserName());
  }

  @Test
  public void testInvalidUserName() {
    assertThrows(IllegalArgumentException.class, () -> new User(""));
    assertThrows(IllegalArgumentException.class, () -> new User(null));
    assertThrows(IllegalArgumentException.class, () -> new User("1213"));
    assertThrows(IllegalArgumentException.class, () -> new User("abcdefghijklmnopqrstu"));
  }

  @Test
  public void testAddGetFilmList() {
    User user = new User("testUser");
    FilmList list = new FilmList();
    Film film = new Film("testfilm", 2022, 10, "comment", true);
    user.addFilmList("list", list);
    assertNotNull(user.getFilmList("list"));
    user.addFilmToFilmList(film, "list");
    FilmListTest.checkFilm(film, "testfilm", 2022, 10, "comment", true);
  }

  @Test
  public void testDeletFilmList() {
    User user = new User("testUser");
    FilmList list = new FilmList();
    user.addFilmList("list", list);
    assertNotNull(user.getFilmList("list"));
    assertEquals(user.getFilmList("list"), list);
    user.deleteFilmList("list");
    assertNull(user.getFilmList("list"));
  }
}
