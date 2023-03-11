package movierater.core;

import java.time.YearMonth;

public class Film {

  private String title;
  private int year;
  private int rating;
  private String comment;
  private boolean seen;

  /**
   * Constructor for Film.
   *
   * @param title the title of the film
   * @param year the year the film was released
   * @param rating personal rating of the film 0-10
   * @param comment personal comment on the film
   * @param seen whether the film has been seen or not
   */
  public Film(String title, int year, int rating, String comment, boolean seen) {
    setTitle(title);
    setYear(year);
    setRating(rating);
    setComment(comment);
    setSeen(seen);
  }

  public Film() {}

  public String getTitle() {
    return this.title;
  }

  /**
   * Sets the title of the film.
   *
   * @param title the title of the film
   */
  public void setTitle(String title) {
    if (title == null || title.isEmpty()) {
      throw new IllegalArgumentException("Title cannot be empty");
    }
    this.title = title;
  }

  public int getYear() {
    return year;
  }

  /**
   * Sets the year the film was released.
   *
   * @param year the year the film was released
   */
  public void setYear(int year) {
    if (year < 1888 || year > YearMonth.now().getYear() + 5) {
      throw new IllegalArgumentException(
          "Invalid year, must be between 1888 and " + (YearMonth.now().getYear() + 5));
    }
    this.year = year;
  }

  public int getRating() {
    return rating;
  }

  /**
   * Sets the personal rating of the film.
   *
   * @param rating personal rating of the film (0-10)
   */
  public void setRating(int rating) {
    if (rating < 0 || rating > 10) {
      throw new IllegalArgumentException("Invalid rating, must be between 0 and 10");
    }
    this.rating = rating;
  }

  public String getComment() {
    return comment;
  }

  /**
   * Sets a comment on the film.
   *
   * @param comment personal comment on the film
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  public boolean isSeen() {
    return seen;
  }

  /**
   * Sets whether the film has been seen or not.
   *
   * @param seen boolean value on whether the film has been seen
   */
  public void setSeen(boolean seen) {
    this.seen = seen;
  }
}
