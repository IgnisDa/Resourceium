package com.bitlegion.server.accounts;

import com.bitlegion.server.uploads.ResponseMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<ResponseMessage> addNewUser(@RequestParam String name, @RequestParam String email,
            @RequestParam String password) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        System.out.println(password);
        String message = "";
        try {
            Account newUser = new Account();
            if (accountRepository.findByEmail(email).isPresent()) {
                message = "A user with email " + email + " already exists";
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage(message));
            }
            if (accountRepository.findByName(name).isPresent()) {
                message = "A user with name " + name + " already exists";
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage(message));
            }
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
            accountRepository.save(newUser);
            message = "The user was created successfully!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<ResponseMessage> loginUser(@RequestParam String name, @RequestParam String password) {
        String message = "";
        Account newUser = accountRepository.findByName(name).get();
        if (newUser.verifyPassword(password)) {
            message = "The user was authenticated successfully";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }
        message = "The user was not authenticated successfully";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Account> getAllUsers() {
        // This returns a JSON or XML with the users
        // userRepository.deleteAll();
        return accountRepository.findAll();
    }
}
