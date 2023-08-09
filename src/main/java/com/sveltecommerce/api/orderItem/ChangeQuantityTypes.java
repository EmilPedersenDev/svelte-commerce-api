package com.sveltecommerce.api.orderItem;

public enum ChangeQuantityTypes {
    INCREMENT("increment"),
    DECREMENT("decrement");
    public final String type;

    ChangeQuantityTypes(String type) {
        this.type = type;
    }
}
