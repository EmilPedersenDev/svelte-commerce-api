package com.sveltecommerce.api.productcategory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sveltecommerce.api.product.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Table(name = "productCategory")
public class ProductCategory {
  @Id
  @GeneratedValue()
  private long id;
  private String name;

  private String link;
  private String sex;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_category_id")
  private List<Product> products;

  public ProductCategory() {
  }

  public ProductCategory(long id, String name, String link, String sex, List<Product> products) {
    this.id = id;
    this.name = name;
    this.link = link;
    this.sex = sex;
    this.products = products;
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

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }
}
