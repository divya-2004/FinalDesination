package com.examly.service;

import java.sql.*;
import java.util.*;

import com.examly.entity.Order;
import com.examly.entity.OrderItem;
import com.examly.util.DBConnectionUtil;

public class OrderServiceImpl implements OrderService{
    @Override
    public boolean createOrder(Order order, List<OrderItem> orderedItems){
        String insertOrder = "INSERT INTO `order` (orderId, customerId, restaurantId, orderStatus, totalPrice, deliveryAddress) VALUES (?, ?, ?, ?, ?, ?)";
        String insertOrderItem = "INSERT INTO orderItem (orderId, itemId, quantity) VALUES (?, ?, ?)";
        
        try(Connection conn = DBConnectionUtil.getConnection();
            PreparedStatement orderStmt = conn.prepareStatement(insertOrder);
            PreparedStatement itemStmt = conn.prepareStatement(insertOrderItem)){
                conn.setAutoCommit(false);
                orderStmt.setInt(1, order.getOrderId());
                orderStmt.setInt(2, order.getCustomerId());
                orderStmt.setInt(3, order.getRestaurantId());
                orderStmt.setString(4, order.getOrderStatus());
                orderStmt.setDouble(5, order.getTotalPrice());
                orderStmt.setString(6, order.getDeliveryAddress());
                int orderRows = orderStmt.executeUpdate();

                for(OrderItem item : orderedItems){
                    itemStmt.setInt(1, item.getOrderId());
                    itemStmt.setInt(2, item.getItemId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.addBatch();
                }
                int[] itemRows = itemStmt.executeBatch();
                conn.commit();
                return orderRows > 0 && itemRows.length == orderedItems.size();
            }catch(SQLException e){
                e.printStackTrace();
                return false;
            }
    }
    @Override
    public List<Order> getOrdersByCustomer(int customerId){
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM `order` WHERE customerId = ?";

        try(Connection conn = DBConnectionUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Order order = new Order(
                    rs.getInt("orderId"),
                    rs.getInt("customerId"),
                    rs.getInt("restaurantId"),
                    rs.getString("orderStatus"),
                    rs.getDouble("totalPrice"),
                    rs.getString("deliveryAddress")
                );
                orders.add(order);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Order getOrderById(int orderId){
        String sql = "SELECT * FROM `order` WHERE orderId = ?";

        try(Connection conn = DBConnectionUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Order order = new Order(
                    rs.getInt("orderId"),
                    rs.getInt("customerId"),
                    rs.getInt("restaurantId"),
                    rs.getString("orderStatus"),
                    rs.getDouble("totalPrice"),
                    rs.getString("deliveryAddress")
                );
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
