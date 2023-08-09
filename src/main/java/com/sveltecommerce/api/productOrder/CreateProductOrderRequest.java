package com.sveltecommerce.api.productOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sveltecommerce.api.productItem.ProductItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateProductOrderRequest implements Serializable {
    @JsonProperty
    @NotNull
    public ProductItem productItem;

    @JsonProperty
    @Min(value = 0, message = "userId must be a positive integer")
    public long userId;


    public CreateProductOrderRequest() {
    }

    public CreateProductOrderRequest(ProductItem productItem, long userId) {
        this.productItem = productItem;
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
}
