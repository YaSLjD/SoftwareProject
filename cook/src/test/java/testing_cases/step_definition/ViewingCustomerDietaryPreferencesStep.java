package testing_cases.step_definition;

import app.LoginSystem;
import app.User;
import app.UserPreferenceSystem;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import static org.junit.Assert.*;

public class ViewingCustomerDietaryPreferencesStep {
    
    private LoginSystem loginSystem;
    private UserPreferenceSystem preferenceSystem;
    private String chefUsername;
    private String customerUsername;
    private List<String> customerPreferences;
    private List<String> customerAllergies;
    
    public ViewingCustomerDietaryPreferencesStep() {
        loginSystem = new LoginSystem();
        preferenceSystem = new UserPreferenceSystem(loginSystem.getUserDatabase());
        chefUsername = "chef";
        customerUsername = "testuser"; // Default test user with preferences
    }
    
    @Given("the chef is logged into the system")
    public void theChefIsLoggedIntoTheSystem() {
        // Simulate chef login
        String loginResult = loginSystem.login(chefUsername, "chefpass");
        assertTrue("Chef should be logged in", loginResult.startsWith("Welcome"));
    }
    
    @When("the chef views the customer's profile")
    public void theChefViewsTheCustomerSProfile() {
        // Simulate viewing customer profile
        User customer = loginSystem.getUserDatabase().get(customerUsername);
        assertNotNull("Customer should exist", customer);
        System.out.println("Chef is viewing profile for customer: " + customerUsername);
    }
    
    @Then("the chef should see the customer's dietary preferences and allergies")
    public void theChefShouldSeeTheCustomerSDietaryPreferencesAndAllergies() {
        // Get customer preferences and allergies
        customerPreferences = preferenceSystem.getUserDietaryPreferences(customerUsername);
        customerAllergies = preferenceSystem.getUserAllergies(customerUsername);
        
        assertFalse("Customer should have dietary preferences", customerPreferences.isEmpty());
        assertFalse("Customer should have allergies", customerAllergies.isEmpty());
        
        System.out.println("Chef can see customer preferences: " + customerPreferences);
        System.out.println("Chef can see customer allergies: " + customerAllergies);
    }
    
    @Then("the chef should be able to customize meals based on those preferences")
    public void theChefShouldBeAbleToCustomizeMealsBasedOnThosePreferences() {
        // Simulate meal customization
        System.out.println("Chef can customize meals based on:");
        System.out.println("- Preferences: " + customerPreferences);
        System.out.println("- Allergies to avoid: " + customerAllergies);
    }
    
    @When("the chef searches for the customer's profile")
    public void theChefSearchesForTheCustomerSProfile() {
        // Simulate searching for customer profile
        User customer = loginSystem.getUserDatabase().get(customerUsername);
        assertNotNull("Customer should exist", customer);
        System.out.println("Chef searched and found customer: " + customerUsername);
    }
    
    @Then("the system should display the customer's order history")
    public void theSystemShouldDisplayTheCustomerSOrderHistory() {
        // Simulate viewing order history
        User customer = loginSystem.getUserDatabase().get(customerUsername);
        assertFalse("Customer should have order history", customer.getOrderHistory().isEmpty());
        System.out.println("System displays " + customer.getOrderHistory().size() + " past orders");
    }
}
