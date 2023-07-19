package com.sveltecommerce.api.productOrder;

public enum ProductOrderStatus {
    // pending, completed, rejected
    PENDING("pending"),
    COMPLETED("completed"),
    REJECTED("rejected");

    public final String status;

    ProductOrderStatus(String status) {
        this.status = status;
    }
}
