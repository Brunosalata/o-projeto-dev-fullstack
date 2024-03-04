package com.brunosalata.fullstackproject.gatewayserver.controller;

import org.springframework.web.bind.annotation.RestController;

import com.brunosalata.fullstackproject.gatewayserver.repository.UserRepository;
import com.brunosalata.fullstackproject.gatewayserver.service.JwtService;
import com.brunosalata.fullstackproject.gatewayserver.util.CryptoUtil;
import com.brunosalata.fullstackproject.gatewayserver.model.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AuthController {
    
    private UserRepository userRepository;
    private JwtService jwtService;

    public AuthController(UserRepository userRepository, JwtService jwtService) {
        super();
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody AuthForm form) throws Exception {
        
        User user = userRepository.findByLogin(form.getLogin());
        if(user == null){
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }
        if(!user.getPassword().equals(CryptoUtil.encrypt(form.getPassword()))){
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(user.getLogin());
        return new ResponseEntity<String>(token, HttpStatus.OK);
    }
    
}
