package se.hkr;

import se.hkr.dao.CustomerDAO;
import se.hkr.dao.EmployeeDAO;
import se.hkr.dao.OrderDAO;
import se.hkr.db.DBConnection;
import se.hkr.menu.Menu;
import se.hkr.model.Customer;
import se.hkr.model.Employee;
import se.hkr.model.OrderHead;
import se.hkr.model.OrderLine;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    private static Menu menu;
    private static CustomerDAO customerDAO;
    private static OrderDAO orderDAO;
    private static EmployeeDAO employeeDAO;

    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            initDAOs(conn);
            menu = new Menu();
            setupMenuOptions();
            menu.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initDAOs(Connection conn) {
        customerDAO = new CustomerDAO(conn);
        orderDAO = new OrderDAO(conn);
        employeeDAO = new EmployeeDAO(conn);
    }

    private static void setupMenuOptions() {
        menu.addOption("1", "List all customers", Main::listAllCustomers);
        menu.addOption("2", "Create a new customer", Main::createCustomer);
        menu.addOption("3", "List all employee's orders", Main::listEmployeesOrders);
        menu.addOption("4", "Create order head and order lines", Main::createOrderHeadAndOrderLines);
        menu.addOption("5", "Print order details", Main::printOrderDetails);
        menu.addOption("6", "Print employee details", Main::printEmployeeDetails);
        menu.addOption("7", "Update customer's address", Main::updateCustomerAddress);
    }

    private static void listAllCustomers() {
        try {
            List<Customer> customers = customerDAO.listAllCustomers();
            customers.forEach(System.out::println);
        } catch (SQLException e) {
            printError("Failed to list all customers", e);
        }
    }

    private static void createCustomer() {
        try {
            Customer customer = new Customer(
                    0,
                    menu.prompt("Address: "),
                    Date.valueOf(menu.prompt("Birth date (YYYY-MM-DD): ")),
                    menu.prompt("City: "),
                    menu.prompt("First name: "),
                    menu.prompt("Last name: "),
                    menu.prompt("Postal code: ")
            );

            customerDAO.createCustomer(customer);
            menu.println("Customer created successfully!");
        } catch (SQLException e) {
            printError("Database error", e);
        } catch (IllegalArgumentException e) {
            menu.println("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    private static void listEmployeesOrders() {
        try {
            int empId = Integer.parseInt(menu.prompt("Employee id: "));
            List<OrderHead> orders = orderDAO.listOrdersForEmployee(empId);
            orders.forEach(System.out::println);
        } catch (NumberFormatException e) {
            menu.println("Employee id is not a number.");
        } catch (SQLException e) {
            printError("Database error", e);
        }
    }

    private static void createOrderHeadAndOrderLines() {
        try {
            long customerId = Long.parseLong(menu.prompt("Customer ID: "));
            long employeeId = Long.parseLong(menu.prompt("Employee ID: "));
            Date orderDate = new Date(System.currentTimeMillis());

            OrderHead order = new OrderHead(0, orderDate, customerId, employeeId);
            List<OrderLine> orderLines = collectOrderLines();

            orderDAO.createOrder(order, orderLines);
            menu.println("Order created successfully.");
        } catch (Exception e) {
            printError("Failed to create order", e);
        }
    }

    private static List<OrderLine> collectOrderLines() {
        List<OrderLine> orderLines = new ArrayList<>();

        while (true) {
            String addMore = menu.prompt("Add an order line? (yes/no): ").trim().toLowerCase();
            if (!addMore.equals("yes")) break;

            try {
                long furnitureId = Long.parseLong(menu.prompt("Furniture ID: "));
                int quantity = Integer.parseInt(menu.prompt("Quantity: "));
                orderLines.add(new OrderLine(0, furnitureId, 0, quantity));
            } catch (NumberFormatException e) {
                menu.println("Invalid input. Please enter numeric values.");
            }
        }

        return orderLines;
    }

    private static void printOrderDetails() {
        try {
            int orderId = Integer.parseInt(menu.prompt("Order id: "));
            orderDAO.listOrderDetails(orderId);
        } catch (NumberFormatException e) {
            menu.println("Order id is not a number.");
        } catch (SQLException e) {
            printError("Could not display order details", e);
        }
    }

    private static void printEmployeeDetails() {
        try {
            int employeeId = Integer.parseInt(menu.prompt("Employee id: "));
            Optional<Employee> employee = employeeDAO.getEmployee(employeeId);

            if (employee.isPresent()) {
                menu.println(employee.toString());
            } else {
                menu.println("No employee found with ID: " + employeeId);
            }
        } catch (NumberFormatException e) {
            menu.println("Employee id is not a number.");
        } catch (SQLException e) {
            printError("Couldn't get employee", e);
        }
    }

    private static void updateCustomerAddress() {
        try {
            int customerId = Integer.parseInt(menu.prompt("Customer's id: "));
            String newAddress = menu.prompt("New address: ");
            customerDAO.updateCustomerAddress(customerId, newAddress);
            menu.println("Updated!");
        } catch (NumberFormatException e) {
            menu.println("Customer id is not a number.");
        } catch (SQLException e) {
            printError("Couldn't update customer's address", e);
        }
    }

    private static void printError(String message, Exception e) {
        menu.println(message + ": " + e.getMessage());
    }
}
