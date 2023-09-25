package com.sveltecommerce.api.orderItem;

import com.sveltecommerce.api.product.ProductRepository;
import com.sveltecommerce.api.productItem.ProductItem;
import com.sveltecommerce.api.productItem.ProductItemRepository;
import com.sveltecommerce.api.productOrder.ProductOrder;
import com.sveltecommerce.api.productOrder.ProductOrderRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@RequestMapping("${apiVersion}" + "orderItems")
public class OrderItemController {
  private final OrderItemRepository repository;
  private final ProductOrderRepository productOrderRepository;
  private final ProductRepository productRepository;
  private final ProductItemRepository productItemRepository;

  @Autowired
  public OrderItemController(OrderItemRepository repository, ProductRepository productRepository, ProductOrderRepository productOrderRepository, ProductItemRepository productItemRepository) {
    this.repository = repository;
    this.productRepository = productRepository;
    this.productOrderRepository = productOrderRepository;
    this.productItemRepository = productItemRepository;
  }

  @GetMapping
  @Produces(APPLICATION_JSON)
  public ResponseEntity<List<OrderItem>> getOrderItems() {
    try {
      List<OrderItem> orderItems = repository.findAll();

      return new ResponseEntity<>(orderItems, HttpStatus.OK);

    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  @Produces(APPLICATION_JSON)
  public ResponseEntity deleteOrderItem(@PathVariable long id) {
    try {
      Optional<OrderItem> orderItem = repository.findById(id);

      if (orderItem.isEmpty()) {
        throw new RuntimeException("No order was found");
      }

      repository.deleteById(id);

      ProductOrder productOrder = productOrderRepository.getReferenceById(orderItem.get().getProductOrder().getId());

      if (productOrder.getOrderItems().isEmpty()) {
        productOrderRepository.deleteById(productOrder.getId());
      }


      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not delete orderItem");
    }
  }

  @PatchMapping("/{id}")
  @Produces(APPLICATION_JSON)
  public ResponseEntity<OrderItem> updateOrderItemQuantity(@PathVariable long id, @Valid @RequestBody OrderItemQuantity orderItemQuantityReq) {
    try {
      Optional<OrderItem> orderItem = repository.findById(id);

      if (orderItem.isEmpty()) {
        throw new RuntimeException("No order was found");
      }

      BigDecimal productPrice = orderItem.get().getProductItem().getProduct().getPrice();

      if (orderItemQuantityReq.getType().equals(ChangeQuantityTypes.INCREMENT.type)) {
        // Check quantity before incrementing
        ProductItem productItem = productItemRepository.getReferenceById(orderItem.get().getProductItem().getId());

        if (productItem.getQuantity() >= orderItem.get().getQuantity() + 1) {
          orderItem.get().incrementQuantity(productPrice);
        } else {
          throw new Exception("Your item was not added. There is not enough stock for this item");
        }

      } else {
        orderItem.get().decrementQuantity(productPrice);
      }

      repository.save(orderItem.get());

      ProductOrder productOrder = productOrderRepository.getReferenceById(orderItem.get().getProductOrder().getId());

      if (productOrder.getOrderItems().isEmpty()) {
        productOrderRepository.deleteById(productOrder.getId());
      }

      return new ResponseEntity<>(orderItem.get(), HttpStatus.OK);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }
}
