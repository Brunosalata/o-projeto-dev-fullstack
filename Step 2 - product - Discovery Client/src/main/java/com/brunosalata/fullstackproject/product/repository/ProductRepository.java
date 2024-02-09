package com.brunosalata.fullstackproject.product.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import com.brunosalata.fullstackproject.product.model.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {
}
