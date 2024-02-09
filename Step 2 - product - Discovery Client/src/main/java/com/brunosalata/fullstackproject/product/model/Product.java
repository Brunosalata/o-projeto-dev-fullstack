package com.brunosalata.fullstackproject.product.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "product")
public class Product implements Serializable {
    
    @Id
    @Column(length = 32, nullable = false)
    private String id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;

    public Product() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
