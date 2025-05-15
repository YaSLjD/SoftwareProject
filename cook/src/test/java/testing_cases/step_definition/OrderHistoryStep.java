package testing_cases.step_definition;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.LoginSystem;
import app.Meal;
import app.Order;
import app.OrderSystem;
import app.User;
import app.logger;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OrderHistoryStep {

    private LoginSystem loginSystem;
    private OrderSystem orderSystem;
    private List<Order> orderHistory;
    private Map<String, Double> orderStatistics;
    private String filterUsername;

    public OrderHistoryStep() {
        loginSystem = new LoginSystem();
        orderSystem = new OrderSystem(loginSystem.getUserDatabase());
        
        // Admin user should already be created in LoginSystem constructor
        
        // Setup test data - customers with orders
        setupTestCustomers();
    }
    
    private void setupTestCustomers() {
        // Create test customers if they don't exist
        String[] testUsers = {"testuser", "customer1", "customer2"};
        for (String username : testUsers) {
            if (loginSystem.getUserDatabase().get(username) == null) {
                // Create user directly instead of using registerUser
                User newUser = new User(username, "password123", "customer");
                loginSystem.getUserDatabase().put(username, newUser);
            }
            
            // Create some test orders for each customer
            createTestOrdersForUser(username);
        }
    }
    
    private void createTestOrdersForUser(String username) {
        User user = loginSystem.getUserDatabase().get(username);
        
        // Only create orders if the user doesn't have any
        if (user.getOrderHistory().isEmpty()) {
            // Create 3 orders for test data
            for (int i = 0; i < 3; i++) {
                Order order = orderSystem.createOrder(username);
                
                // Add a meal to the order
                List<Meal> availableMeals = orderSystem.getAllMeals();
                if (!availableMeals.isEmpty()) {
                    orderSystem.addMealToOrder(order.getOrderId(), availableMeals.get(i % availableMeals.size()).getId());
                }
                
                // Set different statuses for testing
                String[] statuses = {"pending", "processing", "completed"};
                orderSystem.updateOrderStatus(order.getOrderId(), statuses[i % statuses.length]);
            }
        }
    }

    @Given("the administrator is logged in")
    public void theAdministratorIsLoggedIn() {
        String loginResult = loginSystem.login("admin", "adminpass");
        assertTrue("Admin should be logged in", loginResult.startsWith("Welcome"));
    }

    @When("the administrator navigates to the {string} section")
    public void theAdministratorNavigatesToTheSection(String section) {
        assertEquals("Order History", section);
        logger.info("Administrator navigated to the Order History section");
    }

    @Then("the administrator should see a list of all customer orders")
    public void theAdministratorShouldSeeAListOfAllCustomerOrders() {
        // Get all orders from all users
        orderHistory = new ArrayList<>();
        for (User user : loginSystem.getUserDatabase().values()) {
            orderHistory.addAll(user.getOrderHistory());
        }
        
        assertFalse("There should be orders in the system", orderHistory.isEmpty());
        logger.info("Admin can see " + orderHistory.size() + " orders across all customers");
    }

    @Then("each order should show the customer name, order date, and total price")
    public void eachOrderShouldShowTheCustomerNameOrderDateAndTotalPrice() {
        for (Order order : orderHistory) {
            assertNotNull("Order should have a customer name", order.getUsername());
            assertNotNull("Order should have a date", order.getOrderDate());
            assertTrue("Order should have a price", order.getTotalPrice() > 0);
            
            System.out.println("Order details displayed: " + order.getOrderId() + 
                    " | Customer: " + order.getUsername() + 
                    " | Date: " + order.getOrderDate() + 
                    " | Total: $" + String.format("%.2f", order.getTotalPrice()));
        }
    }

    @When("the administrator selects to filter by customer {string}")
    public void theAdministratorSelectsToFilterByCustomer(String username) {
        filterUsername = username;
        logger.info("Administrator filtering orders by customer: " + username);
    }

    @Then("the administrator should only see orders placed by {string}")
    public void theAdministratorShouldOnlySeeOrdersPlacedByCustomer(String username) {
        List<Order> filteredOrders = orderSystem.getUserOrders(username);
        
        assertFalse("Customer should have orders", filteredOrders.isEmpty());
        
        for (Order order : filteredOrders) {
            assertEquals("Order should be from the filtered customer", username, order.getUsername());
            System.out.println("Filtered order displayed: " + order.getOrderId() + 
                    " | Date: " + order.getOrderDate() + 
                    " | Total: $" + String.format("%.2f", order.getTotalPrice()));
        }
    }

    @When("the administrator views order statistics")
    public void theAdministratorViewsOrderStatistics() {
        orderStatistics = new HashMap<>();
        
        // Get all orders
        List<Order> allOrders = new ArrayList<>();
        for (User user : loginSystem.getUserDatabase().values()) {
            allOrders.addAll(user.getOrderHistory());
        }
        
        // Calculate statistics
        double totalValue = 0;
        for (Order order : allOrders) {
            totalValue += order.getTotalPrice();
        }
        
        orderStatistics.put("totalOrders", (double) allOrders.size());
        orderStatistics.put("totalValue", totalValue);
        orderStatistics.put("averageValue", allOrders.isEmpty() ? 0 : totalValue / allOrders.size());
        
        logger.info("Administrator is viewing order statistics");
    }

    @Then("the administrator should see summary data including total orders and average order value")
    public void theAdministratorShouldSeeSummaryDataIncludingTotalOrdersAndAverageOrderValue() {
        assertTrue("There should be orders in the system", orderStatistics.get("totalOrders") > 0);
        assertTrue("Total order value should be greater than zero", orderStatistics.get("totalValue") > 0);
        
        logger.info("Statistics displayed:");
        logger.info("- Total Orders: " + orderStatistics.get("totalOrders").intValue());
        logger.info("- Total Value: $" + String.format("%.2f", orderStatistics.get("totalValue")));
        logger.info("- Average Order Value: $" + String.format("%.2f", orderStatistics.get("averageValue")));
    }
}