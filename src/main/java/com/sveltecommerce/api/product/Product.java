package com.sveltecommerce.api.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sveltecommerce.api.company.Company;
import com.sveltecommerce.api.productItem.ProductItem;
import com.sveltecommerce.api.productcategory.ProductCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
  @Id
  @GeneratedValue()
  private long id;

  private String name;
  private BigDecimal price;
  private String sex;
  private String image;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Company company;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private ProductCategory productCategory;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private List<ProductItem> productItems;

  public Product() {
  }

  public Product(long id, String name, BigDecimal price, String sex, String image, List<ProductItem> productItems, ProductCategory productCategory) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.sex = sex;
    this.image = image;
    this.productItems = productItems;
    this.productCategory = productCategory;
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

  public List<ProductItem> getProductItems() {
    return productItems;
  }

  public void setProductItems(List<ProductItem> productItems) {
    this.productItems = productItems;
  }

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public ProductCategory getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(ProductCategory productCategory) {
    this.productCategory = productCategory;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  @Override
  public String toString() {
    return "Product{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", price=" + price +
      ", sex='" + sex + '\'' +
      ", company=" + company +
      ", productItems=" + productItems +
      '}';
  }
}
