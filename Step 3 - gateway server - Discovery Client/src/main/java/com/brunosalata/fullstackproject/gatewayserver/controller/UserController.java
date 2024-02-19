package com.brunosalata.fullstackproject.gatewayserver.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.brunosalata.fullstackproject.gatewayserver.model.User;
import com.brunosalata.fullstackproject.gatewayserver.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository){
        super();
        this.userRepository = userRepository;
    }

    @GetMapping
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody UserForm form){
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setLogin(form.getLogin());
        user.setPassword(form.getPassword());
        user = userRepository.save(user);
        return ResponseEntity.ok(user.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable String id){
        Optional<User> op = userRepository.findById(id);
        return ResponseEntity.of(op);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@Valid @RequestBody UserForm form, @PathVariable String id) {
        Optional<User> op = userRepository.findById(id);
        if(op.isEmpty()) {
            return ResponseEntity.of(op);
        }
        User user = op.get();
        user.setLogin(form.getLogin());
        user.setPassword(form.getPassword());
        user = userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        Optional<User> op = userRepository.findById(id);
        if(op.isPresent()){
            userRepository.deleteById(id);
        }
        // return ResponseEntity.of(op);
    }
}