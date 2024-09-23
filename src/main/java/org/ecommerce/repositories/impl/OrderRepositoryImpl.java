package org.ecommerce.repositories.impl;

import org.ecommerce.enums.OrderStatus;
import org.ecommerce.models.Order;
import org.ecommerce.models.Product;
import org.ecommerce.repositories.OrderRepository;
import org.ecommerce.util.database.Operations;

public class OrderRepositoryImpl extends CrudOperationsImpl<Order> implements OrderRepository {

    public final Operations<Order> operationsDB;

    public OrderRepositoryImpl(Operations<Order> operationsDB) {
        this.operationsDB = operationsDB;
    }

    @Override
    public void updateStatus(Order order, OrderStatus status) {
        order.setStatus(status);
    }

    @Override
    public void saveProductOrderRelation(Product product, Order order) {
        String query = "INSERT INTO orders " +
                "(fk_shipping_information_id, fk_billing_information_id, fk_payment_details_id, fk_customer_id, date, status, total_usd) VALUES " +
                "(?, ?, ?, ?, ?, ?, ?);";

        try {
            operationsDB.execute(query,
                    order.getShippingInformation().getId(),
                    order.getBillingInformation().getId(),
                    order.getPaymentDetails().getId(),
                    order.getCustomerId(),
                    order.getOrderDate(),
                    order.getStatus().name(),
                    order.getShippingInformation().getShippingCost().getAmount()
                            .add(order.getBillingInformation().getAmount().getAmount())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
