package movierater.fxui;

import java.io.IOException;
import movierater.core.FilmList;
import movierater.core.User;

public interface DataAccess {

  /**
   * Creates a new user with a "Default" FilmList.
   *
   * @param userName the User's userName
   * @return the User with the specified userName
   * @throws InterruptedException InterruptedException
   * @throws IOException IOException
   */
  default User createUser(String userName) throws IOException, InterruptedException {
    User user = new User(userName);
    FilmList defaultList = new FilmList();
    user.addFilmList("Default", defaultList);
    postUserToFile(user);
    return user;
  }

  /**
   * Get a User object from data source.
   *
   * @param userName the User's userName
   * @return the User with the specified userName
   * @throws InterruptedException InterruptedException
   * @throws IOException IOException
   */
  User getUserFromFile(String userName) throws IOException, InterruptedException;

  /**
   * Post a User objcet to data source.
   *
   * @param user the User to be posted
   * @throws InterruptedException InterruptedException
   * @throws IOException IOException
   */
  void postUserToFile(User user) throws IOException, InterruptedException;

  /**
   * Delete a User object from data source.
   *
   * @param user the User's userName
   * @throws IOException IOException
   */
  void deleteUser(User user) throws IOException;
}
