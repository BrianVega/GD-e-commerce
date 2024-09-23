package org.ecommerce.repositories;

import org.ecommerce.enums.OrderStatus;
import org.ecommerce.models.Order;
import org.ecommerce.models.Product;

public interface OrderRepository {
    void updateStatus(Order order, OrderStatus status);
    void saveProductOrderRelation(Order order); // Should this be performed in here? or in the db?
}
