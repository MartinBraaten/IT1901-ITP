package movierater.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FilmTest {
  @Test
  public void testConstructor() {
    Film film = new Film("test", 2021, 4, "testcomment", false);
    assertEquals("test", film.getTitle());
    assertEquals(2021, film.getYear());
    assertEquals(4, film.getRating());
    assertSame("testcomment", film.getComment());
    assertFalse(film.isSeen());
  }

  @Test
  public void testEmptyConstructor() {
    Film film = new Film();
    assertNull(film.getTitle());
    assertEquals(0, film.getYear());
    assertEquals(0, film.getRating());
    assertNull(film.getComment());
    assertFalse(film.isSeen());
  }

  @Test
  public void testThrowsException() {
    Film film = new Film("test", 2021, 4, "testcomment", false);
    assertThrows(IllegalArgumentException.class, () -> film.setYear(1887));
    assertThrows(IllegalArgumentException.class, () -> film.setYear(2040));
    assertThrows(IllegalArgumentException.class, () -> film.setRating(11));
    assertThrows(IllegalArgumentException.class, () -> film.setRating(-1));
    assertThrows(IllegalArgumentException.class, () -> film.setTitle(""));
    assertThrows(IllegalArgumentException.class, () -> film.setTitle(null));
  }

  @Test
  public void testSetTitle() {
    Film film = new Film();
    film.setTitle("test");
    assertEquals("test", film.getTitle());
  }

  @Test
  public void testSetYear() {
    Film film = new Film("test", 2021, 2, "test", false);
    film.setYear(2022);
    assertEquals(2022, film.getYear());
  }

  @Test
  public void testSetRating() {
    Film film = new Film("test", 2021, 2, "test", false);
    film.setRating(5);
    assertEquals(5, film.getRating());
  }

  @Test
  public void testSetComments() {
    Film film = new Film("test", 2021, 2, "test", false);
    film.setComment("test2");
    assertEquals("test2", film.getComment());
  }

  @Test
  public void testSetSeen() {
    Film film = new Film("test", 2021, 2, "test", false);
    film.setSeen(true);
    assertTrue(film.isSeen());
  }

  @Test
  public void testSetNewTitle() {
    Film film = new Film("test", 2021, 2, "test", false);
    film.setTitle("test2");
    assertEquals("test2", film.getTitle());
  }
}
