package com.sveltecommerce.api.productOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sveltecommerce.api.product.ProductResponse;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductOrderResponse implements Serializable {

    private long id;

    private String status;

    List<ProductResponse> productItems;

    private long userId;

    public ProductOrderResponse() {
    }

    public ProductOrderResponse(long id, String status, List<ProductResponse> productItems, long userId) {
        this.id = id;
        this.status = status;
        this.productItems = productItems;
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

    public List<ProductResponse> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductResponse> productItems) {
        this.productItems = productItems;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
