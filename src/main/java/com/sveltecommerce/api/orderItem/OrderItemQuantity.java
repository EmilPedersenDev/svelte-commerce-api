package com.sveltecommerce.api.orderItem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemQuantity implements Serializable {
  @JsonProperty
  @NotNull
  private String type;
  @JsonProperty
  private OrderItem orderItem;

  public OrderItemQuantity() {
  }

  public OrderItemQuantity(String type, OrderItem orderItem) {
    this.type = type;
    this.orderItem = orderItem;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public OrderItem getOrderItem() {
    return orderItem;
  }

  public void setOrderItem(OrderItem orderItem) {
    this.orderItem = orderItem;
  }
}
