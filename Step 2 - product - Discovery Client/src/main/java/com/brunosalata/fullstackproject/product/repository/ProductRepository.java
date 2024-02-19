package com.brunosalata.fullstackproject.product.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.brunosalata.fullstackproject.product.model.Product;

@Service
public interface ProductRepository extends CrudRepository<Product, String> {
}
