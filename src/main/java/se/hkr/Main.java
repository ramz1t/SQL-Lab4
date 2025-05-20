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

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

public class Main {
    private static Menu menu;
    private static CustomerDAO customerDA0;
    private static OrderDAO orderDAO;
    private static EmployeeDAO employeeDAO;

    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            customerDA0 = new CustomerDAO(conn);
            orderDAO = new OrderDAO(conn);
            employeeDAO = new EmployeeDAO(conn);

            menu = new Menu();
            setupMenuOptions();
            menu.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setupMenuOptions() {
        menu.addOption("1", "List all customers", Main::listAllCustomers);
        menu.addOption("2", "Create a new customer", Main::createCustomer);
        menu.addOption("3", "List all employee's orders", Main::listEmployeesOrders);
        menu.addOption("4", "Create order head and order lines", Main::createOrderHeadAndOrderLines);
        menu.addOption("5", "Print order details", Main::printOrderDetails);
        menu.addOption("6", "Print employee details", Main::printEmployeeDetails);
        menu.addOption("7", "Update customer's address", Main::updateCustomerAddress);
    }

    public static void listAllCustomers() {
        try {
            List<Customer> customers = customerDA0.listAllCustomers();
            customers.forEach(System.out::println);
        } catch (SQLException e) {
            menu.println("Failed to list all customers: " + e.getMessage());
        }
    }

    public static void createCustomer() {
        String first_name = menu.prompt("First name: ");
        String last_name = menu.prompt("Last name: ");
        String address = menu.prompt("Address: ");
        String city = menu.prompt("City: ");
        String postal_code = menu.prompt("Postal code: ");
        String birthDateStr = menu.prompt("Birth date (YYYY-MM-DD): ");

        try {
            Date birth_date = Date.valueOf(birthDateStr);

            Customer customer = new Customer(
                    0,
                    address,
                    birth_date,
                    city,
                    first_name,
                    last_name,
                    postal_code
            );

            customerDA0.createCustomer(customer);
            menu.println("Customer created successfully!");
        } catch (SQLException e) {
            menu.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            menu.println("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    public static void listEmployeesOrders() {
        try {
            int emp_id = Integer.parseInt(menu.prompt("Employee id: "));
            List<OrderHead> orders = orderDAO.listOrdersForEmployee(emp_id);
            orders.forEach(System.out::println);
        } catch (NumberFormatException e) {
            menu.println("Employee id is not a number");
        } catch (SQLException e) {
            menu.println("Database error: " + e.getMessage());
        }
    }

    public static void createOrderHeadAndOrderLines() {
        try {
            long customerId = Long.parseLong(menu.prompt("Customer ID: "));
            long employeeId = Long.parseLong(menu.prompt("Employee ID: "));

            Date orderDate = new java.sql.Date(System.currentTimeMillis()); // current date
            OrderHead order = new OrderHead(0, orderDate, customerId, employeeId); // id=0 (auto generated)

            List<OrderLine> orderLines = new ArrayList<>();

            while (true) {
                String addMore = menu.prompt("Add an order line? (yes/no): ").trim().toLowerCase();
                if (!addMore.equals("yes")) break;

                long furnitureId = Long.parseLong(menu.prompt("Furniture ID: "));
                int quantity = Integer.parseInt(menu.prompt("Quantity: "));

                // id=0 and order_id=0 (both auto generated)
                OrderLine line = new OrderLine(0, furnitureId, 0, quantity);
                orderLines.add(line);
            }

            orderDAO.createOrder(order, orderLines);
            menu.println("Order created successfully.");

        } catch (Exception e) {
            menu.println("Failed to create order: " + e.getMessage());
        }
    }

    public static void printOrderDetails() {
        try {
            int order_id = Integer.parseInt(menu.prompt("Order id: "));
            orderDAO.listOrderDetails(order_id);
        } catch (NumberFormatException e) {
            menu.println("Order id is not a number");
        } catch (SQLException e) {
            menu.println("Could not display order's details: " + e.getMessage());
        }
    }

    public static void printEmployeeDetails() {
        try {
            int employee_id = Integer.parseInt(menu.prompt("Employee id: "));
            Employee emp = employeeDAO.getEmployee(employee_id);
            menu.println(emp.toString());
        } catch (NumberFormatException e) {
            menu.println("Employee id is not a number");
        } catch (SQLException e) {
            menu.println("Couldn't get employee: " + e.getMessage());
        }
    }

    public static void updateCustomerAddress() {
        try {
            int customer_id = Integer.parseInt(menu.prompt("Customer's id: "));
            String new_address = menu.prompt("New address: ");
            customerDA0.updateCustomerAddress(customer_id, new_address);
            menu.println("Updated!");
        } catch (SQLException e) {
            menu.println("Couldn't update customer's address: " + e.getMessage());
        } catch (NumberFormatException e) {
            menu.println("Customer id is not a number");
        }
    }
}