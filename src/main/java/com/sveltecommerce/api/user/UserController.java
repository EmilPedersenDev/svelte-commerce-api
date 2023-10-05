package com.sveltecommerce.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@RequestMapping("${apiVersion}" + "users")
public class UserController {

  @Autowired
  private UserRepository repository;

  public UserController(UserRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllAuthors() {
    return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  @Produces(APPLICATION_JSON)
  public ResponseEntity getUser(@PathVariable long id) {
    try {
      Optional<User> user = repository.findById(id);

      if (user.isPresent()) {
        return new ResponseEntity<>(user, HttpStatus.OK);
      } else {
        throw new RuntimeException("No user was found");
      }
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error");
    }
  }
}
