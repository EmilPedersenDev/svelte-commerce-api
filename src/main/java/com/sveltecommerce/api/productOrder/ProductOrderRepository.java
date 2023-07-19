package com.sveltecommerce.api.productOrder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    List<ProductOrder> findByUserIdAndStatus(long userId, String status);
}


