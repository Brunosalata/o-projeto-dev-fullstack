package com.brunosalata.fullstackproject.product.controller;

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

import com.brunosalata.fullstackproject.product.model.Product;
import com.brunosalata.fullstackproject.product.repository.ProductRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository){
        super();
        this.productRepository = productRepository;
    }

    @GetMapping
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody ProductForm form){
        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product = productRepository.save(product);
        return ResponseEntity.ok(product.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable String id){
        Optional<Product> op = productRepository.findById(id);
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
    public ResponseEntity<Product> update(@Valid @RequestBody ProductForm form, @PathVariable String id) {
        Optional<Product> op = productRepository.findById(id);
        if(op.isEmpty()) {
            return ResponseEntity.of(op);
        }
        Product product = op.get();
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product = productRepository.save(product);

        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        Optional<Product> op = productRepository.findById(id);
        if(op.isPresent()){
            productRepository.deleteById(id);
        }
        // return ResponseEntity.of(op);
    }

}
