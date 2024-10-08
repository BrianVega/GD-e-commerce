package org.ecommerce.services.impl;

import org.ecommerce.exceptions.EntityNotFound;
import org.ecommerce.models.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import({OrderServiceImpl.class})
class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    @MockBean
    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(0L);
    }

    @Test
    void create() {
        Order order = new Order();
        orderService.create(order);

        assertNotNull(order.getId());
    }

    @Test
    void delete() {
        orderService.create(order);

        orderService.delete(order.getId());

        assertThrows(EntityNotFound.class,
                () -> orderService.findById(order.getId()));
    }


//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findById() {
        assertDoesNotThrow(() -> orderService.findById(order.getId()));
    }
}