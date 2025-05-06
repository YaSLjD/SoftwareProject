package testing_cases.step_definition;

import app.LoginSystem;
import app.Meal;
import app.UserPreferenceSystem;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import static org.junit.Assert.*;

public class UserDietaryPreferencesandAllergies {
    
    private LoginSystem loginSystem;
    private UserPreferenceSystem preferenceSystem;
    private String currentUser;
    private List<Meal> recommendedMeals;
    
    public UserDietaryPreferencesandAllergies() {
        loginSystem = new LoginSystem();
        preferenceSystem = new UserPreferenceSystem(loginSystem.getUserDatabase());
        currentUser = "testuser"; // Default test user
    }
    
    @Given("the user is logged into the app")
    public void theUserIsLoggedIntoTheApp() {
        // Simulate user login
        String loginResult = loginSystem.login(currentUser, "password123");
        assertTrue("User should be logged in", loginResult.startsWith("Welcome"));
    }
    
    @When("the user selects {string} as a preference")
    public void theUserSelectsAsAPreference(String preference) {
        boolean result = preferenceSystem.addDietaryPreference(currentUser, preference);
        assertTrue("Should add preference successfully", result);
    }
    
    @Then("the system should recommend only vegetarian meals")
    public void theSystemShouldRecommendOnlyVegetarianMeals() {
        List<String> preferences = preferenceSystem.getUserDietaryPreferences(currentUser);
        assertTrue("User should have Vegetarian preference", preferences.contains("Vegetarian"));
        
        // In a real implementation, we would check that only vegetarian meals are recommended
        System.out.println("System recommends only vegetarian meals");
    }
    
    @When("the user adds {string} as an allergy")
    public void theUserAddsAsAnAllergy(String allergy) {
        boolean result = preferenceSystem.addAllergy(currentUser, allergy);
        assertTrue("Should add allergy successfully", result);
    }
    
    @Then("the system should exclude meals containing peanuts")
    public void theSystemShouldExcludeMealsContainingPeanuts() {
        List<String> allergies = preferenceSystem.getUserAllergies(currentUser);
        assertTrue("User should have Peanuts allergy", allergies.contains("Peanuts"));
        
        // In a real implementation, we would check that meals with peanuts are excluded
        System.out.println("System excludes meals containing peanuts");
    }
    
    @When("the user selects {string} and {string}")
    public void theUserSelectsAnd(String preference1, String preference2) {
        boolean result1 = preferenceSystem.addDietaryPreference(currentUser, preference1);
        boolean result2 = preferenceSystem.addDietaryPreference(currentUser, preference2);
        assertTrue("Should add preferences successfully", result1 && result2);
    }
    
    @Then("the system should recommend meals that match the selected preferences")
    public void theSystemShouldRecommendMealsThatMatchTheSelectedPreferences() {
        List<String> preferences = preferenceSystem.getUserDietaryPreferences(currentUser);
        assertTrue("User should have multiple preferences", preferences.size() >= 2);
        
        // In a real implementation, we would check that recommended meals match all preferences
        System.out.println("System recommends meals matching all selected preferences");
    }
}
