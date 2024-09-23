package org.ecommerce.repositories.impl;

import org.ecommerce.enums.OrderStatus;
import org.ecommerce.models.*;
import org.ecommerce.util.database.Operations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryImplTest {
        @InjectMocks
        private OrderRepositoryImpl repository;

        @Mock
        private Product product;

        @Mock
        private Order order;

        @Mock
        private Operations<Order> operationsDB;

        @Mock
        private ShippingInformation shippingInformation;

        @Mock
        private BillingInformation billingInformation;

        @Mock
        private PaymentDetails paymentDetails;

        @Mock
        private Price shippingCost;

        @BeforeEach
        void setUp() {
            when(order.getShippingInformation()).thenReturn(shippingInformation);
            when(order.getBillingInformation()).thenReturn(billingInformation);
            when(order.getPaymentDetails()).thenReturn(paymentDetails);
            when(shippingInformation.getShippingCost()).thenReturn(shippingCost);
            when(order.getCustomerId()).thenReturn(1L);
            when(order.getOrderDate()).thenReturn(Date.valueOf("2024-09-22"));
            when(order.getStatus()).thenReturn(OrderStatus.PLACED);
            when(shippingInformation.getId()).thenReturn(1L);
            when(billingInformation.getId()).thenReturn(1L);
            when(paymentDetails.getId()).thenReturn(1L);
            when(shippingCost.getAmount()).thenReturn(new BigDecimal("19.99"));
            when(billingInformation.getAmount()).thenReturn(new Price(Currency.getInstance("USD"), new BigDecimal("100.00")));
        }

        @Test
        void saveProductOrderRelation_doesNotThrow() {
            assertDoesNotThrow(() -> repository.saveProductOrderRelation(product, order));

            try {
                verify(operationsDB).execute(
                        eq("INSERT INTO orders (fk_shipping_information_id, fk_billing_information_id, fk_payment_details_id, fk_customer_id, date, status, total_usd) VALUES (?, ?, ?, ?, ?, ?, ?);"),
                        eq(1L),
                        eq(1L),
                        eq(1L),
                        eq(1L),
                        eq(Date.valueOf("2024-09-22")),
                        eq(OrderStatus.PLACED.name()),
                        eq(new BigDecimal("119.99"))
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
