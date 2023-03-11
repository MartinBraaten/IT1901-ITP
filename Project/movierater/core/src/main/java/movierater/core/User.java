package movierater.core;

import java.util.HashMap;

public class User {

  private String userName;
  private HashMap<String, FilmList> filmListMap = new HashMap<>();

  public User(String userName) {
    setUserName(userName);
  }

  public User() {}

  public String getUserName() {
    return this.userName;
  }

  /**
   * Sets the username. Throws IllegalArgumentException if invalid input.
   *
   * @param userName the name of the user
   */
  public void setUserName(String userName) {
    if (userName == null || userName.isEmpty()) {
      throw new IllegalArgumentException("Invalid username");
    }
    if (userName.length() > 20) {
      throw new IllegalArgumentException("Username is too long");
    }
    if (userName.matches("[a-zA-Z]+")) {
      this.userName = userName;
    } else {
      throw new IllegalArgumentException("Username can only contain letters");
    }
  }

  /**
   * Adds a new filmlist to the user.
   *
   * @param key key for hashmap
   * @param filmList filmlist object
   */
  public void addFilmList(String key, FilmList filmList) {
    this.filmListMap.put(key, filmList);
  }

  /**
   * Adds a film to specified filmlist.
   *
   * @param film film obect.
   * @param filmListKey key for hashmap.
   */
  public void addFilmToFilmList(Film film, String filmListKey) {
    this.filmListMap.get(filmListKey).addFilm(film);
  }

  public FilmList getFilmList(String key) {
    return this.filmListMap.get(key);
  }

  public HashMap<String, FilmList> getFilmListMap() {
    return new HashMap<>(filmListMap);
  }

  public void deleteFilmList(String filmListKey) {
    filmListMap.remove(filmListKey);
  }
}
