package se.hkr.dao;

import se.hkr.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    Connection connection;

    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Customer> listAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT * FROM customer";

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getLong("id"),
                        rs.getString("address"),
                        rs.getDate("birth_date"),
                        rs.getString("city"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("postal_code")
                );
                customers.add(customer);
            }
        }

        return customers;
    }

    public void createCustomer(Customer customer) throws SQLException {
        String sql = """
        INSERT INTO customer (
                address,
                birth_date,
                city,
                first_name,
                last_name,
                postal_code
        ) VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, customer.address());
            stmt.setDate(2, customer.birth_date());
            stmt.setString(3, customer.city());
            stmt.setString(4, customer.first_name());
            stmt.setString(5, customer.last_name());
            stmt.setString(6, customer.postal_code());
            stmt.executeUpdate();
        }
    }

    public void updateCustomerAddress(int customer_id, String new_address) throws SQLException {
        String sql = "UPDATE customer SET address = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, new_address);
            stmt.setInt(2, customer_id);
            stmt.executeUpdate();
        }
    }
}
