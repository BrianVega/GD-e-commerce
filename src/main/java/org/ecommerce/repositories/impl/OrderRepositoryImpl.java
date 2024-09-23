package org.ecommerce.repositories.impl;

import org.ecommerce.enums.OrderStatus;
import org.ecommerce.models.Order;
import org.ecommerce.models.Product;
import org.ecommerce.repositories.OrderRepository;
import org.ecommerce.util.database.Operations;

import java.sql.SQLException;
import java.util.List;

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
    public void saveProductOrderRelation(Order order) {
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

        System.exit(1); // Simulating that the system fails, so the other part of the transaction won't execute
        save_products_orders(order.getProducts());
    }

    private void save_products_orders(List<Product> products) {
        try {
            Long lastValueId = getLastRowInsertedID();
            String query = "INSERT INTO products_orders (fk_product_id, fk_order_id) VALUES (?, ?);";
            for (Product product : products) {
                operationsDB.execute(query, product.getId(), lastValueId);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private Long getLastRowInsertedID() throws SQLException {
        return operationsDB.findOne("SELECT LASTVAL();", // FIXME: Probably this is not safe when working with concurrent orders placement
                resultSet -> {
                    try {
                        resultSet.next();
                        return Order.builder()
                                .id(resultSet.getLong(1))
                                .build();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).getId();
    }
}
