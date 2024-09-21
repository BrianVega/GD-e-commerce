package org.ecommerce.repositories.impl;

import org.ecommerce.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryImplTest {

    @InjectMocks
    private OrderRepositoryImpl repository;

    @Mock
    private Product product;

    @Mock
    private Order order;



    @BeforeEach
    void setUp() {
    }

    @Test
    void saveProductOrderRelation() {
        when(order.getShippingInformation())
                .thenReturn(new ShippingInformation());
        when(order.getShippingInformation().getShippingCost())
                .thenReturn(new Price(Currency.getInstance("USD")
                        , new BigDecimal("19.99")));
        when(order.getBillingInformation())
                .thenReturn(new BillingInformation());
        when(order.getPaymentDetails())
                .thenReturn(new PaymentDetails());
        repository.saveProductOrderRelation(
                product, order
        );


    }
}