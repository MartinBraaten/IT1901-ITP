package movierater.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FilmListTest {

  // Helper methods to check film equal
  static void checkFilm(
      Film film, String title, int year, int rating, String comment, boolean seen) {
    assertEquals(title, film.getTitle());
    assertEquals(year, film.getYear());
    assertEquals(rating, film.getRating());
    assertEquals(comment, film.getComment());
    assertEquals(seen, film.isSeen());
  }

  static void checkFilm(Film film1, Film film2) {
    checkFilm(
        film1,
        film2.getTitle(),
        film2.getYear(),
        film2.getRating(),
        film2.getComment(),
        film2.isSeen());
  }

  @Test
  public void testConstructor() {
    FilmList filmList = new FilmList();
    assertTrue(filmList.getFilmList().isEmpty());
  }

  @Test
  public void testAddFilm() {
    FilmList newFilmList = new FilmList();
    Film film = new Film("test", 2021, 4, "testcomment", false);
    newFilmList.addFilm(film);
    assertEquals(1, newFilmList.getFilmList().size());
    assertEquals("test", newFilmList.getFilmList().get(0).getTitle());
    assertEquals(2021, newFilmList.getFilmList().get(0).getYear());
    assertEquals(4, newFilmList.getFilmList().get(0).getRating());
    assertSame("testcomment", newFilmList.getFilmList().get(0).getComment());
    assertFalse(newFilmList.getFilmList().get(0).isSeen());
    assertThrows(IllegalArgumentException.class, () -> newFilmList.addFilm(null));
  }

  @Test
  public void testRemoveFilm() {
    FilmList newFilmList = new FilmList();
    Film film = new Film("test", 2021, 4, "testcomment", false);
    newFilmList.addFilm(film);
    assertEquals(1, newFilmList.getFilmList().size());
    newFilmList.removeFilm(film);
    assertTrue(newFilmList.getFilmList().isEmpty());
    assertThrows(IllegalArgumentException.class, () -> newFilmList.removeFilm(null));
  }

  @Test
  public void testGetFilmList() {
    FilmList newFilmList = new FilmList();
    Film film = new Film("test", 2021, 4, "testcomment", false);
    newFilmList.addFilm(film);
    List<Film> testList = newFilmList.getFilmList();
    Iterator<Film> it = newFilmList.iterator();
    assertTrue(it.hasNext());
    checkFilm(it.next(), testList.get(0));
    assertFalse(it.hasNext());
    assertEquals(1, testList.size());
  }
}
