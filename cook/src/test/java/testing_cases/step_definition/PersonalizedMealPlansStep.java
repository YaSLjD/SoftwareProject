package testing_cases.step_definition;

import app.LoginSystem;
import app.Meal;
import app.Order;
import app.OrderSystem;
import app.User;
import app.UserPreferenceSystem;
import app.logger;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import static org.junit.Assert.*;

public class PersonalizedMealPlansStep {
    
    private LoginSystem loginSystem;
    private OrderSystem orderSystem;
    private UserPreferenceSystem preferenceSystem;
    private String chefUsername;
    private String customerUsername;
    private List<Meal> recommendedMeals;
    private Order personalizedMealPlan;
    
    public PersonalizedMealPlansStep() {
        loginSystem = new LoginSystem();
        orderSystem = new OrderSystem(loginSystem.getUserDatabase());
        preferenceSystem = new UserPreferenceSystem(loginSystem.getUserDatabase());
        chefUsername = "chef";
        customerUsername = "testuser"; // Default test user
    }
    
    @Given("the customer has placed previous orders")
    public void theCustomerHasPlacedPreviousOrders() {
        User customer = loginSystem.getUserDatabase().get(customerUsername);
        assertFalse("Customer should have order history", customer.getOrderHistory().isEmpty());
        logger.info("Customer has " + customer.getOrderHistory().size() + " previous orders");
    }
    
    @Given("the chef has accessed the customer's order history")
    public void theChefHasAccessedTheCustomerSOrderHistory() {
        // Simulate chef accessing customer order history
        List<Order> customerOrders = orderSystem.getUserOrders(customerUsername);
        assertFalse("Customer should have order history", customerOrders.isEmpty());
        logger.info("Chef accessed customer's order history with " + customerOrders.size() + " orders");
    }
    
    @Given("the system has recorded the customer's dietary preferences")
    public void theSystemHasRecordedTheCustomerSDietaryPreferences() {
        List<String> preferences = preferenceSystem.getUserDietaryPreferences(customerUsername);
        List<String> allergies = preferenceSystem.getUserAllergies(customerUsername);
        
        assertFalse("Customer should have dietary preferences", preferences.isEmpty());
        assertFalse("Customer should have allergies", allergies.isEmpty());
        
        logger.info("System has recorded customer preferences: " + preferences);
        logger.info("System has recorded customer allergies: " + allergies);
    }
    
    @When("the chef selects meals based on past preferences and dietary needs")
    public void theChefSelectsMealsBasedOnPastPreferencesAndDietaryNeeds() {
        // Get personalized recommendations
        recommendedMeals = orderSystem.getPersonalizedRecommendations(customerUsername);
        assertFalse("Should have meal recommendations", recommendedMeals.isEmpty());
        
        // Create a new meal plan (order)
        personalizedMealPlan = orderSystem.createOrder(customerUsername);
        assertNotNull("Should create a meal plan", personalizedMealPlan);
        
        // Add recommended meals to the plan
        for (Meal meal : recommendedMeals.subList(0, Math.min(3, recommendedMeals.size()))) {
            orderSystem.addMealToOrder(personalizedMealPlan.getOrderId(), meal.getId());
        }
        
        logger.info("Chef selected " + personalizedMealPlan.getMeals().size() + " meals for the plan");
    }
    
    @Then("the system should allow the chef to save and send the meal plan")
    public void theSystemShouldAllowTheChefToSaveAndSendTheMealPlan() {
        // Simulate saving and sending the meal plan
        orderSystem.updateOrderStatus(personalizedMealPlan.getOrderId(), "pending");
        assertEquals("pending", personalizedMealPlan.getStatus());
        
        logger.info("Chef saved and sent the meal plan with ID: " + personalizedMealPlan.getOrderId());
    }
    
    @Given("the chef has submitted a personalized meal plan")
    public void theChefHasSubmittedAPersonalizedMealPlan() {
        // Create a meal plan if not already created
        if (personalizedMealPlan == null) {
            personalizedMealPlan = orderSystem.createOrder(customerUsername);
            assertNotNull("Should create a meal plan", personalizedMealPlan);
            
            // Add some meals to the plan
            List<Meal> allMeals = orderSystem.getAllMeals();
            for (Meal meal : allMeals.subList(0, Math.min(3, allMeals.size()))) {
                orderSystem.addMealToOrder(personalizedMealPlan.getOrderId(), meal.getId());
            }
            
            orderSystem.updateOrderStatus(personalizedMealPlan.getOrderId(), "pending");
        }
        
        logger.info("Chef has submitted a personalized meal plan with ID: " + personalizedMealPlan.getOrderId());
    }
    
    @When("the system processes and sends the plan")
    public void theSystemProcessesAndSendsThePlan() {
        // Simulate processing and sending the plan
        orderSystem.updateOrderStatus(personalizedMealPlan.getOrderId(), "processed");
        assertEquals("processed", personalizedMealPlan.getStatus());
        
        logger.info("System processed and sent the meal plan");
    }
    
    @Then("the customer should receive a notification with the suggested meals")
    public void theCustomerShouldReceiveANotificationWithTheSuggestedMeals() {
        // Simulate notification
        System.out.println("Customer received notification with " + 
                personalizedMealPlan.getMeals().size() + " suggested meals");
        
        for (Meal meal : personalizedMealPlan.getMeals()) {
            logger.info("- " + meal.getName() + " ($" + meal.getPrice() + ")");
        }
    }
    
    @Given("an order is placed by a customer")
    public void anOrderIsPlacedByACustomer() {
        // Create a new order if not already created
        if (personalizedMealPlan == null) {
            personalizedMealPlan = orderSystem.createOrder(customerUsername);
            assertNotNull("Should create an order", personalizedMealPlan);
            
            // Add some meals to the order
            List<Meal> allMeals = orderSystem.getAllMeals();
            for (Meal meal : allMeals.subList(0, Math.min(2, allMeals.size()))) {
                orderSystem.addMealToOrder(personalizedMealPlan.getOrderId(), meal.getId());
            }
        }
        
        logger.info("Customer placed order with ID: " + personalizedMealPlan.getOrderId());
    }
    
    @When("the system processes the order")
    public void theSystemProcessesTheOrder() {
        // Simulate order processing
        orderSystem.updateOrderStatus(personalizedMealPlan.getOrderId(), "processing");
        assertEquals("processing", personalizedMealPlan.getStatus());
        
        logger.info("System processed the order");
    }
    
    @Then("the system should store the order details in the database")
    public void theSystemShouldStoreTheOrderDetailsInTheDatabase() {
        // Verify order is in the database
        Order storedOrder = orderSystem.getOrderById(personalizedMealPlan.getOrderId());
        assertNotNull("Order should be stored in the database", storedOrder);
        assertEquals(personalizedMealPlan.getOrderId(), storedOrder.getOrderId());
        
        logger.info("Order details stored in the database");
    }
    
    @Given("the system administrator wants to analyze trends")
    public void theSystemAdministratorWantsToAnalyzeTrends() {
        // Simulate admin login
        String loginResult = loginSystem.login("admin", "adminpass");
        assertTrue("Admin should be logged in", loginResult.startsWith("Welcome"));
        
        logger.info("System administrator is analyzing trends");
    }
    
    @When("they request order history data")
    public void theyRequestOrderHistoryData() {
        // Simulate requesting order history data
        logger.info("Administrator requested order history data");
    }
    
    @Then("the system should retrieve and display historical order data")
    public void theSystemShouldRetrieveAndDisplayHistoricalOrderData() {
        // Simulate retrieving historical data
        logger.info("System retrieved and displayed historical order data");
    }
    
    @Given("the system administrator wants to improve service offerings")
    public void theSystemAdministratorWantsToImproveServiceOfferings() {
        // Simulate admin login
        String loginResult = loginSystem.login("admin", "adminpass");
        assertTrue("Admin should be logged in", loginResult.startsWith("Welcome"));
        
        logger.info("System administrator wants to improve service offerings");
    }
    
    @When("they generate a report on past order data")
    public void theyGenerateAReportOnPastOrderData() {
        // Simulate generating a report
        logger.info("Administrator generated a report on past order data");
    }
    
    @Then("the system should provide insights and trends based on order history")
    public void theSystemShouldProvideInsightsAndTrendsBasedOnOrderHistory() {
        // Simulate providing insightss
        logger.info("System provided insights and trends based on order history");
    }
}


