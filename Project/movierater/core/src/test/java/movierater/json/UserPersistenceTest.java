package movierater.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import movierater.core.Film;
import movierater.core.FilmList;
import movierater.core.User;
import org.junit.jupiter.api.Test;

public class UserPersistenceTest {

  private final UserPersistence userPersistence = new UserPersistence();

  private User createTestUser() {

    User user = new User("testUser");
    FilmList list = new FilmList();
    Film film1 = new Film("film1", 2000, 9, "testcomment1", true);
    list.addFilm(film1);
    user.addFilmList("list", list);
    return user;
  }

  private void checkUsersAreEqual(User user1, User user2) {
    assertEquals(user1.getUserName(), user2.getUserName());
    assertEquals(user1.getFilmListMap().keySet(), user2.getFilmListMap().keySet());
  }

  @Test
  public void testWriteAndReadUser() {
    User user = createTestUser();
    try {
      StringWriter writer = new StringWriter();
      userPersistence.writeUser(writer, user);
      String jsonString = writer.toString();
      User user2 = userPersistence.readUser(new StringReader(jsonString));
      checkUsersAreEqual(user, user2);
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }
}
