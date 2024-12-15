package org.codiz.onshop.repositories.order;

import org.codiz.onshop.entities.orders.OrderItemStatus;
import org.codiz.onshop.entities.orders.OrderItems;
import org.codiz.onshop.entities.products.SpecificProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersItemsRepository extends JpaRepository<OrderItems,String> {
    Optional<OrderItems> findOrderItemsByOrderItemId(String orderItemId);

    boolean existsByOrderItemId(String specificationId);

    boolean existsOrderItemsBySpecificProductDetails(SpecificProductDetails specificProductDetails);

    OrderItems findOrderItemsBySpecificProductDetails(SpecificProductDetails details);
}
