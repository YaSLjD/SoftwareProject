package testing_cases.step_definition;

import app.LoginSystem;
import app.Meal;
import app.UserPreferenceSystem;
import common.logger;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
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
        currentUser = "testuser";
    }

    @Given("the user is logged into the app")
    public void theUserIsLoggedIntoTheApp() {
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
        List<Meal> meals = new ArrayList<>();

        Meal vegMeal = new Meal("1", "Salad", "Fresh salad", 5.0);
        vegMeal.addDietaryCategory("Vegetarian");

        Meal meatMeal = new Meal("2", "Steak", "Grilled steak", 15.0);
        meatMeal.addDietaryCategory("Meat");

        meals.add(vegMeal);
        meals.add(meatMeal);

        recommendedMeals = preferenceSystem.getRecommendedMeals(currentUser, meals);

        assertTrue("Vegetarian meal should be recommended", recommendedMeals.contains(vegMeal));
        assertFalse("Meat meal should not be recommended", recommendedMeals.contains(meatMeal));
        logger.info("System recommends only vegetarian meals");
    }

    @When("the user adds {string} as an allergy")
    public void theUserAddsAsAnAllergy(String allergy) {
        boolean result = preferenceSystem.addAllergy(currentUser, allergy);
        assertTrue("Should add allergy successfully", result);
    }

    @Then("the system should exclude meals containing peanuts")
    public void theSystemShouldExcludeMealsContainingPeanuts() {
        List<Meal> meals = new ArrayList<>();

        Meal mealWithPeanuts = new Meal("3", "Pad Thai", "Noodles with peanuts", 8.0);
        mealWithPeanuts.addAllergen("Peanuts");
        mealWithPeanuts.addDietaryCategory("Vegetarian");

        Meal safeMeal = new Meal("4", "Fruit Salad", "Mixed fruits", 4.0);
        safeMeal.addDietaryCategory("Vegetarian");

        meals.add(mealWithPeanuts);
        meals.add(safeMeal);

        recommendedMeals = preferenceSystem.getRecommendedMeals(currentUser, meals);

        assertFalse("Meal with peanuts should be excluded", recommendedMeals.contains(mealWithPeanuts));
        assertTrue("Safe meal should be included", recommendedMeals.contains(safeMeal));
        logger.info("System excludes meals containing peanuts");
    }

    @When("the user selects {string} and {string}")
    public void theUserSelectsAnd(String preference1, String preference2) {
        boolean result1 = preferenceSystem.addDietaryPreference(currentUser, preference1);
        boolean result2 = preferenceSystem.addDietaryPreference(currentUser, preference2);
        assertTrue("Should add preferences successfully", result1 && result2);
    }

    @Then("the system should recommend meals that match the selected preferences")
public void theSystemShouldRecommendMealsThatMatchTheSelectedPreferences() {
    List<Meal> meals = new ArrayList<>();

    Meal veganMeal = new Meal("5", "Vegan Bowl", "Plant-based meal", 7.0);
    veganMeal.addDietaryCategory("Vegan");

    Meal glutenFreeMeal = new Meal("6", "Grilled Chicken", "No gluten", 9.0);
    glutenFreeMeal.addDietaryCategory("Gluten-Free");

    Meal unrelatedMeal = new Meal("7", "Spaghetti", "Contains wheat", 6.0);
    unrelatedMeal.addDietaryCategory("High-Carb");

    meals.add(veganMeal);
    meals.add(glutenFreeMeal);
    meals.add(unrelatedMeal);

    recommendedMeals = preferenceSystem.getRecommendedMeals(currentUser, meals);

    boolean hasVegan = recommendedMeals.contains(veganMeal);
    boolean hasGlutenFree = recommendedMeals.contains(glutenFreeMeal);

    assertTrue("Should recommend vegan or gluten-free meal", hasVegan || hasGlutenFree);
    assertFalse("Should not recommend unrelated meal", recommendedMeals.contains(unrelatedMeal));
    logger.info("System recommends meals matching all selected preferences");
}

}
