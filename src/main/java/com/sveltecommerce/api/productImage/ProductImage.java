package com.sveltecommerce.api.productImage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sveltecommerce.api.product.Product;
import com.sveltecommerce.api.productItem.ProductItem;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "productImage")
public class ProductImage {

    @Id
    @GeneratedValue()
    private long id;

    private String source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private ProductItem productItem;

    public ProductImage() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }
}
