package movierater.restserver;

import java.io.IOException;
import movierater.core.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * RestController that handles GET, POST and DELETE mappings.
 */
@RestController
@RequestMapping(path = "/movierater")
public class RestserverController {

  @Autowired private RestserverService restserverService;

  /**
   * Gets User with the specified username from server.
   *
   * @param user username of the user
   * @return User object
   */
  @GetMapping("{user}")
  public User getUser(@PathVariable String user) throws IOException {
    return restserverService.getUser(user);
  }

  /**
   * Adds or replaces User with specified username to server.
   *
   * @param user username of the user
   */
  @PostMapping("{user}")
  public Boolean postUser(@RequestBody User userClass, @PathVariable String user)
      throws IOException {
    return restserverService.postUser(userClass, user);
  }

  /**
   * Deletes User with specified username from server.
   *
   * @param user username of the user
   */
  @DeleteMapping("{user}")
  public Boolean deleteUser(@PathVariable String user) {
    return restserverService.deleteUser(user);
  }
}
