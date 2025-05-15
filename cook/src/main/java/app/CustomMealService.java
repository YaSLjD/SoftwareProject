package app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class CustomMealService {
    
    private Inventory inventory;
    private Map<String, Set<String>> incompatibleIngredients;
    
    public CustomMealService(Inventory inventory) {
        this.inventory = inventory;
        this.incompatibleIngredients = new HashMap<>();
        initializeIncompatibleIngredients();
        addTestIngredientsToInventory();
    }
    
    
    private void addTestIngredientsToInventory() {
        // Check if these test ingredients already exist
        boolean chickenExists = false;
        boolean brownRiceExists = false;
        boolean shrimpExists = false;
        
        for (Ingredient ingredient : inventory.getAllIngredients()) {
            if (ingredient.getName().equalsIgnoreCase("Chicken")) chickenExists = true;
            if (ingredient.getName().equalsIgnoreCase("Brown Rice")) brownRiceExists = true;
            if (ingredient.getName().equalsIgnoreCase("Shrimp")) shrimpExists = true;
        }
        
        // Add test ingredients if they don't exist
        if (!chickenExists) {
            inventory.addIngredient(new Ingredient("ING006", "Chicken", "kg", 10.0, 2.0, "SUP001"));
        }
        
        if (!brownRiceExists) {
            inventory.addIngredient(new Ingredient("ING007", "Brown Rice", "kg", 15.0, 3.0, "SUP002"));
        }
        
        if (!shrimpExists) {
            inventory.addIngredient(new Ingredient("ING008", "Shrimp", "kg", 5.0, 1.0, "SUP001"));
        }
    }
    
   
    private void initializeIncompatibleIngredients() {
        Set<String> chocolateSauceIncompatible = new HashSet<>();
        chocolateSauceIncompatible.add("Shrimp");
        chocolateSauceIncompatible.add("Fish");
        chocolateSauceIncompatible.add("Crab");
        chocolateSauceIncompatible.add("Lobster");
        incompatibleIngredients.put("Chocolate Sauce", chocolateSauceIncompatible);
        
        Set<String> mintIncompatible = new HashSet<>();
        mintIncompatible.add("Garlic");
        mintIncompatible.add("Blue Cheese");
        incompatibleIngredients.put("Mint", mintIncompatible);
    }
    
    
    public Meal createCustomizableMeal(String mealId, String name, String description, double basePrice) {
        return new Meal(mealId, name, description, basePrice);
    }
    
    
    public boolean addIngredientToMeal(Meal meal, String ingredientName) {
        // Check if ingredient exists in inventory
        boolean ingredientExists = false;
        for (Ingredient ingredient : inventory.getAllIngredients()) {
            if (ingredient.getName().equalsIgnoreCase(ingredientName)) {
                ingredientExists = true;
                break;
            }
        }
        
        if (!ingredientExists) {
            return false; // Ingredient not in inventory
        }
        
        // Check if adding this ingredient would create an incompatible combination
        if (!isCompatibleWithMeal(meal, ingredientName)) {
            return false; // Incompatible ingredient
        }
        
        // If all checks pass, add the ingredient
        meal.addIngredient(ingredientName);
        return true;
    }
    
   
    public boolean removeIngredientFromMeal(Meal meal, String ingredientName) {
        if (meal.getIngredients().contains(ingredientName)) {
            meal.removeIngredient(ingredientName);
            return true;
        }
        return false;
    }
    
    
    public boolean isCompatibleWithMeal(Meal meal, String newIngredient) {
        // If this ingredient has known incompatibilities, check each one
        if (incompatibleIngredients.containsKey(newIngredient)) {
            Set<String> incompatibles = incompatibleIngredients.get(newIngredient);
            for (String ingredient : meal.getIngredients()) {
                if (incompatibles.contains(ingredient)) {
                    return false; // Found an incompatible combination
                }
            }
        }
        
        for (String existingIngredient : meal.getIngredients()) {
            if (incompatibleIngredients.containsKey(existingIngredient)) {
                Set<String> incompatibles = incompatibleIngredients.get(existingIngredient);
                if (incompatibles.contains(newIngredient)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    
    public boolean isIngredientAvailable(String ingredientName) {
        // Search for the ingredient in inventory
        for (Ingredient ingredient : inventory.getAllIngredients()) {
            if (ingredient.getName().equalsIgnoreCase(ingredientName) && ingredient.getQuantity() > 0) {
                return true;
            }
        }
        return false;
    }
    
    
    public double calculateCustomizedPrice(Meal meal, Set<String> addedIngredients, Set<String> removedIngredients) {
        double basePrice = meal.getPrice();
        double adjustedPrice = basePrice;
        
        adjustedPrice += addedIngredients.size() * 1.50;
        
        adjustedPrice -= removedIngredients.size() * 0.75;
        
        return Math.max(adjustedPrice, basePrice * 0.5);
    }
}
