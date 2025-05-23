package se.hkr.dao;

import se.hkr.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class EmployeeDAO {
    private final Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    public Optional<Employee> getEmployee(int employee_id) throws SQLException {
        String sql = """
        SELECT id, address, city, first_name, last_name, postal_code
        FROM employee
        WHERE id = ?
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employee_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Employee(
                            rs.getLong("id"),
                            rs.getString("address"),
                            rs.getString("city"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("postal_code")
                    ));
                }
            }
        }
        return Optional.empty();
    }
}
