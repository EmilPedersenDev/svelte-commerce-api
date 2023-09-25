package com.sveltecommerce.api.product;

import com.sveltecommerce.api.productItem.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${apiVersion}" + "products")
public class ProductController {

  @Autowired
  private ProductRepository repository;

  public ProductController(ProductRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> products = repository.findAll();

    return new ResponseEntity<>(products, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProduct(@PathVariable long id) {
    try {
      if (id < 0) {
        throw new RuntimeException("No valid id");
      }

      Optional<Product> product = repository.findById(id);

      if (product.isPresent()) {
        List<ProductItem> productItems = product.get().getProductItems().stream().filter(item -> item.getQuantity() > 0).toList();
        product.get().setProductItems(productItems);
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
      } else {
        throw new RuntimeException("No Product was found");
      }
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No product found");
    }
  }
}


