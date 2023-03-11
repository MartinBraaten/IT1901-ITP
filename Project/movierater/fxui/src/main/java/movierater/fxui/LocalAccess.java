package movierater.fxui;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import movierater.core.User;
import movierater.json.UserPersistence;

public class LocalAccess implements DataAccess {

  private final UserPersistence userPersistence = new UserPersistence();

  private final String path;

  public LocalAccess(String path) {
    this.path = path;
  }

  /**
   * Gets a User object from file.
   *
   * @param userName userName of the User object
   * @return the User object if file exist, 'null' if file does not exist
   * @throws IOException if the named file does not exist, is a directory rather than a regular
   *     file, or for some other reason cannot be opened for reading.
   */
  @Override
  public User getUserFromFile(String userName) throws IOException {
    try (Reader reader = new FileReader(path + userName + ".json", StandardCharsets.UTF_8)) {
      User user = userPersistence.readUser(reader);
      System.out.println("Read from file: " + path + userName + ".json");
      return user;
    }
  }

  /**
   * Save a User object to file.
   *
   * @param user the User object to be saved
   * @throws IOException if the named file exists but is a directory rather than a regular file,
   *     does not exist but cannot be created, or cannot be opened for any other reason
   */
  @Override
  public void postUserToFile(User user) throws IOException {
    try (Writer writer =
        new FileWriter(path + user.getUserName() + ".json", StandardCharsets.UTF_8)) {
      userPersistence.writeUser(writer, user);
      System.out.println("Saved to file: " + path + user.getUserName() + ".json");
    }
  }

  /**
   * Deletes a User object from file.
   *
   * @param user the User object to be deleted
   * @throws IOException IOException
   */
  @Override
  public void deleteUser(User user) throws IOException {
    String userName = user.getUserName();
    Files.deleteIfExists(Path.of(path + userName + ".json"));
  }
}
