package movierater.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import movierater.core.User;

/*
 * Persistence class that handles reading and saving to json format using Jackson.
 */
public class UserPersistence {
  private final ObjectMapper mapper;

  public UserPersistence() {
    mapper = new ObjectMapper();
  }

  /**
   * Reads a user from JSON.
   *
   * @param reader reader object that reads from JSON
   * @return user object
   * @throws IOException IOException
   */
  public User readUser(Reader reader) throws IOException {
    return mapper.readValue(reader, User.class);
  }

  /**
   * Writes a user to JSON format.
   *
   * @param writer writer object that writes to JSON format
   * @param user user object
   * @throws IOException IOException
   */
  public void writeUser(Writer writer, User user) throws IOException {
    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, user);
  }
}
