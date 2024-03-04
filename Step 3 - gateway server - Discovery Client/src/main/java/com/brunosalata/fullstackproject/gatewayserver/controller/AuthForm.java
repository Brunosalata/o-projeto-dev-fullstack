package com.brunosalata.fullstackproject.gatewayserver.controller;

import jakarta.validation.constraints.NotBlank;

public class AuthForm {
    
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    
    public AuthForm() {
        super();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}
