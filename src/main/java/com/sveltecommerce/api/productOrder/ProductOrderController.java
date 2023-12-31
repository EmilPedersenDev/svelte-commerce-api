package com.sveltecommerce.api.productOrder;

import com.sveltecommerce.api.orderItem.OrderItemRepository;
import com.sveltecommerce.api.productItem.ProductItemRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@RequestMapping("${apiVersion}" + "productOrders")
public class ProductOrderController {
  private final ProductOrderRepository repository;

  private final OrderItemRepository orderItemRepository;

  private final ProductItemRepository productItemRepository;

  private final ProductOrderService productOrderService;

  private final Logger logger;

  @Autowired
  public ProductOrderController(ProductOrderRepository repository, OrderItemRepository orderItemRepository, ProductItemRepository productItemRepository, ProductOrderService productOrderService) {
    this.repository = repository;
    this.orderItemRepository = orderItemRepository;
    this.productItemRepository = productItemRepository;
    this.productOrderService = productOrderService;
    this.logger = LoggerFactory.getLogger(ProductOrderController.class);
  }

  @GetMapping
  @Produces(APPLICATION_JSON)
  public ResponseEntity<List<ProductOrder>> getPorductOrders() {
    try {
      List<ProductOrder> productOrders = repository.findAll();
      return new ResponseEntity<>(productOrders, HttpStatus.OK);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }

  @GetMapping("/{id}")
  @Produces(APPLICATION_JSON)
  public ResponseEntity<ProductOrder> getOrder(@PathVariable long id, @NotNull @RequestParam String status) {
    try {
      if (id < 0) {
        throw new RuntimeException("No valid id");
      }
      List<ProductOrder> productOrders = repository.findByUserIdAndStatus(id, status);
      ProductOrder pendingProductOrder = productOrders.stream().filter(order -> order.getStatus().equals(ProductOrderStatus.PENDING.status)).findFirst().orElse(null);
      return new ResponseEntity<>(pendingProductOrder, HttpStatus.OK);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }


  @PostMapping
  @Produces(APPLICATION_JSON)
  public ResponseEntity<ProductOrder> createOrder(@Valid @RequestBody CreateProductOrderRequest productOrderRequest) {
    if (productOrderRequest.getProductItem() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid order request");
    }
    try {
      ProductOrder orderCreatedResponse = productOrderService.createOrder(productOrderRequest);
      return new ResponseEntity<>(orderCreatedResponse, HttpStatus.CREATED);
    } catch (RuntimeException e) {
      logger.error(e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save order");
    }
  }

  @PostMapping("/checkout")
  @Produces(APPLICATION_JSON)
  public ResponseEntity<String> checkout(@Valid @RequestBody ProductOrder productOrder) {
    try {
      String redirectUrl = productOrderService.checkoutStripe(productOrder);
      return new ResponseEntity<>(redirectUrl, HttpStatus.OK);
    } catch (RuntimeException e) {
      logger.error(e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Checkout was not successful");
    }
  }

  @PostMapping("/complete-order")
  @Produces(APPLICATION_JSON)
  public ResponseEntity<Long> completeOrder(@Valid @RequestBody ProductOrder productOrder) {
    try {
      ProductOrder completedOrder = productOrderService.completeOrder(productOrder);
      return new ResponseEntity<>(completedOrder.getId(), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save order");
    }
  }

  @DeleteMapping("/{id}")
  @Produces(APPLICATION_JSON)
  public ResponseEntity<String> deleteOrderById(@PathVariable long id) {
    try {
      repository.deleteById(id);
      return new ResponseEntity<>("Order with id: " + id + " was deleted.", HttpStatus.OK);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not delete order");
    }
  }
}
