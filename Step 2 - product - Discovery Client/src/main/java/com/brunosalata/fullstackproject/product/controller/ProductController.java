package com.brunosalata.fullstackproject.product.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping
    public ImmutableMap<Long, String> findAll() {
        return ImmutableMap.of(
                1L, "Produto 1",
                2L, "Produto 2",
                3L, "Produto 3",
                4L, "Produto 4");
    }

}
