package com.sveltecommerce.api.productcategory;

import com.sveltecommerce.api.product.Product;
import com.sveltecommerce.api.product.ProductRepository;
import com.sveltecommerce.api.productItem.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import spark.utils.StringUtils;

import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${apiVersion}" + "productCategories")
public class ProductCategoryController {
  @Autowired
  public ProductCategoryRepository repository;

  @Autowired
  public ProductRepository productRepository;

  public ProductCategoryController(ProductCategoryRepository repository, ProductRepository productRepository) {
    this.repository = repository;
    this.productRepository = productRepository;
  }

  @GetMapping
  public ResponseEntity<List<ProductCategory>> getProductCategories() {
    try {
      List<ProductCategory> productCategories = repository.findAll();
      return new ResponseEntity<>(productCategories, HttpStatus.OK);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not fetch product categories");
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<List<Product>> getProductCategory(@PathVariable String id, @RequestParam String sex) {
    try {
      if (StringUtils.isBlank(id)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No valid Id was provided");
      }

      List<Product> productsByCategoryAndSex = productRepository.findByProductCategoryIdAndSex(Long.valueOf(id), sex);

      for (Product product : productsByCategoryAndSex) {
        List<ProductItem> productItems = product.getProductItems().stream().filter(item -> item.getQuantity() > 0).toList();
        product.setProductItems(productItems);
      }

      return new ResponseEntity<>(productsByCategoryAndSex, HttpStatus.OK);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not fetch product category");
    }
  }
}
