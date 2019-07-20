package nl.thanus.customvalidation.controllers;

import nl.thanus.customvalidation.api.UserApi;
import nl.thanus.customvalidation.model.User;
import nl.thanus.customvalidation.validators.Username;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
public class UsersController implements UserApi {
    @Override
    public ResponseEntity<User> createUser(@Valid @RequestBody final User body) {
        return ResponseEntity.ok(body);
    }

    @Override
    public ResponseEntity<User> getUser(@Username @PathVariable final String username) {
        final User user = new User();
        user.setFirstName("Foo");
        user.setLastName("Bar");
        user.setUsername("SPRING-123");
        user.setPhone("123456789");

        return ResponseEntity.ok(user);
    }
}
