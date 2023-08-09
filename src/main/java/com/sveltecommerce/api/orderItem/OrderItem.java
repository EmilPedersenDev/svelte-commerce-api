package com.sveltecommerce.api.orderItem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sveltecommerce.api.productItem.ProductItem;
import com.sveltecommerce.api.productOrder.ProductOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "orderItem")
public class OrderItem {
    @Id
    @GeneratedValue()
    private long id;

    private int quantity;

    private BigDecimal price;

    @ManyToOne
    @JsonIgnoreProperties("orderItems")
    @JoinColumn(name = "product_order_id")
    @JsonIgnore
    private ProductOrder productOrder;

    @ManyToOne
    @JsonIgnoreProperties({"orderItem", "productItem", "hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "product_item_id")
    private ProductItem productItem;

    public OrderItem() {
    }

    public OrderItem(int quantity, ProductItem productItem, ProductOrder productOrder, BigDecimal price) {
        this.quantity = quantity;
        this.productItem = productItem;
        this.productOrder = productOrder;
        this.price = price;
    }

    public OrderItem(long id, int quantity, ProductOrder productOrder, ProductItem productItem) {
        this.id = id;
        this.quantity = quantity;
        this.productOrder = productOrder;
        this.productItem = productItem;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void addPrice(BigDecimal price) {
        this.price = this.price.add(price);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void incrementQuantity(BigDecimal productPrice) {
        BigDecimal totalPrice = this.price.add(productPrice);
        setPrice(totalPrice);
        this.quantity++;
    }

    public void decrementQuantity(BigDecimal productPrice) {
        if(this.quantity > 0) {
            BigDecimal totalPrice = this.price.subtract(productPrice);
            setPrice(totalPrice);
            this.quantity--;
        }
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }

    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }


}
