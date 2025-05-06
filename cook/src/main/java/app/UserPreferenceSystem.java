package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserPreferenceSystem {
    private Map<String, User> userDatabase;

    public UserPreferenceSystem(Map<String, User> userDatabase) {
        this.userDatabase = userDatabase;
    }

    /**
     * Add a dietary preference for a user
     */
    public boolean addDietaryPreference(String username, String preference) {
        if (userDatabase.containsKey(username)) {
            User user = userDatabase.get(username);
            user.addDietaryPreference(preference);
            return true;
        }
        return false;
    }

    /**
     * Add an allergy for a user
     */
    public boolean addAllergy(String username, String allergy) {
        if (userDatabase.containsKey(username)) {
            User user = userDatabase.get(username);
            user.addAllergy(allergy);
            return true;
        }
        return false;
    }

    /**
     * Get all dietary preferences for a user
     */
    public List<String> getUserDietaryPreferences(String username) {
        if (userDatabase.containsKey(username)) {
            User user = userDatabase.get(username);
            return new ArrayList<>(user.getDietaryPreferences());
        }
        return new ArrayList<>();
    }

    /**
     * Get all allergies for a user
     */
    public List<String> getUserAllergies(String username) {
        if (userDatabase.containsKey(username)) {
            User user = userDatabase.get(username);
            return new ArrayList<>(user.getAllergies());
        }
        return new ArrayList<>();
    }

    /**
     * Filter meals based on user preferences and allergies
     */
    public List<Meal> getRecommendedMeals(String username, List<Meal> availableMeals) {
        if (!userDatabase.containsKey(username)) {
            return new ArrayList<>();
        }

        User user = userDatabase.get(username);
        List<String> preferences = new ArrayList<>(user.getDietaryPreferences());
        List<String> allergies = new ArrayList<>(user.getAllergies());

        return availableMeals.stream()
                .filter(meal -> {
                    // Check if meal matches any preference
                    boolean matchesPreference = preferences.isEmpty() || 
                            preferences.stream().anyMatch(meal::matchesDietaryPreference);
                    
                    // Check if meal contains any allergen
                    boolean containsAllergen = allergies.stream()
                            .anyMatch(meal::containsAllergen);
                    
                    return matchesPreference && !containsAllergen;
                })
                .collect(Collectors.toList());
    }
}
