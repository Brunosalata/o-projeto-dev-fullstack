package com.brunosalata.fullstackproject.product.controller;

import jakarta.validation.constraints.NotBlank;

public class ProductForm {
    
    @NotBlank
    private String name;
    private String description;

    public ProductForm(){
        super();
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }    
}
