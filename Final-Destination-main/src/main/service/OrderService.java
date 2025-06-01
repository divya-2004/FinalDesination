package com.examly.service;

import java.util.*;
import com.examly.entity.Order;
import com.examly.entity.OrderItem;
public interface OrderService {
    boolean createOrder(Order order, List<OrderItem> orderedItem);
    List<Order> getOrdersByCustomer(int customerId);
    Order getOrderById(int orderId);
}
