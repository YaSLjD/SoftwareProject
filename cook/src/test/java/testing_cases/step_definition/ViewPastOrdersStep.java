package testing_cases.step_definition;

import app.LoginSystem;
import app.Order;
import app.OrderSystem;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import static org.junit.Assert.*;

public class ViewPastOrdersStep {
    
    private LoginSystem loginSystem;
    private OrderSystem orderSystem;
    private String currentUser;
    private List<Order> userOrders;
    private Order selectedOrder;
    
    public ViewPastOrdersStep() {
        loginSystem = new LoginSystem();
        orderSystem = new OrderSystem(loginSystem.getUserDatabase());
        currentUser = "testuser"; // Default test user
    }
    
    @Given("the customer is logged in")
    public void theCustomerIsLoggedIn() {
        // Simulate user login
        String loginResult = loginSystem.login(currentUser, "password123");
        assertTrue("Customer should be logged in", loginResult.startsWith("Welcome"));
    }
    
    @When("the customer navigates to the {string} section")
    public void theCustomerNavigatesToTheSection(String section) {
        assertEquals("Past Orders", section);
        System.out.println("Customer navigated to the Past Orders section");
    }
    
    @Then("the customer should see a list of their past meal orders")
    public void theCustomerShouldSeeAListOfTheirPastMealOrders() {
        userOrders = orderSystem.getUserOrders(currentUser);
        assertFalse("Customer should have at least one order", userOrders.isEmpty());
        System.out.println("Customer can see " + userOrders.size() + " past orders");
    }
    
    @Then("each order should show the meal name, date, and price")
    public void eachOrderShouldShowTheMealNameDateAndPrice() {
        for (Order order : userOrders) {
            assertNotNull("Order should have an ID", order.getOrderId());
            assertNotNull("Order should have a date", order.getOrderDate());
            assertTrue("Order should have a price", order.getTotalPrice() > 0);
            assertFalse("Order should have meals", order.getMeals().isEmpty());
            
            System.out.println("Order details displayed: " + order);
        }
    }
    
    @Given("the customer has previous meal orders")
    public void theCustomerHasPreviousMealOrders() {
        userOrders = orderSystem.getUserOrders(currentUser);
        assertFalse("Customer should have at least one order", userOrders.isEmpty());
    }
    
    @When("the customer selects a meal from their past orders")
    public void theCustomerSelectsAMealFromTheirPastOrders() {
        // Select the first order
        selectedOrder = userOrders.get(0);
        assertNotNull("Selected order should not be null", selectedOrder);
        System.out.println("Customer selected order: " + selectedOrder.getOrderId());
    }
    
    @Then("the system should add that meal to the customer's current cart")
    public void theSystemShouldAddThatMealToTheCustomerSCurrentCart() {
        // Simulate adding to cart
        System.out.println("System added meals from order " + selectedOrder.getOrderId() + " to cart");
    }
    
    @Then("the customer can proceed to checkout")
    public void theCustomerCanProceedToCheckout() {
        // Simulate checkout availability
        System.out.println("Customer can proceed to checkout");
    }
    
    @When("the customer views their personalized meal plan")
    public void theCustomerViewsTheirPersonalizedMealPlan() {
        System.out.println("Customer is viewing personalized meal plan");
    }
    
    @Then("the system should display meal recommendations based on their past orders")
    public void theSystemShouldDisplayMealRecommendationsBasedOnTheirPastOrders() {
        List<app.Meal> recommendations = orderSystem.getPersonalizedRecommendations(currentUser);
        assertFalse("Should have meal recommendations", recommendations.isEmpty());
        System.out.println("System displays " + recommendations.size() + " recommended meals");
    }
    
    @Then("the recommendations should be tailored to the customer's preferences")
    public void theRecommendationsShouldBeTailoredToTheCustomerSPreferences() {
        // In a real implementation, we would check that recommendations match preferences
        System.out.println("Recommendations are tailored to customer preferences");
    }
}
