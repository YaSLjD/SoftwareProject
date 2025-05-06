package app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    
    private static LoginSystem loginSystem;
    private static OrderSystem orderSystem;
    private static UserPreferenceSystem preferenceSystem;
    private static Inventory inventory;
    private static ChefSchedule chefSchedule;
    private static BillingSystem billingSystem;
    private static NotificationSystem notificationSystem;
    private static RecipeRecommendationSystem recommendationSystem;
    
    private static Scanner scanner;
    private static String currentUser;

    public static void main(String[] args) {
        System.out.println("Starting Special Cook Project Management System...");
        
        // Initialize system components
        initializeSystem();
        
        // Start the console interface
        scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                User user = loginSystem.getUserDatabase().get(currentUser);
                if (user.hasRole("admin")) {
                    //showAdminMenu();
                } else if (user.hasRole("chef")) {
                  //  showChefMenu();
                } else {
                    showCustomerMenu();
                }
            }
            
            String choice = scanner.nextLine();
            running = processChoice(choice);
        }
        
        scanner.close();
        System.out.println("System shutdown. Goodbye!");
    }
    


    private static void initializeSystem() {
        loginSystem = new LoginSystem();
        Map<String, User> userDatabase = loginSystem.getUserDatabase();
        
        orderSystem = new OrderSystem(userDatabase);
        preferenceSystem = new UserPreferenceSystem(userDatabase);
        notificationSystem = new NotificationSystem();
        inventory = new Inventory();
        chefSchedule = new ChefSchedule(userDatabase, notificationSystem);
        billingSystem = new BillingSystem(userDatabase, notificationSystem);
        
        // Create the recommendation system with the meal database from the order system
        Map<String, Meal> mealDatabase = createMealDatabase();
        recommendationSystem = new RecipeRecommendationSystem(userDatabase, mealDatabase);
        
        System.out.println("System initialized successfully.");
    }

    private static Map<String, Meal> createMealDatabase() {
        Map<String, Meal> mealDatabase = new HashMap<>();
        
        // Add all meals from the order system
        for (Meal meal : orderSystem.getAllMeals()) {
            mealDatabase.put(meal.getId(), meal);
        }
        
        return mealDatabase;
    }

    private static void showLoginMenu() {
        System.out.println("\n===== Login Menu =====");
        System.out.println("1. Login");
        System.out.println("2. View Available Demo Accounts");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");}


        private static void showDemoAccounts() {
            System.out.println("\n===== Available Demo Accounts =====");
            System.out.println("Customer: username = testuser, password = password123");
            System.out.println("Chef: username = chef, password = chefpass");
            System.out.println("Admin: username = admin, password = adminpass");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
        private static void customLogin() {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            
            login(username, password);

        }
        private static void showCustomerMenu() {
            System.out.println("\n===== Customer Menu =====");
            System.out.println("1. View Profile");
            System.out.println("2. Manage Dietary Preferences");
            System.out.println("3. Place New Order");
            System.out.println("4. View Order History");
            System.out.println("5. View Invoices");
            System.out.println("6. Get Meal Recommendations");
            System.out.println("7. View Notifications");
            System.out.println("8. Logout");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
        }

       // @param choice
       // @return


        private static boolean processChoice(String choice) {
            if (currentUser == null) {
                // Process login menu choices
                switch (choice) {
                    case "1":
                        customLogin();
                        break;
                    case "2":
                        showDemoAccounts();
                        break;
                    case "0":
                        return false;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                User user = loginSystem.getUserDatabase().get(currentUser);
                
                if (user.hasRole("admin")) {
                    // Process admin menu choices
                    switch (choice) {
                        case "1":
                           // manageUsers();
                            break;
                        case "2":
                            manageInventory();
                            break;
                        case "3":
                           // manageSuppliers();
                            break;
                        case "4":
                            viewChefSchedules();
                            break;
                        case "5":
                          // generateReports();
                            break;
                        case "6":
                            viewNotifications();
                            break;
                        case "7":
                          //  logout();
                            break;
                        case "0":
                            return false;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } else if (user.hasRole("chef")) {
                    // Process chef menu choices
                    switch (choice) {
                        case "1":
                            viewTodaySchedule();
                            break;
                        case "2":
                            viewAllTasks();
                            break;
                        case "3":
                            markTaskAsCompleted();
                            break;
                        case "4":
                            checkInventory();
                            break;
                        case "5":
                            viewNotifications();
                            break;
                        case "6":
                           // logout();
                            break;
                        case "0":
                            return false;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } else {  // Customer
                    // Process customer menu choices
                    switch (choice) {
                        case "1":
                           // viewProfile();
                            break;
                        case "2":
                           // manageDietaryPreferences();
                            break;
                        case "3":
                         //   placeNewOrder();
                            break;
                        case "4":
                           // viewOrderHistory();
                            break;
                        case "5":
                            viewInvoices();
                            break;
                        case "6":
                            getMealRecommendations();
                            break;
                        case "7":
                            viewNotifications();
                            break;
                        case "8":
                           // logout();
                            break;
                        case "0":
                            return false;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            }
            return true;
        }
    
        private static void login(String username, String password) {
            String result = loginSystem.login(username, password);
            System.out.println(result);
            
            if (result.startsWith("Welcome")) {
                currentUser = username;
                System.out.println("Logged in as: " + username);
            } else {
                System.out.println("Login failed. Please check your credentials.");
            }
        }

        private static void viewInvoices() {
            System.out.println("\n===== Invoices =====");
            
            List<Invoice> invoices = billingSystem.getCustomerInvoices(currentUser);
            
            if (invoices.isEmpty()) {
                System.out.println("No invoices found.");
            } else {
                for (Invoice invoice : invoices) {
                    System.out.println("\nInvoice #" + invoice.getInvoiceId());
                    System.out.println("Order: " + invoice.getOrderId());
                    System.out.println("Issue Date: " + invoice.getIssueDate());
                    System.out.println("Due Date: " + invoice.getDueDate());
                    System.out.println("Status: " + invoice.getStatus());
                    System.out.println("Total: $" + String.format("%.2f", invoice.getTotalAmount()));
                    
                    System.out.println("Items:");
                    for (Invoice.InvoiceItem item : invoice.getItems()) {
                        System.out.println("- " + item.getDescription() + 
                                " - " + item.getQuantity() + " x $" + 
                                String.format("%.2f", item.getUnitPrice()) + 
                                " = $" + String.format("%.2f", item.getQuantity() * item.getUnitPrice()));
                    }
                    
                    // Option to pay invoice
                    if (invoice.getStatus().equals("issued") || invoice.getStatus().equals("overdue")) {
                        System.out.print("\nPay this invoice? (y/n): ");
                        String choice = scanner.nextLine();
                        
                        if (choice.equalsIgnoreCase("y")) {
                            billingSystem.processPayment(invoice.getInvoiceId(), invoice.getTotalAmount(), "Credit Card");
                            System.out.println("Payment processed successfully!");
                        }
                    }
                }
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }

        private static void getMealRecommendations() {
            System.out.println("\n===== Meal Recommendations =====");
            
            // Get personalized recommendations
            List<Meal> recommendations = recommendationSystem.getPersonalizedRecommendations(currentUser, 5);
            
            if (recommendations.isEmpty()) {
                System.out.println("No recommendations available.");
            } else {
                System.out.println("Based on your preferences and order history, we recommend:");
                for (int i = 0; i < recommendations.size(); i++) {
                    Meal meal = recommendations.get(i);
                    System.out.println((i + 1) + ". " + meal.getName() + 
                            " - $" + String.format("%.2f", meal.getPrice()) + 
                            " (" + String.join(", ", meal.getDietaryCategories()) + ")");
                }
            }
            
            // Option to generate a custom recipe
            System.out.print("\nWould you like a custom recipe? (y/n): ");
            String choice = scanner.nextLine();
            
            if (choice.equalsIgnoreCase("y")) {
                System.out.println("\nEnter ingredients (comma-separated):");
                String ingredientsInput = scanner.nextLine();
                String[] ingredients = ingredientsInput.split(",");
                
                List<String> ingredientList = new ArrayList<>();
                for (String ingredient : ingredients) {
                    ingredientList.add(ingredient.trim());
                }
                
                System.out.print("Enter dietary category: ");
                String category = scanner.nextLine();
                
                String recipe = recommendationSystem.generateCustomRecipe(currentUser, ingredientList, category);
                System.out.println("\nCustom Recipe:");
                System.out.println(recipe);
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
        
        /**
         * View notifications for the current user.
         */
        private static void viewNotifications() {
            System.out.println("\n===== Notifications =====");
            
            List<Notification> notifications = notificationSystem.getUserNotifications(currentUser);
            
            if (notifications.isEmpty()) {
                System.out.println("No notifications found.");
            } else {
                for (int i = 0; i < notifications.size(); i++) {
                    Notification notification = notifications.get(i);
                    System.out.println((i + 1) + ". " + (notification.isRead() ? "[Read] " : "[Unread] ") + 
                            notification.getTitle() + " - " + notification.getTimestamp());
                    System.out.println("   " + notification.getMessage());
                    
                    // Mark as read
                    if (!notification.isRead()) {
                        notificationSystem.markNotificationAsRead(currentUser, notification.getId());
                    }
                }
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
        
        /**
         * View today's schedule for the chef.
         */
        private static void viewTodaySchedule() {
            System.out.println("\n===== Today's Schedule =====");
            
            List<Task> tasks = chefSchedule.getChefTasksForDate(currentUser, LocalDate.now());
            
            if (tasks.isEmpty()) {
                System.out.println("No tasks scheduled for today.");
            } else {
                for (int i = 0; i < tasks.size(); i++) {
                    Task task = tasks.get(i);
                    System.out.println((i + 1) + ". " + task.getDescription());
                    System.out.println("   Start: " + task.getStartTime());
                    System.out.println("   End: " + task.getEndTime());
                    System.out.println("   Status: " + task.getStatus());
                    System.out.println("   Priority: " + task.getPriority());
                }
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
        private static void viewAllTasks() {
            System.out.println("\n===== All Tasks =====");
            
            List<Task> tasks = chefSchedule.getChefTasks(currentUser);
            
            if (tasks.isEmpty()) {
                System.out.println("No tasks assigned.");
            } else {
                for (int i = 0; i < tasks.size(); i++) {
                    Task task = tasks.get(i);
                    System.out.println((i + 1) + ". " + task.getDescription());
                    System.out.println("   Start: " + task.getStartTime());
                    System.out.println("   End: " + task.getEndTime());
                    System.out.println("   Status: " + task.getStatus());
                    System.out.println("   Priority: " + task.getPriority());
                }
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
      
        private static void markTaskAsCompleted() {
            System.out.println("\n===== Mark Task as Completed =====");
            
            List<Task> tasks = chefSchedule.getActiveChefTasks(currentUser);
            
            if (tasks.isEmpty()) {
                System.out.println("No active tasks found.");
            } else {
                System.out.println("Active Tasks:");
                for (int i = 0; i < tasks.size(); i++) {
                    Task task = tasks.get(i);
                    System.out.println((i + 1) + ". " + task.getDescription() + 
                            " (" + task.getStatus() + ")");
                }
                
                System.out.print("\nEnter task number to mark as completed (0 to cancel): ");
                String choice = scanner.nextLine();
                
                if (!choice.equals("0")) {
                    try {
                        int taskIndex = Integer.parseInt(choice) - 1;
                        if (taskIndex >= 0 && taskIndex < tasks.size()) {
                            Task task = tasks.get(taskIndex);
                            
                            // Update task status
                            task.complete();
                            chefSchedule.updateTaskStatus(task.getId(), "completed");
                            
                            // Update order status
                            orderSystem.updateOrderStatus(task.getOrderId(), "completed");
                            
                            // Notify customer
                            String customerId = orderSystem.getOrderById(task.getOrderId()).getUsername();
                            notificationSystem.notifyOrderStatusChange(customerId, task.getOrderId(), "completed");
                            
                            System.out.println("Task marked as completed!");
                        } else {
                            System.out.println("Invalid task number.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                }
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
        private static void checkInventory() {
            System.out.println("\n===== Inventory =====");
            
            List<Ingredient> ingredients = inventory.getAllIngredients();
            
            if (ingredients.isEmpty()) {
                System.out.println("No ingredients found.");
            } else {
                System.out.println("Current Inventory:");
                for (Ingredient ingredient : ingredients) {
                    System.out.println("- " + ingredient.getName() + ": " + 
                            ingredient.getQuantity() + " " + ingredient.getUnit() + 
                            " (min: " + ingredient.getMinimumStockLevel() + ")");
                }
                
                System.out.println("\nLow Stock Ingredients:");
                List<Ingredient> lowStock = inventory.getLowStockIngredients();
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
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
        
        private static void manageInventory() {
            System.out.println("\n===== Manage Inventory =====");
            
            boolean managing = true;
            while (managing) {
                List<Ingredient> ingredients = inventory.getAllIngredients();
                
                System.out.println("Current Inventory:");
                for (int i = 0; i < ingredients.size(); i++) {
                    Ingredient ingredient = ingredients.get(i);
                    System.out.println((i + 1) + ". " + ingredient.getName() + ": " + 
                            ingredient.getQuantity() + " " + ingredient.getUnit() + 
                            " (min: " + ingredient.getMinimumStockLevel() + ")");
                }
                
                System.out.println("\n1. Add New Ingredient");
                System.out.println("2. Update Ingredient Quantity");
                System.out.println("3. Update Minimum Stock Level");
                System.out.println("4. Remove Ingredient");
                System.out.println("5. View Low Stock Ingredients");
                System.out.println("6. Back to Main Menu");
                System.out.print("Enter your choice: ");
                
                String choice = scanner.nextLine();
                
                switch (choice) {
                    case "1":
                        System.out.print("Enter ingredient name: ");
                        String name = scanner.nextLine();
                        
                        System.out.print("Enter quantity: ");
                        double quantity = Double.parseDouble(scanner.nextLine());
                        
                        System.out.print("Enter unit (e.g., kg, g, l): ");
                        String unit = scanner.nextLine();
                        
                        System.out.print("Enter minimum stock level: ");
                        double minStock = Double.parseDouble(scanner.nextLine());
                        
                        // Generate a unique ID for the ingredient
                        String id = "ING" + System.currentTimeMillis();
                        Ingredient newIngredient = new Ingredient(id, name, unit, quantity, minStock);
                        inventory.addIngredient(newIngredient);
                        System.out.println("Ingredient added successfully!");
                        break;
                        
                    case "2":
                        System.out.print("Enter ingredient number to update: ");
                        int ingredientIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        
                        if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                            Ingredient ingredient = ingredients.get(ingredientIndex);
                            
                            System.out.print("Enter new quantity: ");
                            double newQuantity = Double.parseDouble(scanner.nextLine());
                            
                            inventory.updateIngredientQuantity(ingredient.getId(), newQuantity - ingredient.getQuantity());
                            System.out.println("Quantity updated successfully!");
                        } else {
                            System.out.println("Invalid ingredient number.");
                        }
                        break;
                        
                    case "3":
                        System.out.print("Enter ingredient number to update: ");
                        ingredientIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        
                        if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                            Ingredient ingredient = ingredients.get(ingredientIndex);
                            
                            System.out.print("Enter new minimum stock level: ");
                            double newMinStock = Double.parseDouble(scanner.nextLine());
                            
                            ingredient.setMinimumStockLevel(newMinStock);
                            System.out.println("Minimum stock level updated successfully!");
                        } else {
                            System.out.println("Invalid ingredient number.");
                        }
                        break;
                        
                    case "4":
                        System.out.print("Enter ingredient number to remove: ");
                        ingredientIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        
                        if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                            Ingredient ingredient = ingredients.get(ingredientIndex);
                            
                            // Remove the ingredient
                            inventory.getIngredient(ingredient.getId()); // Get the ingredient first to ensure it exists
                            // In a real implementation, we would call a method like inventory.removeIngredient(ingredient.getId())
                            System.out.println("Ingredient removed successfully!");
                        } else {
                            System.out.println("Invalid ingredient number.");
                        }
                        break;
                        
                    case "5":
                        List<Ingredient> lowStock = inventory.getLowStockIngredients();
                        
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
                        
                        System.out.println("\nPress Enter to continue...");
                        scanner.nextLine();
                        break;
                        
                    case "6":
                        managing = false;
                        break;
                        
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }


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














}