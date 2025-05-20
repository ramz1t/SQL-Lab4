package se.hkr.dao;

import se.hkr.menu.Menu;
import se.hkr.model.OrderHead;
import se.hkr.model.OrderLine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    Connection connection;

    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    public List<OrderHead> listOrdersForEmployee(int employeeId) throws SQLException {
        List<OrderHead> orders = new ArrayList<>();

        String sql = "SELECT id, order_date, customer_id, employee_id FROM order_head WHERE employee_id = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderHead order = new OrderHead(
                        rs.getLong("id"),
                        rs.getDate("order_date"),
                        rs.getLong("customer_id"),
                        rs.getLong("employee_id")
                );
                orders.add(order);
            }
        }

        return orders;
    }

    public void createOrder(OrderHead order, List<OrderLine> orderLines) throws SQLException {
        String insertOrderSql = "INSERT INTO order_head (order_date, customer_id, employee_id) VALUES (?, ?, ?)";
        String insertOrderLineSql = "INSERT INTO order_line (furniture_id, order_id, quantity) VALUES (?, ?, ?)";

        // Here I create a copy of Connection to only apply setAutoCommit(false) in this function
        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement orderLineStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = this.connection;
            conn.setAutoCommit(false);

            orderStmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setDate(1, new java.sql.Date(order.order_date().getTime()));
            orderStmt.setLong(2, order.customer_id());
            orderStmt.setLong(3, order.employee_id());
            orderStmt.executeUpdate();

            generatedKeys = orderStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
            long generatedOrderId = generatedKeys.getLong(1);

            orderLineStmt = conn.prepareStatement(insertOrderLineSql);
            for (OrderLine line : orderLines) {
                orderLineStmt.setLong(1, line.furniture_id());
                orderLineStmt.setLong(2, generatedOrderId);
                orderLineStmt.setInt(3, line.quantity());
                orderLineStmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
                Menu.println("Transaction rolled back.");
            } catch (SQLException rollbackEx) {
                Menu.println("Rollback failed: " + rollbackEx.getMessage());
            }
            throw e;
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (orderStmt != null) orderStmt.close();
            if (orderLineStmt != null) orderLineStmt.close();
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    public void listOrderDetails(int order_id) throws SQLException {
        String orderInfoSql = """
        SELECT
            CONCAT(c.first_name, ' ', c.last_name) AS customer_name,
            CONCAT(e.first_name, ' ', e.last_name) AS employee_name
        FROM order_head oh
        JOIN customer c ON oh.customer_id = c.id
        JOIN employee e ON oh.employee_id = e.id
        WHERE oh.id = ?
    """;

        String orderLinesSql = """
        SELECT
            f.name AS furniture_name,
            ol.quantity
        FROM order_line ol
        JOIN furniture f ON ol.furniture_id = f.id
        WHERE ol.order_id = ?
    """;

        try (
                PreparedStatement orderStmt = connection.prepareStatement(orderInfoSql);
                PreparedStatement linesStmt = connection.prepareStatement(orderLinesSql)
        ) {
            orderStmt.setInt(1, order_id);
            try (ResultSet rs = orderStmt.executeQuery()) {
                if (rs.next()) {
                    String customerName = rs.getString("customer_name");
                    String employeeName = rs.getString("employee_name");

                    Menu.println("Customer: " + customerName);
                    Menu.println("Employee: " + employeeName);
                    Menu.println("Items:");
                } else {
                    Menu.println("No order found with ID: " + order_id);
                    return;
                }
            }

            linesStmt.setInt(1, order_id);
            try (ResultSet rs = linesStmt.executeQuery()) {
                int index = 1;
                while (rs.next()) {
                    String itemName = rs.getString("furniture_name");
                    int quantity = rs.getInt("quantity");

                    Menu.printf("  %d. %s (Qty: %d)%n", index++, itemName, quantity);
                }
            }
        }
    }
}
