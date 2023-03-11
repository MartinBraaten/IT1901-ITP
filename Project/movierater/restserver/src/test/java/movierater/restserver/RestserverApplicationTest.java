package movierater.restserver;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import movierater.core.Film;
import movierater.core.FilmList;
import movierater.core.User;
import movierater.json.UserPersistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
@ContextConfiguration(
    classes = {RestserverController.class, RestserverService.class, RestserverApplication.class})
@WebMvcTest
class RestserverApplicationTest {

  @Autowired private MockMvc mockMvc;

  private final ObjectMapper mapper = new ObjectMapper();
  private static final String pathString =
      System.getProperty("user.home") + "/saveFile_testuser.json";

  @BeforeAll
  public static void setUpTestUser() {
    User testUser = new User("testuser");
    FilmList testList = new FilmList();
    Film testfilm = new Film("TestTitle", 2020, 8, "comment", true);
    testUser.addFilmList("testList", testList);
    testUser.addFilmToFilmList(testfilm, "testList");

    try (Writer writer = new FileWriter(pathString, StandardCharsets.UTF_8)) {
      UserPersistence userPersistence = new UserPersistence();
      userPersistence.writeUser(writer, testUser);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMain() {
    RestserverApplication.main(new String[] {});
    assertTrue(true);
  }

  @Test
  public void testGetUser() throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/movierater/testuser")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.userName").value("testuser"))
            .andReturn();

    try {
      User user = mapper.readValue(result.getResponse().getContentAsString(), User.class);
      Iterator<Film> it = user.getFilmList("testList").iterator();
      assertTrue(it.hasNext());
      Film testfilm = it.next();
      assertFalse(it.hasNext());
      assertEquals("TestTitle", testfilm.getTitle());
    } catch (JsonProcessingException e) {
      fail(e.getMessage());
    }
  }

  @Test
  @Order(1)
  public void testPostUser() throws Exception {
    String testUser = mapper.writeValueAsString(new User("testpost"));

    mockMvc
        .perform(
            (MockMvcRequestBuilders.post("/movierater/testpost")
                .content(testUser)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
        .andExpect(jsonPath("$", equalTo(true)));
  }

  @Test
  @Order(2)
  public void testDeleteUser() throws Exception {

    mockMvc
        .perform(
            (MockMvcRequestBuilders.delete("/movierater/testpost")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", equalTo(true)));
  }

  @Test
  public void testDeleteNoUser() throws Exception {
    mockMvc
        .perform(
            (MockMvcRequestBuilders.delete("/movierater/nouser")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", equalTo(false)));
  }

  @AfterAll
  public static void tearDown() {
    new File(pathString).delete();
  }
}
