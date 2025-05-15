package test.java.testing_cases.step_definition;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.CustomMealService;
import app.Inventory;
import app.LoginSystem;
import app.Meal;
import app.Order;
import app.OrderSystem;
import app.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CustomMealRequestsStep {
    
    private LoginSystem loginSystem;
    private OrderSystem orderSystem;
    private CustomMealService customMealService;
    private Inventory inventory;
    
    private String currentUser;
    private Meal currentMeal;
    private Order currentOrder;
    private Set<String> addedIngredients;
    private Set<String> removedIngredients;
    private double originalPrice;
    private String incompatibilityWarning;
    private String unavailabilityMessage;
    
    public CustomMealRequestsStep() {
        loginSystem = new LoginSystem();
        inventory = new Inventory();
        orderSystem = new OrderSystem(loginSystem.getUserDatabase());
        customMealService = new CustomMealService(inventory);
        
        currentUser = "testuser"; // Default test user
        addedIngredients = new HashSet<>();
        removedIngredients = new HashSet<>();
    }
    
    @Given("the customer is logged in to customize a meal")
    public void theCustomerIsLoggedInToCustomizeAMeal() {
        // Similar to ViewPastOrdersStep but with a unique step name
        String loginResult = loginSystem.login(currentUser, "password123");
        assertTrue("Customer should be logged in", loginResult.startsWith("Welcome"));
    }
    
    @Given("there is a customizable meal available")
    public void thereIsACustomizableMealAvailable() {
        // Create a customizable base meal with some default ingredients
        currentMeal = customMealService.createCustomizableMeal(
                "CM001", "Customizable Rice Bowl", "Build your own rice bowl with your choice of ingredients", 9.99);
        
        // Add default ingredients
        currentMeal.addIngredient("White Rice");
        currentMeal.addIngredient("Mixed Vegetables");
        currentMeal.addIngredient("Soy Sauce");
        
        // Add it to the order system
        orderSystem.addMeal(currentMeal);
        
        assertNotNull("A customizable meal should be available", currentMeal);
        System.out.println("Customizable meal available: " + currentMeal.getName());
    }
    
    @When("the customer selects the meal for customization")
    public void theCustomerSelectsTheMealForCustomization() {
        originalPrice = currentMeal.getPrice();
        System.out.println("Customer selected the meal for customization: " + currentMeal.getName());
        System.out.println("Original price: $" + String.format("%.2f", originalPrice));
        System.out.println("Default ingredients: " + currentMeal.getIngredients());
    }
    
    @When("the customer adds ingredient {string} to the meal")
    public void theCustomerAddsIngredientToTheMeal(String ingredientName) {
        boolean success = customMealService.addIngredientToMeal(currentMeal, ingredientName);
        assertTrue("Should be able to add " + ingredientName + " to the meal", success);
        
        addedIngredients.add(ingredientName);
        System.out.println("Added ingredient: " + ingredientName);
    }
    
    @When("the customer removes ingredient {string} from the meal")
    public void theCustomerRemovesIngredientFromTheMeal(String ingredientName) {
        boolean success = customMealService.removeIngredientFromMeal(currentMeal, ingredientName);
        assertTrue("Should be able to remove " + ingredientName + " from the meal", success);
        
        removedIngredients.add(ingredientName);
        System.out.println("Removed ingredient: " + ingredientName);
    }
    
    @When("the customer attempts to add incompatible ingredient {string}")
    public void theCustomerAttemptsToAddIncompatibleIngredient(String ingredientName) {
        // First check compatibility
        boolean isCompatible = customMealService.isCompatibleWithMeal(currentMeal, ingredientName);
        
        if (!isCompatible) {
            incompatibilityWarning = "Warning: " + ingredientName + " is not compatible with your current ingredients.";
            System.out.println(incompatibilityWarning);
        } else {
            // If there's no compatibility issue, try to add it
            boolean success = customMealService.addIngredientToMeal(currentMeal, ingredientName);
            
            // For test purposes, we expect this to fail
            assertFalse("Should not be able to add incompatible ingredient: " + ingredientName, success);
        }
    }
    
    @When("the customer attempts to add unavailable ingredient {string}")
    public void theCustomerAttemptsToAddUnavailableIngredient(String ingredientName) {
        boolean isAvailable = customMealService.isIngredientAvailable(ingredientName);
        
        if (!isAvailable) {
            unavailabilityMessage = "Sorry, " + ingredientName + " is currently unavailable.";
            System.out.println(unavailabilityMessage);
        } else {
            // If it's available (which shouldn't happen for this test), try to add it
            boolean success = customMealService.addIngredientToMeal(currentMeal, ingredientName);
            
            // For test purposes, we expect this to fail
            assertFalse("Should not be able to add unavailable ingredient: " + ingredientName, success);
        }
    }
    
    @Then("the meal should contain ingredient {string}")
    public void theMealShouldContainIngredient(String ingredientName) {
        assertTrue("Meal should contain " + ingredientName, 
                currentMeal.getIngredients().contains(ingredientName));
        System.out.println("Confirmed meal contains: " + ingredientName);
    }
    
    @Then("the meal should not contain ingredient {string}")
    public void theMealShouldNotContainIngredient(String ingredientName) {
        assertFalse("Meal should not contain " + ingredientName, 
                currentMeal.getIngredients().contains(ingredientName));
        System.out.println("Confirmed meal does not contain: " + ingredientName);
    }
    
    @Then("the price should be updated accordingly")
    public void thePriceShouldBeUpdatedAccordingly() {
        double expectedPrice = customMealService.calculateCustomizedPrice(
                currentMeal, addedIngredients, removedIngredients);
        
        // Update the meal price
        currentMeal.setPrice(expectedPrice);
        
        assertNotEquals("Price should be different from original", originalPrice, currentMeal.getPrice(), 0.01);
        System.out.println("New price: $" + String.format("%.2f", currentMeal.getPrice()));
    }
    
    @Then("the system should display an incompatibility warning")
    public void theSystemShouldDisplayAnIncompatibilityWarning() {
        assertNotNull("System should display an incompatibility warning", incompatibilityWarning);
        System.out.println("Compatibility warning displayed: " + incompatibilityWarning);
    }
    
    @Then("the system should display an unavailability message")
    public void theSystemShouldDisplayAnUnavailabilityMessage() {
        assertNotNull("System should display an unavailability message", unavailabilityMessage);
        System.out.println("Unavailability message displayed: " + unavailabilityMessage);
    }
    
    @Given("the customer has customized a meal")
    public void theCustomerHasCustomizedAMeal() {
        // Reuse steps from above to create and customize a meal
        thereIsACustomizableMealAvailable();
        theCustomerSelectsTheMealForCustomization();
        
        // Add a couple of ingredients
        theCustomerAddsIngredientToTheMeal("Chicken");
        theCustomerAddsIngredientToTheMeal("Brown Rice");
        theCustomerRemovesIngredientFromTheMeal("White Rice");
        
        // Update the price
        thePriceShouldBeUpdatedAccordingly();
    }
    
    @When("the customer places the order")
    public void theCustomerPlacesTheOrder() {
        // Create a new order for the customer
        currentOrder = orderSystem.createOrder(currentUser);
        assertNotNull("Order should be created", currentOrder);
        
        // Add the customized meal to the order
        boolean success = orderSystem.addMealToOrder(currentOrder.getOrderId(), currentMeal.getId());
        assertTrue("Meal should be added to the order", success);
        
        System.out.println("Customer placed order #" + currentOrder.getOrderId() + " with the customized meal");
    }
    
    @Then("the order should be created with the customized meal")
    public void theOrderShouldBeCreatedWithTheCustomizedMeal() {
        assertNotNull("Order should exist", currentOrder);
        assertFalse("Order should contain meals", currentOrder.getMeals().isEmpty());
        
        boolean mealFound = false;
        for (Meal meal : currentOrder.getMeals()) {
            if (meal.getId().equals(currentMeal.getId())) {
                mealFound = true;
                
                // Verify some ingredients
                assertTrue("Meal should contain Chicken", meal.getIngredients().contains("Chicken"));
                assertTrue("Meal should contain Brown Rice", meal.getIngredients().contains("Brown Rice"));
                assertFalse("Meal should not contain White Rice", meal.getIngredients().contains("White Rice"));
                
                break;
            }
        }
        
        assertTrue("The customized meal should be in the order", mealFound);
    }
    
    @Then("the order should appear in the customer's order history")
    public void theOrderShouldAppearInTheCustomerSOrderHistory() {
        List<app.Order> customerOrders = orderSystem.getUserOrders(currentUser);
        assertFalse("Customer should have orders", customerOrders.isEmpty());
        
        boolean orderFound = false;
        for (Order order : customerOrders) {
            if (order.getOrderId().equals(currentOrder.getOrderId())) {
                orderFound = true;
                break;
            }
        }
        
        assertTrue("The order should appear in the customer's order history", orderFound);
        System.out.println("Confirmed the order appears in the customer's order history");
    }
}
