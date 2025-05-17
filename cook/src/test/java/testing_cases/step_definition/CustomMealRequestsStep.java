package testing_cases.step_definition;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.plaf.synth.SynthContext;

import app.CustomMealService;
import app.Inventory;
import app.LoginSystem;
import app.Meal;
import app.Order;
import app.OrderSystem;
import app.User;
import common.logger;
import io.cucumber.java.Scenario;
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
    private boolean addIngredientResult;
    private boolean removeIngredientResult;
    
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
        logger.info("Customizable meal available: " + currentMeal.getName());
    }
    
    @When("the customer selects the meal for customization")
    public void theCustomerSelectsTheMealForCustomization() {
        originalPrice = currentMeal.getPrice();
        logger.info("Customer selected the meal for customization: " + currentMeal.getName());
        logger.info("Original price: $" + String.format("%.2f", originalPrice));
        logger.info("Default ingredients: " + currentMeal.getIngredients());
    }
    
    @When("the customer adds ingredient {string} to the meal")
    public void theCustomerAddsIngredientToTheMeal(String ingredientName) {
        boolean success = customMealService.addIngredientToMeal(currentMeal, ingredientName);
        assertTrue("Should be able to add " + ingredientName + " to the meal", success);
        
        addedIngredients.add(ingredientName);
        logger.info("Added ingredient: " + ingredientName);
    }
    
    @When("the customer removes ingredient {string} from the meal")
    public void theCustomerRemovesIngredientFromTheMeal(String ingredientName) {
        boolean success = customMealService.removeIngredientFromMeal(currentMeal, ingredientName);
        assertTrue("Should be able to remove " + ingredientName + " from the meal", success);
        
        removedIngredients.add(ingredientName);
        logger.info("Removed ingredient: " + ingredientName);
    }
    
    @When("the customer attempts to add incompatible ingredient {string}")
    public void theCustomerAttemptsToAddIncompatibleIngredient(String ingredientName) {
        logger.info("Current meal ingredients before adding: " + currentMeal.getIngredients());
        
        boolean isCompatible = customMealService.isCompatibleWithMeal(currentMeal, ingredientName);
        
        if (!isCompatible) {
            incompatibilityWarning = "Warning: " + ingredientName + " is not compatible with your current ingredients.";
            logger.info("Incompatibility warning set: " + incompatibilityWarning);
        } else {
            boolean success = customMealService.addIngredientToMeal(currentMeal, ingredientName);
            assertTrue("Should be able to add compatible ingredient: " + ingredientName, success);
        }
    }
    

    

    
    @When("the customer attempts to add unavailable ingredient {string}")
    public void theCustomerAttemptsToAddUnavailableIngredient(String ingredientName) {
        boolean isAvailable = customMealService.isIngredientAvailable(ingredientName);
        
        if (!isAvailable) {
            unavailabilityMessage = "Sorry, " + ingredientName + " is currently unavailable.";
            logger.info(unavailabilityMessage);
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
        logger.info("Confirmed meal contains: " + ingredientName);
    }
    
    @Then("the meal should not contain ingredient {string}")
    public void theMealShouldNotContainIngredient(String ingredientName) {
        assertFalse("Meal should not contain " + ingredientName, 
                currentMeal.getIngredients().contains(ingredientName));
        logger.info("Confirmed meal does not contain: " + ingredientName);
    }
    
    @Then("the price should be updated accordingly")
    public void thePriceShouldBeUpdatedAccordingly() {
        double expectedPrice = customMealService.calculateCustomizedPrice(
                currentMeal, addedIngredients, removedIngredients);
        
        // Update the meal price
        currentMeal.setPrice(expectedPrice);
        
        assertNotEquals("Price should be different from original", originalPrice, currentMeal.getPrice(), 0.01);
        logger.info("New price: $" + String.format("%.2f", currentMeal.getPrice()));
    }
    
    @Then("the system should display an incompatibility warning")
    public void theSystemShouldDisplayAnIncompatibilityWarning() {
        assertNotNull("System should display an incompatibility warning", incompatibilityWarning);
        logger.info("Compatibility warning displayed: " + incompatibilityWarning);
    }
    
    @Then("the system should display an unavailability message")
    public void theSystemShouldDisplayAnUnavailabilityMessage() {
        assertNotNull("System should display an unavailability message", unavailabilityMessage);
        logger.info("Unavailability message displayed: " + unavailabilityMessage);
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
        
        logger.info("Customer placed order #" + currentOrder.getOrderId() + " with the customized meal");
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
        logger.info("Confirmed the order appears in the customer's order history");
    }
    @When("internal setters and removals are triggered for coverage")
public void internal_setters_and_removals_are_triggered() {
    Meal testMeal = new Meal("ID-0", "Placeholder", "To be updated", 10.0);

    testMeal.setId("NEW-ID");
    testMeal.setName("Updated Name");
    testMeal.setDescription("Updated Description");

    testMeal.addAllergen("Milk");
    testMeal.addDietaryCategory("Keto");
    testMeal.removeAllergen("Milk");
    testMeal.removeDietaryCategory("Keto");

    assertEquals("NEW-ID", testMeal.getId());
    assertEquals("Updated Name", testMeal.getName());
    assertEquals("Updated Description", testMeal.getDescription());
    assertFalse(testMeal.getAllergens().contains("Milk"));
    assertFalse(testMeal.getDietaryCategories().contains("Keto"));
}

@Given("the meal already contains ingredient {string}")
public void the_meal_already_contains_ingredient(String ingredient) {
    currentMeal.addIngredient(ingredient);
    logger.info("Meal ingredients after adding: " + currentMeal.getIngredients());
}


@When("the customer attempts to add ingredient {string} to the meal")
public void the_customer_attempts_to_add_ingredient(String ingredient) {
    addIngredientResult = customMealService.addIngredientToMeal(currentMeal, ingredient);
}


}
