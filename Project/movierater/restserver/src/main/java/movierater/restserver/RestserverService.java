package movierater.restserver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import movierater.core.User;
import movierater.json.UserPersistence;
import org.springframework.stereotype.Service;

/*
 * Service for handling reading, saving and deleting from data source (json file)
 */
@Service
public class RestserverService {

  private final UserPersistence userPersistence = new UserPersistence();
  private static final String pathString = System.getProperty("user.home") + "/saveFile_";

  /**
   * (GET) Reads a User object from json file and returns the User.
   *
   * @param userName username of User object to get
   * @return User
   */
  public User getUser(String userName) throws IOException {

    try (Reader reader = new FileReader(pathString + userName + ".json", StandardCharsets.UTF_8)) {
      User user = userPersistence.readUser(reader);
      System.out.println("Read from file: " + pathString + userName + ".json");
      return user;
    }
  }

  /**
   * (POST) Saves a User object to json file. If the file already exists, it will be overwritten.
   *
   * @param user the User object to save
   * @param userName the userName of specified User object
   * @return Boolean
   */
  public Boolean postUser(User user, String userName) throws IOException {

    try (Writer writer = new FileWriter(pathString + userName + ".json", StandardCharsets.UTF_8)) {
      userPersistence.writeUser(writer, user);
      System.out.println("Saved to file: " + pathString + userName + ".json");
      return true;
    }
  }

  /**
   * (DELETE) Deletes the json file 'userName'.json. This will remove the User object saved in the
   * json file.
   *
   * @param userName name of the file to be deleted
   * @return Boolean
   */
  public Boolean deleteUser(String userName) {

    File file = new File(pathString + userName + ".json");
    if (file.exists()) {
      return file.delete();
    } else {
      return false;
    }
  }
}
