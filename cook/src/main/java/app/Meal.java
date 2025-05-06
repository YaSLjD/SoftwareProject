package app;

import java.util.HashSet;
import java.util.Set;

public class Meal {
    private String id;
    private String name;
    private String description;
    private double price;
    private Set<String> ingredients;
    private Set<String> allergens;
    private Set<String> dietaryCategories; // e.g., "vegetarian", "vegan", "gluten-free", "low-carb"

    public Meal(String id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.ingredients = new HashSet<>();
        this.allergens = new HashSet<>();
        this.dietaryCategories = new HashSet<>();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Set<String> getIngredients() {
        return ingredients;
    }

    public void addIngredient(String ingredient) {
        this.ingredients.add(ingredient);
    }

    public void removeIngredient(String ingredient) {
        this.ingredients.remove(ingredient);
    }

    public Set<String> getAllergens() {
        return allergens;
    }

    public void addAllergen(String allergen) {
        this.allergens.add(allergen);
    }

    public void removeAllergen(String allergen) {
        this.allergens.remove(allergen);
    }

    public Set<String> getDietaryCategories() {
        return dietaryCategories;
    }

    public void addDietaryCategory(String category) {
        this.dietaryCategories.add(category);
    }

    public void removeDietaryCategory(String category) {
        this.dietaryCategories.remove(category);
    }

    public boolean containsAllergen(String allergen) {
        return this.allergens.contains(allergen);
    }

    public boolean matchesDietaryPreference(String preference) {
        return this.dietaryCategories.contains(preference);
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", dietaryCategories=" + dietaryCategories +
                '}';
    }
}
