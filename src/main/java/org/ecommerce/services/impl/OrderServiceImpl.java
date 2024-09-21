package org.ecommerce.services.impl;

import org.ecommerce.enums.Error;
import org.ecommerce.enums.OrderStatus;
import org.ecommerce.exceptions.EntityNotFound;
import org.ecommerce.message.broker.MessageQueue;
import org.ecommerce.message.broker.consumers.OrderConsumer;
import org.ecommerce.message.broker.producers.Producer;
import org.ecommerce.models.Order;
import org.ecommerce.repositories.impl.OrderRepositoryImpl;
import org.ecommerce.services.OrderService;
import org.ecommerce.util.database.Operations;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderServiceImpl implements OrderService {

    private final OrderRepositoryImpl orderRepositoryImpl;
    private final MessageQueue<Order> messageQueue;
    private final ExecutorService executorService;

    public OrderServiceImpl(OrderRepositoryImpl orderRepositoryImpl, MessageQueue<Order> messageQueue) {
        this.orderRepositoryImpl = orderRepositoryImpl;
        this.messageQueue = messageQueue;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public Order create(Order order) {
        orderRepositoryImpl.save(order);
        produceOrder(order);
        //orderRepositoryImpl.updateStatus(order, OrderStatus.REQUESTED);
        updateOrderStatus(order.getId(), OrderStatus.REQUESTED);
        return order;
    }

    @Override
    public void createOrderTransaction(Order order) {
        orderRepositoryImpl.save(order);

    }

    @Override
    public Order update(Long id, Order entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Long id) {
        orderRepositoryImpl.deleteById(id);
    }

    @Override
    public List<Order> findAll() {
        return orderRepositoryImpl.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepositoryImpl.findById(id)
                .orElseThrow(() -> new EntityNotFound(Error.ENTITY_NOT_FOUND.getDescription()));
    }

    // Consume message broker messages
    public void startOrderProcessing() {
        executorService.submit(new OrderConsumer(messageQueue));
    }

    public void stopOrderProcessing() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void produceOrder(Order order) {
        Producer<Order> producer = new Producer<>(messageQueue, order);
        producer.run();
    }

    // Shipping Management connects to this service to update the status
    public void updateOrderStatus(Long id, OrderStatus status) {
        orderRepositoryImpl.findById(id)
                .map(result -> {
                    orderRepositoryImpl.updateStatus(result, status);
                    return result;
                }).orElseThrow(() -> new EntityNotFound("Order not found"));
    }
}
