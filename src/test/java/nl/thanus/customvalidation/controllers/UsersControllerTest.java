package nl.thanus.customvalidation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.thanus.customvalidation.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateUser() throws Exception {
        final User user = new User();
        user.setUsername("SPRING-123");
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setPhone("+31234567890");

        final String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                post("/user")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userJson));
    }

    @Test
    public void shouldNotCreateUserWhenUsernameIsInvalid() throws Exception {
        final User user = new User();
        user.setUsername("SPRING");
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setPhone("1234567890");

        mockMvc.perform(
                post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateUserWhenPhoneNumberIsInvalid() throws Exception {
        final User user = new User();
        user.setUsername("SPRING-123");
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setPhone("1234");

        mockMvc.perform(
                post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFindUserByUsername() throws Exception {
        mockMvc.perform(
                get("/user/{username}", "SPRING-123"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotFindUserWhenUsernameIsInvalid() throws Exception {
        mockMvc.perform(
                get("/user/{username}", "123"))
                .andExpect(status().isBadRequest());
    }
}
