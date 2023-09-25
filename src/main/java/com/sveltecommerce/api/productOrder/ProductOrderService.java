package com.sveltecommerce.api.productOrder;

import com.stripe.exception.StripeException;
import com.sveltecommerce.api.orderItem.OrderItem;
import com.sveltecommerce.api.orderItem.OrderItemRepository;
import com.sveltecommerce.api.product.Product;
import com.sveltecommerce.api.product.ProductRepository;
import com.sveltecommerce.api.productItem.ProductItem;
import com.sveltecommerce.api.productItem.ProductItemRepository;
import com.sveltecommerce.api.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static spark.Spark.port;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Component
public class ProductOrderService {

  private final ProductOrderRepository repository;

  private final ProductItemRepository productItemRepository;

  private final OrderItemRepository orderItemRepository;

  private final ProductRepository productRepository;

  @Autowired
  public ProductOrderService(ProductOrderRepository repository, ProductItemRepository productItemRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
    this.repository = repository;
    this.productItemRepository = productItemRepository;
    this.orderItemRepository = orderItemRepository;
    this.productRepository = productRepository;
  }

  public ProductOrder createOrder(CreateProductOrderRequest productOrderRequest) throws RuntimeException {
    ProductItem productItemReq = productOrderRequest.getProductItem();

    // Temporary user for testing purposes
    User testUser = new User(productOrderRequest.getUserId(), "Emil Pedersen");
    ProductOrder pendingProductOrder = getPendingProductOrder(testUser.getId());

    if (pendingProductOrder == null) {
      pendingProductOrder = new ProductOrder(ProductOrderStatus.PENDING.status, productOrderRequest.getUserId());
      repository.save(pendingProductOrder);
    }

    Optional<ProductItem> savedProductItem = productItemRepository.findById(productItemReq.getId());

    OrderItem newOrderItem = createOrderItem(productItemReq, pendingProductOrder);
    orderItemRepository.save(newOrderItem);

    return pendingProductOrder;
  }

  private OrderItem createOrderItem(ProductItem productItemReq, ProductOrder productOrderReq) throws RuntimeException {
    List<OrderItem> savedOrderItems = orderItemRepository.findByProductItemId(productItemReq.getId());
    Optional<OrderItem> savedOrderItem = savedOrderItems.stream().filter(item -> item.getProductOrder().getId() == productOrderReq.getId()).findFirst();

    // We only want to update the quantity when the savedOrderItem already exists


    Optional<Product> product = productRepository.findById(productItemReq.getProductId());
    BigDecimal productPrice = null;

    if (product.isPresent()) {
      productPrice = product.get().getPrice();
    } else {
      throw new RuntimeException("Product was not found");
    }

    if (savedOrderItem.isPresent()) {
      savedOrderItem.get().setQuantity(savedOrderItem.get().getQuantity() + productItemReq.getQuantity());
      BigDecimal orderItemPrice = productPrice.multiply(new BigDecimal(productItemReq.getQuantity()));
      savedOrderItem.get().addPrice(orderItemPrice);
      orderItemRepository.save(savedOrderItem.get());
      return savedOrderItem.get();
    } else {
      OrderItem newOrderItem = null;
      OrderItem orderItem = new OrderItem(productItemReq.getQuantity(), productItemReq, productOrderReq, productPrice.multiply(new BigDecimal(productItemReq.getQuantity())));
      productOrderReq.addOrderItem(orderItem);
      newOrderItem = orderItemRepository.save(orderItem);
      return newOrderItem;
    }
  }

  private int getProductItemQuantity(Optional<ProductItem> savedProductItem, ProductItem productItemReq) throws RuntimeException {
    if (savedProductItem.isPresent() && savedProductItem.get().getQuantity() > 0) {
      int quantity = savedProductItem.get().getQuantity() - productItemReq.getQuantity();
      if (quantity < 0) {
        throw new RuntimeException("You can't buy that amount of this product, products left are " + savedProductItem.get().getQuantity());
      }
      return quantity;
    } else {
      throw new RuntimeException("ProductItem is out of stock");
    }
  }

  private ProductOrder getPendingProductOrder(long userId) {
    List<ProductOrder> pendingProductOrders = repository.findByUserIdAndStatus(userId, ProductOrderStatus.PENDING.status);
    Optional<ProductOrder> pendingProductOrder = pendingProductOrders.stream().filter(order -> order.getStatus().equals(ProductOrderStatus.PENDING.status)).findFirst();
    return pendingProductOrder.orElse(null);
  }

  public String checkoutStripe(ProductOrder productOrder) throws StripeException {
    // Check quantity here to be able to cancel the order before paying
    port(4242);
    Stripe.apiKey = "sk_test_51NdZTbH0MQfguTsOcaBPKAZpsphiYThFcdpB0RReo599Wbr3pamdOJcZ0qVsoTrfx4rMyoFIVCUIX1gRBDU3J3gj00bGAYVx2O";

    SessionCreateParams.Builder stripeBuilder =
      SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl("http://localhost:5173/order-success")
        .setCancelUrl("http://localhost:5173/order-fail");

    for (OrderItem productOrderItem : productOrder.getOrderItems()) {
      stripeBuilder.addLineItem(
        SessionCreateParams.LineItem.builder()
          .setQuantity((long) productOrderItem.getQuantity())
          .setPriceData(
            SessionCreateParams.LineItem.PriceData.builder()
              .setCurrency("usd")
              .setUnitAmount(productOrderItem.getProductItem().getProduct().getPrice().multiply(new BigDecimal(100)).longValue())
              .setProductData(
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                  .setName(productOrderItem.getProductItem().getProduct().getName())
                  .build())
              .build())
          .build());
    }

    SessionCreateParams stripeParams = stripeBuilder.build();

    Session session = Session.create(stripeParams);

    return session.getUrl();
  }

  public ProductOrder completeOrder(ProductOrder productOrder) throws Exception {
    ArrayList<ProductItem> productItemsToUpdate = new ArrayList<>();

    for (OrderItem orderItem : productOrder.getOrderItems()) {
      int quantity = orderItem.getProductItem().getQuantity() - orderItem.getQuantity();

      if (quantity > 0) {
        Optional<ProductItem> savedProductItem = productItemRepository.findById(orderItem.getProductItem().getId());
        if (savedProductItem.isPresent()) {
          savedProductItem.get().setQuantity(quantity);
          productItemsToUpdate.add(savedProductItem.get());
        } else {
          throw new IOException("ProductItem in order was not found");
        }
      } else {
        throw new Exception("There is not enough stock for the product: " + orderItem.getProductItem().getProduct().getName());
      }
    }

    productItemRepository.saveAll(productItemsToUpdate);

    Optional<ProductOrder> savedProductOrder = repository.findById(productOrder.getId());

    if (savedProductOrder.isPresent()) {
      savedProductOrder.get().setStatus(ProductOrderStatus.COMPLETED.status);
      repository.save(savedProductOrder.get());
      return savedProductOrder.get();
    } else {
      throw new IOException("order was not found");
    }
  }
}
