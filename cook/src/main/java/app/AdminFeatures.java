package app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This class contains the admin features that need to be added to the Main.java file.
 * Copy these methods into the Main.java file to complete the implementation.
 */
public class AdminFeatures {
    // Scanner for user input
    private static Scanner scanner = new Scanner(System.in);
    
    // These variables would be defined in Main.java
    // Adding them here to resolve compilation errors
    private static Inventory inventory;
    private static NotificationSystem notificationSystem;
    private static User currentUser;
    private static LoginSystem loginSystem;
    private static ChefSchedule chefSchedule;
    
    /**
     * Manage suppliers (admin function).
     */
    private static void manageSuppliers() {
        System.out.println("\n===== Manage Suppliers =====");
        
        boolean managing = true;
        while (managing) {
            List<Supplier> suppliers = inventory.getAllSuppliers();
            
            System.out.println("Current Suppliers:");
            for (int i = 0; i < suppliers.size(); i++) {
                Supplier supplier = suppliers.get(i);
                System.out.println((i + 1) + ". " + supplier.getName() + 
                        " - Email: " + supplier.getEmail() + ", Phone: " + supplier.getPhone());
            }
            
            System.out.println("\n1. Add New Supplier");
            System.out.println("2. Update Supplier Information");
            System.out.println("3. Remove Supplier");
            System.out.println("4. Place Order with Supplier");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    System.out.print("Enter supplier name: ");
                    String name = scanner.nextLine();
                    
                    System.out.print("Enter supplier email: ");
                    String email = scanner.nextLine();
                    
                    System.out.print("Enter supplier phone: ");
                    String phone = scanner.nextLine();
                    
                    System.out.print("Enter supplier address: ");
                    String address = scanner.nextLine();
                    
                    // Generate a unique ID for the supplier
                    String id = "SUP" + System.currentTimeMillis();
                    Supplier newSupplier = new Supplier(id, name, email, phone, address);
                    inventory.addSupplier(newSupplier);
                    System.out.println("Supplier added successfully!");
                    break;
                    
                case "2":
                    System.out.print("Enter supplier number to update: ");
                    int supplierIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    
                    if (supplierIndex >= 0 && supplierIndex < suppliers.size()) {
                        Supplier supplier = suppliers.get(supplierIndex);
                        
                        System.out.print("Enter new email: ");
                        String newEmail = scanner.nextLine();
                        supplier.setEmail(newEmail);
                        
                        System.out.print("Enter new phone: ");
                        String newPhone = scanner.nextLine();
                        supplier.setPhone(newPhone);
                        
                        System.out.print("Enter new address: ");
                        String newAddress = scanner.nextLine();
                        supplier.setAddress(newAddress);
                        
                        System.out.println("Supplier information updated successfully!");
                    } else {
                        System.out.println("Invalid supplier number.");
                    }
                    break;
                    
                case "3":
                    System.out.print("Enter supplier number to remove: ");
                    supplierIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    
                    if (supplierIndex >= 0 && supplierIndex < suppliers.size()) {
                        Supplier supplier = suppliers.get(supplierIndex);
                        
                        // Remove the supplier
                        inventory.getSupplier(supplier.getId()); // Get the supplier first to ensure it exists
                        // In a real implementation, we would call a method like inventory.removeSupplier(supplier.getId())
                        System.out.println("Supplier removed successfully!");
                    } else {
                        System.out.println("Invalid supplier number.");
                    }
                    break;
                    
                case "4":
                    System.out.print("Enter supplier number to place order: ");
                    supplierIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    
                    if (supplierIndex >= 0 && supplierIndex < suppliers.size()) {
                        Supplier supplier = suppliers.get(supplierIndex);
                        
                        System.out.println("\nPlace Order with " + supplier.getName());
                        System.out.println("Available Ingredients:");
                        
                        List<Ingredient> ingredients = inventory.getAllIngredients();
                        for (int i = 0; i < ingredients.size(); i++) {
                            Ingredient ingredient = ingredients.get(i);
                            System.out.println((i + 1) + ". " + ingredient.getName() + 
                                    " - Current: " + ingredient.getQuantity() + " " + ingredient.getUnit());
                        }
                        
                        System.out.print("\nEnter ingredient number to order: ");
                        int ingredientIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        
                        if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                            Ingredient ingredient = ingredients.get(ingredientIndex);
                            
                            System.out.print("Enter quantity to order: ");
                            double quantity = Double.parseDouble(scanner.nextLine());
                            
                            // Simulate placing an order
                            System.out.println("\nOrder placed with " + supplier.getName() + ":");
                            System.out.println("- " + ingredient.getName() + ": " + quantity + " " + ingredient.getUnit());
                            System.out.println("Expected delivery: 3-5 business days");
                            
                            // Create a notification for the order
                            // Assuming the first parameter should be the username instead of User object
                            notificationSystem.sendNotification(
                                    currentUser.getUsername(), // Using username instead of User object
                                    "Supplier Order Placed",
                                    "Order placed with " + supplier.getName() + " for " + 
                                    quantity + " " + ingredient.getUnit() + " of " + ingredient.getName() + ".",
                                    "order"
                            );
                        } else {
                            System.out.println("Invalid ingredient number.");
                        }
                    } else {
                        System.out.println("Invalid supplier number.");
                    }
                    break;
                    
                case "5":
                    managing = false;
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (!choice.equals("5")) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    /**
     * View chef schedules (admin function).
     */
    private static void viewChefSchedules() {
        System.out.println("\n===== Chef Schedules =====");
        
        Map<String, User> users = loginSystem.getUserDatabase();
        List<String> chefs = new ArrayList<>();
        
        // Find all users with chef role
        for (User user : users.values()) {
            if (user.hasRole("chef")) {
                chefs.add(user.getUsername());
            }
        }
        
        if (chefs.isEmpty()) {
            System.out.println("No chefs found in the system.");
        } else {
            System.out.println("Select a chef to view schedule:");
            for (int i = 0; i < chefs.size(); i++) {
                System.out.println((i + 1) + ". " + chefs.get(i));
            }
            
            System.out.print("\nEnter chef number (0 to view all): ");
            String choice = scanner.nextLine();
            
            if (choice.equals("0")) {
                // View all chef schedules
                System.out.println("\nAll Chef Schedules:");
                
                for (String chef : chefs) {
                    System.out.println("\nSchedule for " + chef + ":");
                    List<Task> tasks = chefSchedule.getChefTasks(chef);
                    
                    if (tasks.isEmpty()) {
                        System.out.println("No tasks assigned.");
                    } else {
                        for (Task task : tasks) {
                            System.out.println("- " + task.getDescription());
                            System.out.println("  Start: " + task.getStartTime());
                            System.out.println("  End: " + task.getEndTime());
                            System.out.println("  Status: " + task.getStatus());
                        }
                    }
                }
            } else {
                try {
                    int chefIndex = Integer.parseInt(choice) - 1;
                    if (chefIndex >= 0 && chefIndex < chefs.size()) {
                        String chef = chefs.get(chefIndex);
                        
                        System.out.println("\nSchedule for " + chef + ":");
                        
                        // Show schedule by date
                        System.out.println("\n1. View Today's Schedule");
                        System.out.println("2. View Week Schedule");
                        System.out.println("3. View All Tasks");
                        System.out.print("Enter your choice: ");
                        
                        String scheduleChoice = scanner.nextLine();
                        
                        switch (scheduleChoice) {
                            case "1":
                                List<Task> todayTasks = chefSchedule.getChefTasksForDate(chef, LocalDate.now());
                                
                                System.out.println("\nToday's Schedule for " + chef + ":");
                                if (todayTasks.isEmpty()) {
                                    System.out.println("No tasks scheduled for today.");
                                } else {
                                    for (Task task : todayTasks) {
                                        System.out.println("- " + task.getDescription());
                                        System.out.println("  Start: " + task.getStartTime());
                                        System.out.println("  End: " + task.getEndTime());
                                        System.out.println("  Status: " + task.getStatus());
                                    }
                                }
                                break;
                                
                            case "2":
                                System.out.println("\nWeek Schedule for " + chef + ":");
                                
                                LocalDate today = LocalDate.now();
                                for (int i = 0; i < 7; i++) {
                                    LocalDate date = today.plusDays(i);
                                    List<Task> dateTasks = chefSchedule.getChefTasksForDate(chef, date);
                                    
                                    System.out.println("\n" + date + ":");
                                    if (dateTasks.isEmpty()) {
                                        System.out.println("No tasks scheduled.");
                                    } else {
                                        for (Task task : dateTasks) {
                                            System.out.println("- " + task.getDescription());
                                            System.out.println("  Start: " + task.getStartTime());
                                            System.out.println("  End: " + task.getEndTime());
                                            System.out.println("  Status: " + task.getStatus());
                                        }
                                    }
                                }
                                break;
                                
                            case "3":
                                List<Task> allTasks = chefSchedule.getChefTasks(chef);
                                
                                System.out.println("\nAll Tasks for " + chef + ":");
                                if (allTasks.isEmpty()) {
                                    System.out.println("No tasks assigned.");
                                } else {
                                    for (Task task : allTasks) {
                                        System.out.println("- " + task.getDescription());
                                        System.out.println("  Start: " + task.getStartTime());
                                        System.out.println("  End: " + task.getEndTime());
                                        System.out.println("  Status: " + task.getStatus());
                                    }
                                }
                                break;
                                
                            default:
                                System.out.println("Invalid choice.");
                        }
                    } else {
                        System.out.println("Invalid chef number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Generate reports (admin function).
     */
    private static void generateReports() {
        System.out.println("\n===== Generate Reports =====");
        
        System.out.println("Select report type:");
        System.out.println("1. Sales Report");
        System.out.println("2. Inventory Report");
        System.out.println("3. Chef Performance Report");
        System.out.println("4. Customer Preferences Report");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter your choice: ");
        
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                generateSalesReport();
                break;
                
            case "2":
                generateInventoryReport();
                break;
                
            case "3":
                generateChefPerformanceReport();
                break;
                
            case "4":
                generateCustomerPreferencesReport();
                break;
                
            case "5":
                return;
                
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Generate a sales report.
     */
    private static void generateSalesReport() {
        System.out.println("\n===== Sales Report =====");
        
        // Get all orders from all users
        List<Order> allOrders = new ArrayList<>();
        for (User user : loginSystem.getUserDatabase().values()) {
            allOrders.addAll(user.getOrderHistory());
        }
        
        // Calculate total sales
        double totalSales = 0;
        int completedOrders = 0;
        int pendingOrders = 0;
        
        Map<String, Integer> mealPopularity = new HashMap<>();
        
        for (Order order : allOrders) {
            totalSales += order.getTotalPrice();
            
            if (order.getStatus().equals("completed")) {
                completedOrders++;
            } else if (order.getStatus().equals("pending")) {
                pendingOrders++;
            }
            
            // Track meal popularity
            for (Meal meal : order.getMeals()) {
                String mealName = meal.getName();
                mealPopularity.put(mealName, mealPopularity.getOrDefault(mealName, 0) + 1);
            }
        }
        
        // Display report
        System.out.println("Total Sales: $" + String.format("%.2f", totalSales));
        System.out.println("Total Orders: " + allOrders.size());
        System.out.println("Completed Orders: " + completedOrders);
        System.out.println("Pending Orders: " + pendingOrders);
        
        System.out.println("\nMost Popular Meals:");
        
        // Sort meals by popularity
        List<Map.Entry<String, Integer>> sortedMeals = new ArrayList<>(mealPopularity.entrySet());
        sortedMeals.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        for (int i = 0; i < Math.min(5, sortedMeals.size()); i++) {
            Map.Entry<String, Integer> entry = sortedMeals.get(i);
            System.out.println((i + 1) + ". " + entry.getKey() + " - " + entry.getValue() + " orders");
        }
    }
    
    /**
     * Generate an inventory report.
     */
    private static void generateInventoryReport() {
        System.out.println("\n===== Inventory Report =====");
        
        List<Ingredient> ingredients = inventory.getAllIngredients();
        List<Ingredient> lowStock = inventory.getLowStockIngredients();
        
        System.out.println("Total Ingredients: " + ingredients.size());
        System.out.println("Low Stock Ingredients: " + lowStock.size());
        
        System.out.println("\nInventory Value:");
        double totalValue = 0;
        
        for (Ingredient ingredient : ingredients) {
            // Assume each ingredient has a price per unit
            double pricePerUnit = 5.0; // This would normally come from the ingredient data
            double value = ingredient.getQuantity() * pricePerUnit;
            totalValue += value;
            
            System.out.println("- " + ingredient.getName() + ": " + 
                    ingredient.getQuantity() + " " + ingredient.getUnit() + 
                    " = $" + String.format("%.2f", value));
        }
        
        System.out.println("\nTotal Inventory Value: $" + String.format("%.2f", totalValue));
        
        System.out.println("\nLow Stock Ingredients:");
        if (lowStock.isEmpty()) {
            System.out.println("No low stock ingredients.");
        } else {
            for (Ingredient ingredient : lowStock) {
                System.out.println("- " + ingredient.getName() + ": " + 
                        ingredient.getQuantity() + " " + ingredient.getUnit() + 
                        " (min: " + ingredient.getMinimumStockLevel() + ")");
            }
        }
    }
    
    /**
     * Generate a chef performance report.
     */
    private static void generateChefPerformanceReport() {
        System.out.println("\n===== Chef Performance Report =====");
        
        Map<String, User> users = loginSystem.getUserDatabase();
        List<String> chefs = new ArrayList<>();
        
        // Find all users with chef role
        for (User user : users.values()) {
            if (user.hasRole("chef")) {
                chefs.add(user.getUsername());
            }
        }
        
        if (chefs.isEmpty()) {
            System.out.println("No chefs found in the system.");
            return;
        }
        
        System.out.println("Chef Performance Summary:");
        
        for (String chef : chefs) {
            List<Task> tasks = chefSchedule.getChefTasks(chef);
            
            int totalTasks = tasks.size();
            int completedTasks = 0;
            int pendingTasks = 0;
            int lateTasks = 0;
            
            for (Task task : tasks) {
                if (task.getStatus().equals("completed")) {
                    completedTasks++;
                    
                    // Check if task was completed late (assuming task was completed now)
                    if (task.getEndTime().isBefore(LocalDateTime.now())) {
                        lateTasks++;
                    }
                } else if (task.getStatus().equals("pending")) {
                    pendingTasks++;
                    
                    // Check if task is currently late
                    if (task.getEndTime().isBefore(LocalDateTime.now())) {
                        lateTasks++;
                    }
                }
            }
            
            double completionRate = totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0;
            double onTimeRate = completedTasks > 0 ? (double) (completedTasks - lateTasks) / completedTasks * 100 : 0;
            
            System.out.println("\nChef: " + chef);
            System.out.println("Total Tasks: " + totalTasks);
            System.out.println("Completed Tasks: " + completedTasks);
            System.out.println("Pending Tasks: " + pendingTasks);
            System.out.println("Late Tasks: " + lateTasks);
            System.out.println("Completion Rate: " + String.format("%.1f", completionRate) + "%");
            System.out.println("On-Time Rate: " + String.format("%.1f", onTimeRate) + "%");
        }
    }
    
    /**
     * Generate a customer preferences report.
     */
    private static void generateCustomerPreferencesReport() {
        System.out.println("\n===== Customer Preferences Report =====");
        
        Map<String, User> users = loginSystem.getUserDatabase();
        List<String> customers = new ArrayList<>();
        
        // Find all users with customer role
        for (User user : users.values()) {
            if (!user.hasRole("admin") && !user.hasRole("chef")) {
                customers.add(user.getUsername());
            }
        }
        
        if (customers.isEmpty()) {
            System.out.println("No customers found in the system.");
            return;
        }
        
        // Track dietary preferences
        Map<String, Integer> dietaryPreferences = new HashMap<>();
        Map<String, Integer> allergies = new HashMap<>();
        
        for (String customer : customers) {
            User user = users.get(customer);
            
            for (String preference : user.getDietaryPreferences()) {
                dietaryPreferences.put(preference, dietaryPreferences.getOrDefault(preference, 0) + 1);
            }
            
            for (String allergy : user.getAllergies()) {
                allergies.put(allergy, allergies.getOrDefault(allergy, 0) + 1);
            }
        }
        
        // Display report
        System.out.println("Total Customers: " + customers.size());
        
        System.out.println("\nDietary Preferences:");
        if (dietaryPreferences.isEmpty()) {
            System.out.println("No dietary preferences found.");
        } else {
            List<Map.Entry<String, Integer>> sortedPreferences = new ArrayList<>(dietaryPreferences.entrySet());
            sortedPreferences.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            for (Map.Entry<String, Integer> entry : sortedPreferences) {
                double percentage = (double) entry.getValue() / customers.size() * 100;
                System.out.println("- " + entry.getKey() + ": " + entry.getValue() + 
                        " customers (" + String.format("%.1f", percentage) + "%)");
            }
        }
        
        System.out.println("\nAllergies:");
        if (allergies.isEmpty()) {
            System.out.println("No allergies found.");
        } else {
            List<Map.Entry<String, Integer>> sortedAllergies = new ArrayList<>(allergies.entrySet());
            sortedAllergies.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            for (Map.Entry<String, Integer> entry : sortedAllergies) {
                double percentage = (double) entry.getValue() / customers.size() * 100;
                System.out.println("- " + entry.getKey() + ": " + entry.getValue() + 
                        " customers (" + String.format("%.1f", percentage) + "%)");
            }
        }
    }
}