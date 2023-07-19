package com.sveltecommerce.api.productItem;

import com.sveltecommerce.api.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping( "${apiVersion}" + "productItems")
public class ProductItemController {

    @Autowired
    private ProductItemRepository repository;

    public ProductItemController(ProductItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<ProductItem>> getAllProductItems() {

        try {
            List<ProductItem> productItems = repository.findAll();
            return new ResponseEntity<>(productItems, HttpStatus.OK);
        } catch(Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }

    }
}
