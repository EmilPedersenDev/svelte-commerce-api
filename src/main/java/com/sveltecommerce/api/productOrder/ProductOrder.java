package com.sveltecommerce.api.productOrder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sveltecommerce.api.orderItem.OrderItem;
import com.sveltecommerce.api.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productOrder")
public class ProductOrder {
    @Id
    @GeneratedValue()
    private long id;

    private String status;

    @OneToMany(mappedBy = "productOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private User user;

    @Column(name="user_id", insertable=true, updatable=true)
    private long userId;

    public ProductOrder() {
    }

    public ProductOrder(String status) {
        this.status = status;
    }

    public ProductOrder(String status, User user) {
        this.status = status;
        this.user = user;
        this.userId = user.getId();
    }

    public ProductOrder(String status, long userId) {
        this.status = status;
        this.userId = userId;
    }

    public ProductOrder(long id, String status, List<OrderItem> orderItems, User user, long userId) {
        this.id = id;
        this.status = status;
        this.orderItems = orderItems;
        this.user = user;
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
    }
}
