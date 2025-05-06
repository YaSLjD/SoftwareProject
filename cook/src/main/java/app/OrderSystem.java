package app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderSystem {
    private Map<String, User> userDatabase;
    private Map<String, Meal> mealDatabase;
    private Map<String, Order> orderDatabase;

    public OrderSystem(Map<String, User> userDatabase) {
        this.userDatabase = userDatabase;
        this.mealDatabase = new HashMap<>();
        this.orderDatabase = new HashMap<>();
        
        // Initialize with some sample meals
        initializeSampleMeals();
    }
    
    private void initializeSampleMeals() {
        addMeal(new Meal("M001", "Veggie Pasta", "Delicious pasta with vegetables", 12.99));
        addMeal(new Meal("M002", "Chicken Salad", "Fresh salad with grilled chicken", 10.99));
        addMeal(new Meal("M003", "Peanut Curry", "Spicy curry with peanuts", 14.99));
        addMeal(new Meal("M004", "Gluten-Free Pizza", "Pizza with gluten-free crust", 15.99));
        addMeal(new Meal("M005", "Low-Carb Burger", "Burger with lettuce wrap instead of bun", 11.99));
    }
    
    /**
     * Add a meal to the database
     */
    public void addMeal(Meal meal) {
        mealDatabase.put(meal.getId(), meal);
    }
    
    /**
     * Get all available meals
     */
    public List<Meal> getAllMeals() {
        return new ArrayList<>(mealDatabase.values());
    }
    
    /**
     * Get a meal by ID
     */
    public Meal getMealById(String mealId) {
        return mealDatabase.get(mealId);
    }
    
    /**
     * Create a new order for a user
     */
    public Order createOrder(String username) {
        if (!userDatabase.containsKey(username)) {
            return null;
        }
        
        String orderId = "O" + UUID.randomUUID().toString().substring(0, 8);
        Order order = new Order(orderId, username);
        orderDatabase.put(orderId, order);
        
        // Add to user's order history
        userDatabase.get(username).addOrder(order);
        
        return order;
    }
    
    /**
     * Add a meal to an order
     */
    public boolean addMealToOrder(String orderId, String mealId) {
        if (!orderDatabase.containsKey(orderId) || !mealDatabase.containsKey(mealId)) {
            return false;
        }
        
        Order order = orderDatabase.get(orderId);
        Meal meal = mealDatabase.get(mealId);
        
        order.addMeal(meal);
        return true;
    }
    
    /**
     * Get all orders for a user
     */
    public List<Order> getUserOrders(String username) {
        if (!userDatabase.containsKey(username)) {
            return new ArrayList<>();
        }
        
        return userDatabase.get(username).getOrderHistory();
    }
    
    /**
     * Get order by ID
     */
    public Order getOrderById(String orderId) {
        return orderDatabase.get(orderId);
    }
    
    /**
     * Update order status
     */
    public boolean updateOrderStatus(String orderId, String status) {
        if (!orderDatabase.containsKey(orderId)) {
            return false;
        }
        
        Order order = orderDatabase.get(orderId);
        order.setStatus(status);
        return true;
    }
    
    /**
     * Generate personalized meal recommendations based on order history
     */
    public List<Meal> getPersonalizedRecommendations(String username) {
        if (!userDatabase.containsKey(username)) {
            return new ArrayList<>();
        }
        
        User user = userDatabase.get(username);
        List<Order> orderHistory = user.getOrderHistory();
        
        // If no order history, return all meals
        if (orderHistory.isEmpty()) {
            return getAllMeals();
        }
        
        // Count meal occurrences in order history
        Map<String, Integer> mealCounts = new HashMap<>();
        for (Order order : orderHistory) {
            for (Meal meal : order.getMeals()) {
                String category = meal.getDietaryCategories().isEmpty() ? 
                        "regular" : meal.getDietaryCategories().iterator().next();
                
                mealCounts.put(category, mealCounts.getOrDefault(category, 0) + 1);
            }
        }
        
        // Find most ordered category
        String favoriteCategory = mealCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("regular");
        
        // Filter meals by favorite category and allergies
        List<String> allergies = new ArrayList<>(user.getAllergies());
        
        return getAllMeals().stream()
                .filter(meal -> {
                    boolean matchesCategory = meal.getDietaryCategories().contains(favoriteCategory) ||
                            meal.getDietaryCategories().isEmpty();
                    
                    boolean containsAllergen = allergies.stream()
                            .anyMatch(meal::containsAllergen);
                    
                    return matchesCategory && !containsAllergen;
                })
                .collect(Collectors.toList());
    }
}
