package com.bitlegion.server.accounts;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/accounts") // This means URLs start with /demo (after Application path)
@CrossOrigin
public class AccountsController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private AccountRepository accountRepository;

    @PostMapping(path = "/register") // Map ONLY POST Requests
    public ResponseEntity<ResponseMessage> addNewUser(@RequestParam String username, @RequestParam String email,
            @RequestParam String password, @RequestParam Date dateOfBirth, @RequestParam String firstName,
            @RequestParam String lastName) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        String message = "";

        if (username.length() == 0) {
            message = "You provided an invalid name";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        if (password.length() == 0) {
            message = "You provided an invalid password";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        if (username.length() == 0) {
            message = "You provided an invalid name";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        if (lastName.length() == 0) {
            message = "You provided an invalid last name";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        if (firstName.length() == 0) {
            message = "You provided an invalid first name";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        if (dateOfBirth.after(new Date())) {
            message = "Invalid date of birth detected";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        try {
            Account newUser = new Account();
            if (accountRepository.findByEmail(email).isPresent()) {
                message = "A user with email " + email + " already exists";
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage(message));
            }
            if (accountRepository.findByUsername(username).isPresent()) {
                message = "A user with name " + username + " already exists";
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage(message));
            }
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setDateOfBirth(dateOfBirth);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            accountRepository.save(newUser);
            message = "The user was created successfully!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Account> loginUser(@RequestParam String name, @RequestParam String password) {
        Optional<Account> maybeUser = accountRepository.findByUsername(name);
        if (maybeUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Account user = maybeUser.get();
        if (user.verifyPassword(password)) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/update/{userID}")
    public ResponseEntity<ResponseMessage> updateUser(@RequestParam(required = false) String password,
            @RequestParam(required = false) String bio, @PathVariable Integer userID) {
        String message = "";

        Optional<Account> maybeUser = accountRepository.findById(userID);
        if (maybeUser.isEmpty()) {
            message = "No such user found";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
        Account user = maybeUser.get();

        if (password != null) {
            user.setPassword(password);
        }
        if (bio != null) {
            user.setBio(bio);
        }
        accountRepository.save(user);
        message = "The user was updated successfully";
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }

    @GetMapping(path = "/{userID}")
    public ResponseEntity<Account> getUserDetails(@PathVariable Integer userID) {
        Optional<Account> maybeUser = accountRepository.findById(userID);
        if (maybeUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(maybeUser.get());
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Account> getAllUsers() {
        return accountRepository.findAll();
    }
}
