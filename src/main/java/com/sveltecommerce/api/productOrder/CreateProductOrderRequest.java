package com.sveltecommerce.api.productOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sveltecommerce.api.productItem.ProductItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateProductOrderRequest implements Serializable {
    @JsonProperty
    public ProductItem productItem;
    @JsonProperty
    public ProductOrder currentProductOrder;

    @JsonProperty
    public long userId;


    public CreateProductOrderRequest() {
    }

    public CreateProductOrderRequest(ProductItem productItem, ProductOrder currentProductOrder, long userId) {
        this.productItem = productItem;
        this.currentProductOrder = currentProductOrder;
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    public ProductOrder getCurrentProductOrder() {
        return currentProductOrder;
    }

    public void setCurrentProductOrder(ProductOrder currentProductOrder) {
        this.currentProductOrder = currentProductOrder;
    }
}
