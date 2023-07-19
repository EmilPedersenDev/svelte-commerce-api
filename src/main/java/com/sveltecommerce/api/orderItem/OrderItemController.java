package com.sveltecommerce.api.orderItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.Produces;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("${apiVersion}" + "orderItems")
public class OrderItemController {
    @Autowired
    private OrderItemRepository repository;

    public OrderItemController(OrderItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Produces(APPLICATION_JSON)
    public ResponseEntity<List<OrderItem>> getOrderItems() {
        try {
            List<OrderItem> orderItems = repository.findAll();

            return new ResponseEntity<>(orderItems, HttpStatus.OK);

        } catch(Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }



    }


}
