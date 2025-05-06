package app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
    private String username;
    private String password;
    private String role; // "customer", "chef", "admin"
    private Set<String> dietaryPreferences;
    private Set<String> allergies;
    private List<Order> orderHistory;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.dietaryPreferences = new HashSet<>();
        this.allergies = new HashSet<>();
        this.orderHistory = new ArrayList<>();
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<String> getDietaryPreferences() {
        return dietaryPreferences;
    }

    public void addDietaryPreference(String preference) {
        this.dietaryPreferences.add(preference);
    }

    public void removeDietaryPreference(String preference) {
        this.dietaryPreferences.remove(preference);
    }

    public Set<String> getAllergies() {
        return allergies;
    }

    public void addAllergy(String allergy) {
        this.allergies.add(allergy);
    }

    public void removeAllergy(String allergy) {
        this.allergies.remove(allergy);
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public void addOrder(Order order) {
        this.orderHistory.add(order);
    }

    public boolean hasRole(String role) {
        return this.role.equals(role);
    }
}
