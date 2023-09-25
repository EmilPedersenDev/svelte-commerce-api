package com.sveltecommerce.api.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Optional<Product> findBySex(String sex);

  @Query("SELECT p FROM Product p WHERE (p.productCategory.id=:productCategoryId AND p.sex=:sex) OR (p.productCategory.id=:productCategoryId AND p.sex='unisex')")
  List<Product> findByProductCategoryIdAndSex(@Param("productCategoryId") long productCategoryId, @Param("sex") String sex);
}


