package com.sveltecommerce.api.productOrder;

import com.sveltecommerce.api.orderItem.OrderItem;
import com.sveltecommerce.api.orderItem.OrderItemRepository;
import com.sveltecommerce.api.product.Product;
import com.sveltecommerce.api.product.ProductRepository;
import com.sveltecommerce.api.productItem.ProductItem;
import com.sveltecommerce.api.productItem.ProductItemRepository;
import com.sveltecommerce.api.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

       if(pendingProductOrder == null) {
            pendingProductOrder = new ProductOrder(ProductOrderStatus.PENDING.status, productOrderRequest.getUserId());
            repository.save(pendingProductOrder);
        }

        Optional<ProductItem> savedProductItem = productItemRepository.findById(productItemReq.getId());

        // The stock of the productItem needs to be reduced when creating a new order
        int productItemQuantity = getProductItemQuantity(savedProductItem, productItemReq);
        savedProductItem.ifPresent(item -> item.setQuantity(productItemQuantity));

        OrderItem newOrderItem = createOrderItem(productItemReq, pendingProductOrder);
        if(newOrderItem != null) {
            pendingProductOrder.addOrderItem(newOrderItem);
        }
        updateProductItem(savedProductItem);

        return pendingProductOrder;
    }

    private OrderItem createOrderItem(ProductItem productItemReq, ProductOrder productOrderReq) throws RuntimeException {
        List<OrderItem> savedOrderItems = orderItemRepository.findByProductItemId(productItemReq.getId());
        Optional<OrderItem> savedOrderItem = savedOrderItems.stream().filter(item -> item.getProductOrder().getId() == productOrderReq.getId()).findFirst();

        // We only want to update the quantity when the savedOrderItem already exists
        OrderItem newOrderItem = null;

        Optional<Product> product = productRepository.findById(productItemReq.getProductId());
        BigDecimal productPrice = null;

        if(product.isPresent()) {
            productPrice = product.get().getPrice();
        } else {
            throw new RuntimeException("Product was not found");
        }

        if(savedOrderItem.isPresent()) {
            savedOrderItem.get().setQuantity(savedOrderItem.get().getQuantity() + productItemReq.getQuantity());
            BigDecimal orderItemPrice = productPrice.multiply(new BigDecimal(productItemReq.getQuantity()));
            savedOrderItem.get().addPrice(orderItemPrice);
            orderItemRepository.save(savedOrderItem.get());
        } else {
            OrderItem orderItem = new OrderItem(productItemReq.getQuantity(), productItemReq, productOrderReq, productPrice.multiply(new BigDecimal(productItemReq.getQuantity())));
            newOrderItem = orderItemRepository.save(orderItem);
        }

        return newOrderItem;
    }

    private void updateProductItem(Optional<ProductItem> savedProductItem) {
        savedProductItem.ifPresent(newProductItem -> productItemRepository.save(savedProductItem.get()));
    }


    private int getProductItemQuantity(Optional<ProductItem> savedProductItem, ProductItem productItemReq) throws RuntimeException {
        if(savedProductItem.isPresent() && savedProductItem.get().getQuantity() > 0) {
            int quantity = savedProductItem.get().getQuantity() - productItemReq.getQuantity();
            if(quantity < 0) {
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
}
