package movierater.core;

import java.util.ArrayList;
import java.util.Iterator;

public class FilmList implements Iterable<Film> {

  private final ArrayList<Film> filmList = new ArrayList<>();

  public FilmList() {}

  public ArrayList<Film> getFilmList() {
    return new ArrayList<>(filmList);
  }

  /**
   * Adds new film to the list.
   *
   * @param newFilm film to be added to the list.
   */
  public void addFilm(Film newFilm) {
    if (newFilm == null) {
      throw new IllegalArgumentException("Invalid film");
    }
    filmList.add(newFilm);
  }

  /**
   * Removes film from the list.
   *
   * @param film film to be removed from the list.
   */
  public void removeFilm(Film film) {
    if (film == null) {
      throw new IllegalArgumentException("Invalid film");
    }
    filmList.remove(film);
  }

  /**
   * An iterator that iterates through films of list.
   *
   * @return iterator that iterates through films of list.
   */
  @Override
  public Iterator<Film> iterator() {
    return filmList.iterator();
  }
}
