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
    @Min(value = 0, message = "orderItemId must be a positive integer")
    private long orderItemId;

    public OrderItemQuantity() {
    }

    public OrderItemQuantity(String type, long orderItemId) {
        this.type = type;
        this.orderItemId = orderItemId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(long orderItemId) {
        this.orderItemId = orderItemId;
    }
}
