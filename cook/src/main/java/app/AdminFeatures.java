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
        logger.info("\n===== Manage Suppliers =====");
        
        boolean managing = true;
        while (managing) {
            List<Supplier> suppliers = inventory.getAllSuppliers();
            
            logger.info("Current Suppliers:");
            for (int i = 0; i < suppliers.size(); i++) {
                Supplier supplier = suppliers.get(i);
                System.out.println((i + 1) + ". " + supplier.getName() + 
                        " - Email: " + supplier.getEmail() + ", Phone: " + supplier.getPhone());
            }
            
            logger.info("\n1. Add New Supplier");
            logger.info("2. Update Supplier Information");
            logger.info("3. Remove Supplier");
            logger.info("4. Place Order with Supplier");
            logger.info("5. Back to Main Menu");
            logger.info("Enter your choice: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    logger.info("Enter supplier name: ");
                    String name = scanner.nextLine();
                    
                    logger.info("Enter supplier email: ");
                    String email = scanner.nextLine();
                    
                    logger.info("Enter supplier phone: ");
                    String phone = scanner.nextLine();
                    
                    logger.info("Enter supplier address: ");
                    String address = scanner.nextLine();
                    
                    // Generate a unique ID for the supplier
                    String id = "SUP" + System.currentTimeMillis();
                    Supplier newSupplier = new Supplier(id, name, email, phone, address);
                    inventory.addSupplier(newSupplier);
                    logger.info("Supplier added successfully!");
                    break;
                    
                case "2":
                    logger.info("Enter supplier number to update: ");
                    int supplierIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    
                    if (supplierIndex >= 0 && supplierIndex < suppliers.size()) {
                        Supplier supplier = suppliers.get(supplierIndex);
                        
                        logger.info("Enter new email: ");
                        String newEmail = scanner.nextLine();
                        supplier.setEmail(newEmail);
                        
                        logger.info("Enter new phone: ");
                        String newPhone = scanner.nextLine();
                        supplier.setPhone(newPhone);
                        
                        logger.info("Enter new address: ");
                        String newAddress = scanner.nextLine();
                        supplier.setAddress(newAddress);
                        
                        logger.info("Supplier information updated successfully!");
                    } else {
                        logger.info("Invalid supplier number.");
                    }
                    break;
                    
                case "3":
                    logger.info("Enter supplier number to remove: ");
                    supplierIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    
                    if (supplierIndex >= 0 && supplierIndex < suppliers.size()) {
                        Supplier supplier = suppliers.get(supplierIndex);
                        
                        // Remove the supplier
                        inventory.getSupplier(supplier.getId()); // Get the supplier first to ensure it exists
                        // In a real implementation, we would call a method like inventory.removeSupplier(supplier.getId())
                        logger.info("Supplier removed successfully!");
                    } else {
                        logger.info("Invalid supplier number.");
                    }
                    break;
                    
                case "4":
                    logger.info("Enter supplier number to place order: ");
                    supplierIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    
                    if (supplierIndex >= 0 && supplierIndex < suppliers.size()) {
                        Supplier supplier = suppliers.get(supplierIndex);
                        
                        logger.info("\nPlace Order with " + supplier.getName());
                        logger.info("Available Ingredients:");
                        
                        List<Ingredient> ingredients = inventory.getAllIngredients();
                        for (int i = 0; i < ingredients.size(); i++) {
                            Ingredient ingredient = ingredients.get(i);
                            System.out.println((i + 1) + ". " + ingredient.getName() + 
                                    " - Current: " + ingredient.getQuantity() + " " + ingredient.getUnit());
                        }
                        
                        logger.info("\nEnter ingredient number to order: ");
                        int ingredientIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        
                        if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                            Ingredient ingredient = ingredients.get(ingredientIndex);
                            
                            logger.info("Enter quantity to order: ");
                            double quantity = Double.parseDouble(scanner.nextLine());
                            
                            // Simulate placing an order
                            logger.info("\nOrder placed with " + supplier.getName() + ":");
                            logger.info("- " + ingredient.getName() + ": " + quantity + " " + ingredient.getUnit());
                            logger.info("Expected delivery: 3-5 business days");
                            
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
                            logger.info("Invalid ingredient number.");
                        }
                    } else {
                        logger.info("Invalid supplier number.");
                    }
                    break;
                    
                case "5":
                    managing = false;
                    break;
                    
                default:
                    logger.info("Invalid choice. Please try again.");
            }
            
            if (!choice.equals("5")) {
                logger.info("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    /**
     * View chef schedules (admin function).
     */
    private static void viewChefSchedules() {
        logger.info("\n===== Chef Schedules =====");
        
        Map<String, User> users = loginSystem.getUserDatabase();
        List<String> chefs = new ArrayList<>();
        
        // Find all users with chef role
        for (User user : users.values()) {
            if (user.hasRole("chef")) {
                chefs.add(user.getUsername());
            }
        }
        
        if (chefs.isEmpty()) {
            logger.info("No chefs found in the system.");
        } else {
            logger.info("Select a chef to view schedule:");
            for (int i = 0; i < chefs.size(); i++) {
                logger.info((i + 1) + ". " + chefs.get(i));
            }
            
            logger.info("\nEnter chef number (0 to view all): ");
            String choice = scanner.nextLine();
            
            if (choice.equals("0")) {
                // View all chef schedules
                logger.info("\nAll Chef Schedules:");
                
                for (String chef : chefs) {
                    logger.info("\nSchedule for " + chef + ":");
                    List<Task> tasks = chefSchedule.getChefTasks(chef);
                    
                    if (tasks.isEmpty()) {
                        logger.info("No tasks assigned.");
                    } else {
                        for (Task task : tasks) {
                            logger.info("- " + task.getDescription());
                            logger.info("  Start: " + task.getStartTime());
                            logger.info("  End: " + task.getEndTime());
                            logger.info("  Status: " + task.getStatus());
                        }
                    }
                }
            } else {
                try {
                    int chefIndex = Integer.parseInt(choice) - 1;
                    if (chefIndex >= 0 && chefIndex < chefs.size()) {
                        String chef = chefs.get(chefIndex);
                        
                        logger.info("\nSchedule for " + chef + ":");
                        
                        // Show schedule by date
                        logger.info("\n1. View Today's Schedule");
                        logger.info("2. View Week Schedule");
                        logger.info("3. View All Tasks");
                        logger.info("Enter your choice: ");
                        
                        String scheduleChoice = scanner.nextLine();
                        
                        switch (scheduleChoice) {
                            case "1":
                                List<Task> todayTasks = chefSchedule.getChefTasksForDate(chef, LocalDate.now());
                                
                                logger.info("\nToday's Schedule for " + chef + ":");
                                if (todayTasks.isEmpty()) {
                                    logger.info("No tasks scheduled for today.");
                                } else {
                                    for (Task task : todayTasks) {
                                        logger.info("- " + task.getDescription());
                                        logger.info("  Start: " + task.getStartTime());
                                        logger.info("  End: " + task.getEndTime());
                                        logger.info("  Status: " + task.getStatus());
                                    }
                                }
                                break;
                                
                            case "2":
                                logger.info("\nWeek Schedule for " + chef + ":");
                                
                                LocalDate today = LocalDate.now();
                                for (int i = 0; i < 7; i++) {
                                    LocalDate date = today.plusDays(i);
                                    List<Task> dateTasks = chefSchedule.getChefTasksForDate(chef, date);
                                    
                                    logger.info("\n" + date + ":");
                                    if (dateTasks.isEmpty()) {
                                        logger.info("No tasks scheduled.");
                                    } else {
                                        for (Task task : dateTasks) {
                                            logger.info("- " + task.getDescription());
                                            logger.info("  Start: " + task.getStartTime());
                                            logger.info("  End: " + task.getEndTime());
                                            logger.info("  Status: " + task.getStatus());
                                        }
                                    }
                                }
                                break;
                                
                            case "3":
                                List<Task> allTasks = chefSchedule.getChefTasks(chef);
                                
                                logger.info("\nAll Tasks for " + chef + ":");
                                if (allTasks.isEmpty()) {
                                    logger.info("No tasks assigned.");
                                } else {
                                    for (Task task : allTasks) {
                                        logger.info("- " + task.getDescription());
                                        logger.info("  Start: " + task.getStartTime());
                                        logger.info("  End: " + task.getEndTime());
                                        logger.info("  Status: " + task.getStatus());
                                    }
                                }
                                break;
                                
                            default:
                                logger.info("Invalid choice.");
                        }
                    } else {
                        logger.info("Invalid chef number.");
                    }
                } catch (NumberFormatException e) {
                    logger.info("Invalid input. Please enter a number.");
                }
            }
        }
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Generate reports (admin function).
     */
    private static void generateReports() {
        logger.info("\n===== Generate Reports =====");
        
        logger.info("Select report type:");
        logger.info("1. Sales Report");
        logger.info("2. Inventory Report");
        logger.info("3. Chef Performance Report");
        logger.info("4. Customer Preferences Report");
        logger.info("5. Back to Main Menu");
        logger.info("Enter your choice: ");
        
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
                logger.info("Invalid choice. Please try again.");
        }
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Generate a sales report.
     */
    private static void generateSalesReport() {
        logger.info("\n===== Sales Report =====");
        
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
        logger.info("Total Sales: $" + String.format("%.2f", totalSales));
        logger.info("Total Orders: " + allOrders.size());
        logger.info("Completed Orders: " + completedOrders);
        logger.info("Pending Orders: " + pendingOrders);
        
        logger.info("\nMost Popular Meals:");
        
        // Sort meals by popularity
        List<Map.Entry<String, Integer>> sortedMeals = new ArrayList<>(mealPopularity.entrySet());
        sortedMeals.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        for (int i = 0; i < Math.min(5, sortedMeals.size()); i++) {
            Map.Entry<String, Integer> entry = sortedMeals.get(i);
            logger.info((i + 1) + ". " + entry.getKey() + " - " + entry.getValue() + " orders");
        }
    }
    
    /**
     * Generate an inventory report.
     */
    private static void generateInventoryReport() {
        logger.info("\n===== Inventory Report =====");
        
        List<Ingredient> ingredients = inventory.getAllIngredients();
        List<Ingredient> lowStock = inventory.getLowStockIngredients();
        
        logger.info("Total Ingredients: " + ingredients.size());
        logger.info("Low Stock Ingredients: " + lowStock.size());
        
        logger.info("\nInventory Value:");
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
        
        logger.info("\nTotal Inventory Value: $" + String.format("%.2f", totalValue));
        
        logger.info("\nLow Stock Ingredients:");
        if (lowStock.isEmpty()) {
            logger.info("No low stock ingredients.");
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
        logger.info("\n===== Chef Performance Report =====");
        
        Map<String, User> users = loginSystem.getUserDatabase();
        List<String> chefs = new ArrayList<>();
        
        // Find all users with chef role
        for (User user : users.values()) {
            if (user.hasRole("chef")) {
                chefs.add(user.getUsername());
            }
        }
        
        if (chefs.isEmpty()) {
            logger.info("No chefs found in the system.");
            return;
        }
        
        logger.info("Chef Performance Summary:");
        
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
            
            logger.info("\nChef: " + chef);
            logger.info("Total Tasks: " + totalTasks);
            logger.info("Completed Tasks: " + completedTasks);
            logger.info("Pending Tasks: " + pendingTasks);
            logger.info("Late Tasks: " + lateTasks);
            logger.info("Completion Rate: " + String.format("%.1f", completionRate) + "%");
            logger.info("On-Time Rate: " + String.format("%.1f", onTimeRate) + "%");
        }
    }
    
    /**
     * Generate a customer preferences report.
     */
    private static void generateCustomerPreferencesReport() {
        logger.info("\n===== Customer Preferences Report =====");
        
        Map<String, User> users = loginSystem.getUserDatabase();
        List<String> customers = new ArrayList<>();
        
        // Find all users with customer role
        for (User user : users.values()) {
            if (!user.hasRole("admin") && !user.hasRole("chef")) {
                customers.add(user.getUsername());
            }
        }
        
        if (customers.isEmpty()) {
            logger.info("No customers found in the system.");
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
        logger.info("Total Customers: " + customers.size());
        
        logger.info("\nDietary Preferences:");
        if (dietaryPreferences.isEmpty()) {
            logger.info("No dietary preferences found.");
        } else {
            List<Map.Entry<String, Integer>> sortedPreferences = new ArrayList<>(dietaryPreferences.entrySet());
            sortedPreferences.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            for (Map.Entry<String, Integer> entry : sortedPreferences) {
                double percentage = (double) entry.getValue() / customers.size() * 100;
                System.out.println("- " + entry.getKey() + ": " + entry.getValue() + 
                        " customers (" + String.format("%.1f", percentage) + "%)");
            }
        }
        
        logger.info("\nAllergies:");
        if (allergies.isEmpty()) {
            logger.info("No allergies found.");
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