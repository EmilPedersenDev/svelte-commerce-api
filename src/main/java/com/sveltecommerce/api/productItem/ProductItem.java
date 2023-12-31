package com.sveltecommerce.api.productItem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sveltecommerce.api.orderItem.OrderItem;
import com.sveltecommerce.api.product.Product;
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

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "productItem")
public class ProductItem {

  @Id
  @GeneratedValue()
  private long id;

  private int quantity;
  private String size;
  private String color;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties({"orderItem", "productItems", "hibernateLazyInitializer", "handler"})
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(name = "product_id", insertable = false, updatable = false)
  private long productId;

  @OneToMany(mappedBy = "productItem", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> orderItem;

  public ProductItem() {
  }

  public ProductItem(long id, int quantity, String size, String color, Product product, List<OrderItem> orderItem) {
    this.id = id;
    this.quantity = quantity;
    this.size = size;
    this.color = color;
    this.product = product;
    this.orderItem = orderItem;
  }


  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public long getProductId() {
    return productId;
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

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public List<OrderItem> getOrderItem() {
    return orderItem;
  }

  public void setOrderItem(List<OrderItem> orderItem) {
    this.orderItem = orderItem;
  }
}
