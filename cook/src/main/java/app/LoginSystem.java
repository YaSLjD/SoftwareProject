package app;

import java.util.HashMap;
import java.util.Map;

public class LoginSystem {

    private Map<String, User> userDatabase; // Store user data
    
    /**
     * Get the user database
     */
    public Map<String, User> getUserDatabase() {
        return userDatabase;
    }

    public LoginSystem() {
        userDatabase = new HashMap<>();
        // Add default users
        userDatabase.put("testuser", new User("testuser", "password123", "customer"));
        userDatabase.put("admin", new User("admin", "adminpass", "admin"));
        userDatabase.put("chef", new User("chef", "chefpass", "chef"));
        
        // Initialize some sample data
        initializeSampleData(); }
    
    
    private void initializeSampleData() {
        // Add dietary preferences for test user
        User testUser = userDatabase.get("testuser");
        testUser.addDietaryPreference("Vegetarian");
        testUser.addAllergy("Peanuts");
        
        // Create some sample meals
        Meal veggiePasta = new Meal("M001", "Veggie Pasta", "Delicious pasta with vegetables", 12.99);
        veggiePasta.addDietaryCategory("Vegetarian");
        veggiePasta.addIngredient("Pasta");
        veggiePasta.addIngredient("Tomato");
        veggiePasta.addIngredient("Basil");
        
        Meal chickenSalad = new Meal("M002", "Chicken Salad", "Fresh salad with grilled chicken", 10.99);
        chickenSalad.addIngredient("Chicken");
        chickenSalad.addIngredient("Lettuce");
        chickenSalad.addIngredient("Tomato");
        
        Meal peanutCurry = new Meal("M003", "Peanut Curry", "Spicy curry with peanuts", 14.99);
        peanutCurry.addAllergen("Peanuts");
        peanutCurry.addIngredient("Rice");
        peanutCurry.addIngredient("Curry");
        peanutCurry.addIngredient("Peanuts");
        
        // Create a sample order
        Order order = new Order("O001", "testuser");
        order.addMeal(veggiePasta);
        order.setStatus("delivered");
        
        // Add order to user history
        testUser.addOrder(order);
    }

    // Simulate user navigation to login page
    public void navigateToLoginPage() {
        System.out.println("Navigating to the login page...");
    }

    // Perform login operation
    public String login(String username, String password) {
        if (userDatabase.containsKey(username)) {
            User user = userDatabase.get(username);
            if (user.getPassword().equals(password)) {
                return "Welcome " + username;
            } else {
                return "Invalid username or password";
            }
        } else {
            return "Invalid username or password";
        }
    }

    // Verify user redirection to homepage
    public void verifyHomepageRedirection(String message) {
        if (message.startsWith("Welcome")) {
            System.out.println("User redirected to the homepage successfully.");
        } else {
            System.out.println("User remains on the login page.");
        }
    }

    // Verify welcome message display
    public void verifyWelcomeMessage(String message) {
        System.out.println("Displayed message: " + message);
    }

    // Verify error message display on login failure
    public void verifyErrorMessage(String message) {
        System.out.println("Error message: " + message);
    }
}