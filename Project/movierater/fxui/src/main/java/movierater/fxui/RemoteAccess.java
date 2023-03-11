package movierater.fxui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import movierater.core.User;

public class RemoteAccess implements DataAccess {

  private final URI endpointBaseUri;
  private final ObjectMapper objectMapper;

  public RemoteAccess(final URI endpointBaseUri) {
    this.endpointBaseUri = endpointBaseUri;
    this.objectMapper = new ObjectMapper();
  }

  /**
   * Sends a GET request for User object with specified userName.
   *
   * @param userName userName of the User object to get
   * @return the User object with the specified userName
   * @throws InterruptedException InterruptedException
   * @throws IOException IOException
   */
  @Override
  public User getUserFromFile(String userName) throws IOException, InterruptedException {
    HttpRequest request =
        HttpRequest.newBuilder(URI.create(endpointBaseUri + userName))
            .header("Accept", "application/json")
            .GET()
            .build();

    final HttpResponse<String> response =
        HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
    final String responseString = response.body();
    System.out.println("getUser() response: " + responseString);
    return objectMapper.readValue(responseString, User.class);
  }

  /**
   * Send a POST request for a User object.
   *
   * @param user the User object to post
   * @throws InterruptedException InterruptedException
   * @throws IOException IOException
   */
  @Override
  public void postUserToFile(User user) throws IOException, InterruptedException {

    String data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
    HttpRequest request =
        HttpRequest.newBuilder(URI.create(endpointBaseUri + user.getUserName()))
            .header("content-type", "application/json")
            .header("Accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(data))
            .build();
    final HttpResponse<String> response =
        HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
    String responseString = response.body();
    System.out.println("postUser() response: " + responseString);
  }

  /**
   * Sends a DELETE request for User object with specified userName.
   *
   * @param user userName of the User object to get
   */
  @Override
  public void deleteUser(User user) {
    String userName = user.getUserName();
    HttpRequest request =
        HttpRequest.newBuilder(URI.create(endpointBaseUri + userName))
            .header("Accept", "application/json")
            .DELETE()
            .build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      final String responseString = response.body();
      System.out.println("deleteUser() response: " + responseString);
    } catch (IOException | InterruptedException e) {
      System.out.println("Could not delete user from remote");
    }
  }
}
