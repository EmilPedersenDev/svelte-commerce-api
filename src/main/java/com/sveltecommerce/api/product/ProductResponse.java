package com.sveltecommerce.api.product;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductResponse implements Serializable {
    private long id;
    private String name;
    private BigDecimal price;
    private String sex;
    private int quantity;
    private String size;
    private String color;

    public ProductResponse() {
    }

    public ProductResponse(long id, String name, BigDecimal price, String sex, int quantity, String size, String color) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sex = sex;
        this.quantity = quantity;
        this.size = size;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
