package app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import common.logger;




/**
 * Main class to demonstrate and test the Special Cook Project Management System.
 * This class provides a simple console interface to interact with the system.
 */
public class Main {
    
    private static LoginSystem loginSystem;
    private static OrderSystem orderSystem;
    private static UserPreferenceSystem preferenceSystem;
    private static Inventory inventory;
    private static ChefSchedule chefSchedule;
    private static BillingSystem billingSystem;
    private static NotificationSystem notificationSystem;
    private static RecipeRecommendationSystem recommendationSystem;
    private static CustomMealService customMealService;
    private static Scanner scanner = new Scanner(System.in);
    
    // These variables would be defined in Main.java
    // Adding them here to resolve compilation errors
    
    private static String currentUser;
    
    public static void main(String[] args) {
        logger.info("Starting Special Cook Project Management System...");
        
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
                    showAdminMenu();
                } else if (user.hasRole("chef")) {
                    showChefMenu();
                } else {
                    showCustomerMenu();
                }
            }
            
            String choice = scanner.nextLine();
            running = processChoice(choice);
        }
        
        scanner.close();
        logger.info("System shutdown. Goodbye!");
    }
    
    /**
     * Initialize all system components.
     */
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
        
        // Initialize CustomMealService
        customMealService = new CustomMealService(inventory);
        
        logger.info("System initialized successfully.");
    }
    
    /**
     * Create a meal database for the recommendation system.
     */
    private static Map<String, Meal> createMealDatabase() {
        Map<String, Meal> mealDatabase = new HashMap<>();
        
        // Add all meals from the order system
        for (Meal meal : orderSystem.getAllMeals()) {
            mealDatabase.put(meal.getId(), meal);
        }
        
        return mealDatabase;
    }
    
    /**
     * Show the login menu with options to input username and password.
     */
    private static void showLoginMenu() {
        logger.info("\n===== Login Menu =====");
        logger.info("1. Login");
        logger.info("2. View Available Demo Accounts");
        logger.info("0. Exit");
        logger.info("Enter your choice: ");
    }
    
    /**
     * Show available demo accounts.
     */
    private static void showDemoAccounts() {
        logger.info("\n===== Available Demo Accounts =====");
        logger.info("Customer: username = testuser, password = password123");
        logger.info("Chef: username = chef, password = chefpass");
        logger.info("Admin: username = admin, password = adminpass");
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Prompt for custom login.
     */
    private static void customLogin() {
        logger.info("Enter username: ");
        String username = scanner.nextLine();
        
        logger.info("Enter password: ");
        String password = scanner.nextLine();
        
        login(username, password);
    }
    
    /**
     * Show the customer menu.
     */
    private static void showCustomerMenu() {
        logger.info("\n===== Customer Menu =====");
        logger.info("1. View Profile");
        logger.info("2. Manage Dietary Preferences");
        logger.info("3. Place New Order");
        logger.info("4. Create Custom Meal");
        logger.info("5. View Order History");
        logger.info("6. View Invoices");
        logger.info("7. Get Meal Recommendations");
        logger.info("8. View Notifications");
        logger.info("9. Logout");
        logger.info("0. Exit");
        logger.info("Enter your choice: ");
    }
    
    /**
     * Show the chef menu.
     */
    private static void showChefMenu() {
        logger.info("\n===== Chef Menu =====");
        logger.info("1. View Today's Schedule");
        logger.info("2. View All Tasks");
        logger.info("3. Mark Task as Completed");
        logger.info("4. Check Inventory");
        logger.info("5. View Notifications");
        logger.info("6. Logout");
        logger.info("0. Exit");
        logger.info("Enter your choice: ");
    }
    
    /**
     * Show the admin menu.
     */
    private static void showAdminMenu() {
        logger.info("\n===== Admin Menu =====");
        logger.info("1. Manage Users");
        logger.info("2. Manage Inventory");
        logger.info("3. Manage Suppliers");
        logger.info("4. View Chef Schedules");
        logger.info("5. Generate Reports");
        logger.info("6. View Notifications");
        logger.info("7. Logout");
        logger.info("0. Exit");
        logger.info("Enter your choice: ");
    }
    
    /**
     * Process the user's menu choice.
     * 
     * @param choice The user's choice
     * @return true to continue running, false to exit
     */
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
                    logger.info("Invalid choice. Please try again.");
            }
        } else {
            User user = loginSystem.getUserDatabase().get(currentUser);
            
            if (user.hasRole("admin")) {
                // Process admin menu choices
                switch (choice) {
                    case "1":
                        manageUsers();
                        break;
                    case "2":
                        manageInventory();
                        break;
                    case "3":
                        manageSuppliers();
                        break;
                    case "4":
                        viewChefSchedules();
                        break;
                    case "5":
                       generateReports();
                        break;
                    case "6":
                        viewNotifications();
                        break;
                    case "7":
                        logout();
                        break;
                    case "0":
                        return false;
                    default:
                        logger.info("Invalid choice. Please try again.");
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
                        logout();
                        break;
                    case "0":
                        return false;
                    default:
                        logger.info("Invalid choice. Please try again.");
                }
            } else {  // Customer
                // Process customer menu choices
                switch (choice) {
                    case "1":
                        viewProfile();
                        break;
                    case "2":
                        manageDietaryPreferences();
                        break;
                    case "3":
                        placeNewOrder();
                        break;
                    case "4":
                        createCustomMeal();
                        break;
                    case "5":
                        viewOrderHistory();
                        break;
                    case "6":
                        viewInvoices();
                        break;
                    case "7":
                        getMealRecommendations();
                        break;
                    case "8":
                        viewNotifications();
                        break;
                    case "9":
                        logout();
                        break;
                    case "0":
                        return false;
                    default:
                        logger.info("Invalid choice. Please try again.");
                }
            }
        }
        return true;
    }
    
    /**
     * Login with the given username and password.
     */
    private static void login(String username, String password) {
        String result = loginSystem.login(username, password);
        logger.info(result);
        
        if (result.startsWith("Welcome")) {
            currentUser = username;
            logger.info("Logged in as: " + username);
        } else {
            logger.info("Login failed. Please check your credentials.");
        }
    }
    
    /**
     * Logout the current user.
     */
    private static void logout() {
        logger.info("Logging out...");
        currentUser = null;
    }
    
    /**
     * View the current user's profile.
     */
    private static void viewProfile() {
        User user = loginSystem.getUserDatabase().get(currentUser);
        
        logger.info("\n===== User Profile =====");
        logger.info("Username: " + user.getUsername());
        logger.info("Role: " + user.getRole());
        
        logger.info("\nDietary Preferences:");
        for (String preference : user.getDietaryPreferences()) {
            logger.info("- " + preference);
        }
        
        logger.info("\nAllergies:");
        for (String allergy : user.getAllergies()) {
            logger.info("- " + allergy);
        }
        
        logger.info("\nOrder History:");
        for (Order order : user.getOrderHistory()) {
            System.out.println("- Order #" + order.getOrderId() + 
                    " (" + order.getOrderDate() + ") - " + order.getStatus());
        }
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Manage the current user's dietary preferences.
     */
    private static void manageDietaryPreferences() {
        boolean managing = true;
        
        while (managing) {
            User user = loginSystem.getUserDatabase().get(currentUser);
            
            logger.info("\n===== Manage Dietary Preferences =====");
            logger.info("Current Preferences:");
            int i = 1;
            for (String preference : user.getDietaryPreferences()) {
                logger.info(i++ + ". " + preference);
            }
            
            logger.info("\nCurrent Allergies:");
            i = 1;
            for (String allergy : user.getAllergies()) {
                logger.info(i++ + ". " + allergy);
            }
            
            logger.info("\n1. Add Dietary Preference");
            logger.info("2. Remove Dietary Preference");
            logger.info("3. Add Allergy");
            logger.info("4. Remove Allergy");
            logger.info("5. Back to Main Menu");
            logger.info("Enter your choice: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    logger.info("Enter dietary preference to add: ");
                    String preference = scanner.nextLine();
                    preferenceSystem.addDietaryPreference(currentUser, preference);
                    logger.info("Dietary preference added: " + preference);
                    break;
                case "2":
                    logger.info("Enter dietary preference to remove: ");
                    preference = scanner.nextLine();
                    user.removeDietaryPreference(preference);
                    logger.info("Dietary preference removed: " + preference);
                    break;
                case "3":
                    logger.info("Enter allergy to add: ");
                    String allergy = scanner.nextLine();
                    preferenceSystem.addAllergy(currentUser, allergy);
                    logger.info("Allergy added: " + allergy);
                    break;
                case "4":
                    logger.info("Enter allergy to remove: ");
                    allergy = scanner.nextLine();
                    user.removeAllergy(allergy);
                    logger.info("Allergy removed: " + allergy);
                    break;
                case "5":
                    managing = false;
                    break;
                default:
                    logger.info("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Place a new order.
     */
    private static void placeNewOrder() {
        logger.info("\n===== Place New Order =====");
        
        // Create a new order
        Order order = orderSystem.createOrder(currentUser);
        logger.info("New order created: " + order.getOrderId());
        
        // Show available meals
        List<Meal> availableMeals = orderSystem.getAllMeals();
        logger.info("\nAvailable Meals:");
        for (int i = 0; i < availableMeals.size(); i++) {
            Meal meal = availableMeals.get(i);
            System.out.println((i + 1) + ". " + meal.getName() + 
                    " - $" + meal.getPrice() + 
                    " (" + String.join(", ", meal.getDietaryCategories()) + ")");
        }
        
        // Add meals to the order
        boolean addingMeals = true;
        while (addingMeals) {
            logger.info("\nEnter meal number to add (0 to finish): ");
            String choice = scanner.nextLine();
            
            if (choice.equals("0")) {
                addingMeals = false;
            } else {
                try {
                    int mealIndex = Integer.parseInt(choice) - 1;
                    if (mealIndex >= 0 && mealIndex < availableMeals.size()) {
                        Meal meal = availableMeals.get(mealIndex);
                        orderSystem.addMealToOrder(order.getOrderId(), meal.getId());
                        logger.info("Added to order: " + meal.getName());
                    } else {
                        logger.info("Invalid meal number. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    logger.info("Invalid input. Please enter a number.");
                }
            }
        }
        
        // Update order status
        orderSystem.updateOrderStatus(order.getOrderId(), "pending");
        logger.info("\nOrder placed successfully!");
        System.out.println("Order #" + order.getOrderId() + " - Total: $" + 
                String.format("%.2f", order.getTotalPrice()));
        
        // Assign to chef
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusMinutes(45);
        Task task = new Task(order.getOrderId(), "chef", 
                "Prepare order #" + order.getOrderId(), now, end);
        chefSchedule.addTask(task);
        
        // Generate invoice
        Invoice invoice = billingSystem.generateInvoice(order);
        invoice.issue();
        
        // Send notification
        notificationSystem.notifyOrderStatusChange(currentUser, order.getOrderId(), "pending");
        notificationSystem.notifyChefAssignment("chef", order.getOrderId());
        notificationSystem.notifyNewInvoice(currentUser, invoice);
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * View the current user's order history.
     */
    private static void viewOrderHistory() {
        logger.info("\n===== Order History =====");
        
        List<Order> orders = orderSystem.getUserOrders(currentUser);
        
        if (orders.isEmpty()) {
            logger.info("No orders found.");
        } else {
            for (Order order : orders) {
                logger.info("\nOrder #" + order.getOrderId());
                logger.info("Date: " + order.getOrderDate());
                logger.info("Status: " + order.getStatus());
                logger.info("Total: $" + String.format("%.2f", order.getTotalPrice()));
                
                logger.info("Meals:");
                for (Meal meal : order.getMeals()) {
                    System.out.println("- " + meal.getName() + 
                            " - $" + String.format("%.2f", meal.getPrice()));
                }
            }
        }
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * View the current user's invoices.
     */
    private static void viewInvoices() {
        logger.info("\n===== Invoices =====");
        
        List<Invoice> invoices = billingSystem.getCustomerInvoices(currentUser);
        
        if (invoices.isEmpty()) {
            logger.info("No invoices found.");
        } else {
            for (Invoice invoice : invoices) {
                logger.info("\nInvoice #" + invoice.getInvoiceId());
                logger.info("Order: " + invoice.getOrderId());
                logger.info("Issue Date: " + invoice.getIssueDate());
                logger.info("Due Date: " + invoice.getDueDate());
                logger.info("Status: " + invoice.getStatus());
                logger.info("Total: $" + String.format("%.2f", invoice.getTotalAmount()));
                
                logger.info("Items:");
                for (Invoice.InvoiceItem item : invoice.getItems()) {
                    System.out.println("- " + item.getDescription() + 
                            " - " + item.getQuantity() + " x $" + 
                            String.format("%.2f", item.getUnitPrice()) + 
                            " = $" + String.format("%.2f", item.getQuantity() * item.getUnitPrice()));
                }
                
                // Option to pay invoice
                if (invoice.getStatus().equals("issued") || invoice.getStatus().equals("overdue")) {
                    logger.info("\nPay this invoice? (y/n): ");
                    String choice = scanner.nextLine();
                    
                    if (choice.equalsIgnoreCase("y")) {
                        billingSystem.processPayment(invoice.getInvoiceId(), invoice.getTotalAmount(), "Credit Card");
                        logger.info("Payment processed successfully!");
                    }
                }
            }
        }
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Get meal recommendations for the current user.
     */
    private static void getMealRecommendations() {
        logger.info("\n===== Meal Recommendations =====");
        
        // Get personalized recommendations
        List<Meal> recommendations = recommendationSystem.getPersonalizedRecommendations(currentUser, 5);
        
        if (recommendations.isEmpty()) {
            logger.info("No recommendations available.");
        } else {
            logger.info("Based on your preferences and order history, we recommend:");
            for (int i = 0; i < recommendations.size(); i++) {
                Meal meal = recommendations.get(i);
                System.out.println((i + 1) + ". " + meal.getName() + 
                        " - $" + String.format("%.2f", meal.getPrice()) + 
                        " (" + String.join(", ", meal.getDietaryCategories()) + ")");
            }
        }
        
        // Option to generate a custom recipe
        logger.info("\nWould you like a custom recipe? (y/n): ");
        String choice = scanner.nextLine();
        
        if (choice.equalsIgnoreCase("y")) {
            logger.info("\nEnter ingredients (comma-separated):");
            String ingredientsInput = scanner.nextLine();
            String[] ingredients = ingredientsInput.split(",");
            
            List<String> ingredientList = new ArrayList<>();
            for (String ingredient : ingredients) {
                ingredientList.add(ingredient.trim());
            }
            
            logger.info("Enter dietary category: ");
            String category = scanner.nextLine();
            
            String recipe = recommendationSystem.generateCustomRecipe(currentUser, ingredientList, category);
            logger.info("\nCustom Recipe:");
            logger.info(recipe);
        }
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * View notifications for the current user.
     */
    private static void viewNotifications() {
        logger.info("\n===== Notifications =====");
        
        List<Notification> notifications = notificationSystem.getUserNotifications(currentUser);
        
        if (notifications.isEmpty()) {
            logger.info("No notifications found.");
        } else {
            for (int i = 0; i < notifications.size(); i++) {
                Notification notification = notifications.get(i);
                System.out.println((i + 1) + ". " + (notification.isRead() ? "[Read] " : "[Unread] ") + 
                        notification.getTitle() + " - " + notification.getTimestamp());
                logger.info("   " + notification.getMessage());
                
                // Mark as read
                if (!notification.isRead()) {
                    notificationSystem.markNotificationAsRead(currentUser, notification.getId());
                }
            }
        }
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * View today's schedule for the chef.
     */
    private static void viewTodaySchedule() {
        logger.info("\n===== Today's Schedule =====");
        
        List<Task> tasks = chefSchedule.getChefTasksForDate(currentUser, LocalDate.now());
        
        if (tasks.isEmpty()) {
            logger.info("No tasks scheduled for today.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                logger.info((i + 1) + ". " + task.getDescription());
                logger.info("   Start: " + task.getStartTime());
                logger.info("   End: " + task.getEndTime());
                logger.info("   Status: " + task.getStatus());
                logger.info("   Priority: " + task.getPriority());
            }
        }
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * View all tasks for the chef.
     */
    private static void viewAllTasks() {
        logger.info("\n===== All Tasks =====");
        
        List<Task> tasks = chefSchedule.getChefTasks(currentUser);
        
        if (tasks.isEmpty()) {
            logger.info("No tasks assigned.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                logger.info((i + 1) + ". " + task.getDescription());
                logger.info("   Start: " + task.getStartTime());
                logger.info("   End: " + task.getEndTime());
                logger.info("   Status: " + task.getStatus());
                logger.info("   Priority: " + task.getPriority());
            }
        }
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Mark a task as completed.
     */
    private static void markTaskAsCompleted() {
        logger.info("\n===== Mark Task as Completed =====");
        
        List<Task> tasks = chefSchedule.getActiveChefTasks(currentUser);
        
        if (tasks.isEmpty()) {
            logger.info("No active tasks found.");
        } else {
            logger.info("Active Tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                System.out.println((i + 1) + ". " + task.getDescription() + 
                        " (" + task.getStatus() + ")");
            }
            
            logger.info("\nEnter task number to mark as completed (0 to cancel): ");
            String choice = scanner.nextLine();
            
            if (!choice.equals("0")) {
                try {
                    int taskIndex = Integer.parseInt(choice) - 1;
                    if (taskIndex >= 0 && taskIndex < tasks.size()) {
                        Task task = tasks.get(taskIndex);
                        
                        try {
                            // Start the task first if it's pending
                            if (task.getStatus().equals("pending")) {
                                task.start();
                                chefSchedule.updateTaskStatus(task.getId(), "in-progress");
                            }
                            
                            // Update task status
                            task.complete();
                            chefSchedule.updateTaskStatus(task.getId(), "completed");
                            
                            // Update order status - only if the order exists
                            Order order = orderSystem.getOrderById(task.getOrderId());
                            if (order != null) {
                                orderSystem.updateOrderStatus(task.getOrderId(), "completed");
                                
                                // Notify customer - safely get the username
                                notificationSystem.notifyOrderStatusChange(order.getUsername(), task.getOrderId(), "completed");
                            }
                            
                            logger.info("Task marked as completed!");
                        } catch (Exception e) {
                            logger.info("Error completing task: " + e.getMessage());
                            logger.info("Please contact system administrator if this problem persists.");
                        }
                    } else {
                        logger.info("Invalid task number.");
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
     * Check inventory.
     */
    private static void checkInventory() {
        logger.info("\n===== Inventory =====");
        
        List<Ingredient> ingredients = inventory.getAllIngredients();
        
        if (ingredients.isEmpty()) {
            logger.info("No ingredients found.");
        } else {
            logger.info("Current Inventory:");
            for (Ingredient ingredient : ingredients) {
                System.out.println("- " + ingredient.getName() + ": " + 
                        ingredient.getQuantity() + " " + ingredient.getUnit() + 
                        " (min: " + ingredient.getMinimumStockLevel() + ")");
            }
            
            logger.info("\nLow Stock Ingredients:");
            List<Ingredient> lowStock = inventory.getLowStockIngredients();
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
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Manage users (admin function).
     */
    private static void manageUsers() {
        logger.info("\n===== Manage Users =====");
        
        Map<String, User> users = loginSystem.getUserDatabase();
        
        logger.info("Users:");
        for (User user : users.values()) {
            logger.info("- " + user.getUsername() + " (" + user.getRole() + ")");
        }
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Manage inventory (admin function).
     */
    private static void manageInventory() {
        logger.info("\n===== Manage Inventory =====");
        
        boolean managing = true;
        while (managing) {
            List<Ingredient> ingredients = inventory.getAllIngredients();
            
            logger.info("Current Inventory:");
            for (int i = 0; i < ingredients.size(); i++) {
                Ingredient ingredient = ingredients.get(i);
                System.out.println((i + 1) + ". " + ingredient.getName() + ": " + 
                        ingredient.getQuantity() + " " + ingredient.getUnit() + 
                        " (min: " + ingredient.getMinimumStockLevel() + ")");
            }
            
            logger.info("\n1. Add New Ingredient");
            logger.info("2. Update Ingredient Quantity");
            logger.info("3. Update Minimum Stock Level");
            logger.info("4. Remove Ingredient");
            logger.info("5. View Low Stock Ingredients");
            logger.info("6. Back to Main Menu");
            logger.info("Enter your choice: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    logger.info("Enter ingredient name: ");
                    String name = scanner.nextLine();
                    
                    logger.info("Enter quantity: ");
                    double quantity = Double.parseDouble(scanner.nextLine());
                    
                    logger.info("Enter unit (e.g., kg, g, l): ");
                    String unit = scanner.nextLine();
                    
                    logger.info("Enter minimum stock level: ");
                    double minStock = Double.parseDouble(scanner.nextLine());
                    
                    // Generate a unique ID for the ingredient
                    String id = "ING" + System.currentTimeMillis();
                    Ingredient newIngredient = new Ingredient(id, name, unit, quantity, minStock);
                    inventory.addIngredient(newIngredient);
                    logger.info("Ingredient added successfully!");
                    break;
                    
                case "2":
                    logger.info("Enter ingredient number to update: ");
                    int ingredientIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    
                    if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                        Ingredient ingredient = ingredients.get(ingredientIndex);
                        
                        logger.info("Enter new quantity: ");
                        double newQuantity = Double.parseDouble(scanner.nextLine());
                        
                        inventory.updateIngredientQuantity(ingredient.getId(), newQuantity - ingredient.getQuantity());
                        logger.info("Quantity updated successfully!");
                    } else {
                        logger.info("Invalid ingredient number.");
                    }
                    break;
                    
                case "3":
                    logger.info("Enter ingredient number to update: ");
                    ingredientIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    
                    if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                        Ingredient ingredient = ingredients.get(ingredientIndex);
                        
                        logger.info("Enter new minimum stock level: ");
                        double newMinStock = Double.parseDouble(scanner.nextLine());
                        
                        ingredient.setMinimumStockLevel(newMinStock);
                        logger.info("Minimum stock level updated successfully!");
                    } else {
                        logger.info("Invalid ingredient number.");
                    }
                    break;
                    
                    case "4":
                    logger.info("Enter ingredient number to remove: ");
                    ingredientIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    
                    if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                        Ingredient ingredient = ingredients.get(ingredientIndex);
                        
                        // Remove the ingredient
                        if (inventory.removeIngredient(ingredient.getId())) {
                            logger.info("Ingredient removed successfully!");
                        } else {
                            logger.info("Failed to remove ingredient.");
                        }
                    } else {
                        logger.info("Invalid ingredient number.");
                    }
                    break;
                    
                case "5":
                    List<Ingredient> lowStock = inventory.getLowStockIngredients();
                    
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
                    
                    logger.info("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
                    
                case "6":
                    managing = false;
                    break;
                    
                default:
                    logger.info("Invalid choice. Please try again.");
            }
        }
    }
    
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
                        if (inventory.removeSupplier(supplier.getId())) {
                            logger.info("Supplier removed successfully!");
                        } else {
                            logger.info("Failed to remove supplier.");
                        }
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
                            notificationSystem.sendNotification(
                                    currentUser,
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
        logger.info("5. Order History Reports");
        logger.info("6. Back to Main Menu");
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
                orderHistoryReports();
                break;
                
            case "6":
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
    /**
     * Create a custom meal with the selected ingredients.
     * 
     * @param ingredients List of ingredient names to include in the meal
     * @return The created custom meal
     */
    private static Meal createCustomMeal(List<String> ingredients) {
        logger.info("\n===== Creating Custom Meal =====");
        
        // Generate a unique meal ID
        String mealId = "MEAL" + System.currentTimeMillis();
        
        // Create a base customizable meal
        Meal customMeal = customMealService.createCustomizableMeal(
                mealId, 
                "Custom Meal", 
                "A meal customized with your selected ingredients", 
                10.0);  // Base price
        
        // Add the selected ingredients
        logger.info("Adding ingredients to your custom meal...");
        for (String ingredient : ingredients) {
            boolean added = customMealService.addIngredientToMeal(customMeal, ingredient);
            if (added) {
                logger.info("Added: " + ingredient);
            } else {
                logger.info("Could not add: " + ingredient + " (incompatible or unavailable)");
            }
        }
        
        // Set dietary categories
        customMeal.addDietaryCategory("Custom");
        
        return customMeal;
    }
    
    /**
     * Validate if the selected ingredient combinations are valid.
     * 
     * @param ingredients List of ingredient names to validate
     * @return true if all ingredients are compatible, false otherwise
     */
    private static boolean validateIngredients(List<String> ingredients) {
        logger.info("\n===== Validating Ingredients =====");
        
        // Create a temporary meal to test compatibility
        Meal testMeal = new Meal("TEST", "Test Meal", "For validation", 0.0);
        
        // Check each ingredient
        for (String ingredient : ingredients) {
            // If we can't add it to the test meal, the combination is invalid
            if (!customMealService.addIngredientToMeal(testMeal, ingredient)) {
                // Check if it's because of incompatibility (not availability)
                if (customMealService.isIngredientAvailable(ingredient)) {
                    logger.info("Invalid combination: " + ingredient + " is incompatible with other ingredients");
                    return false;
                }
            }
        }
        
        logger.info("All ingredient combinations are valid!");
        return true;
    }
    
    /**
     * Check if a specific ingredient is available in the inventory.
     * 
     * @param ingredient Name of the ingredient to check
     * @return true if the ingredient is available, false otherwise
     */
    private static boolean checkIngredientAvailability(String ingredient) {
        boolean available = customMealService.isIngredientAvailable(ingredient);
        
        if (available) {
            logger.info("Ingredient is available: " + ingredient);
        } else {
            logger.info("Ingredient is unavailable: " + ingredient);
        }
        
        return available;
    }
    
    /**
     * Create a custom meal through the command line interface.
     */
    private static void createCustomMeal() {
        logger.info("\n===== Custom Meal Creator =====");
        
        // Show available ingredients
        List<Ingredient> availableIngredients = inventory.getAllIngredients();
        logger.info("\nAvailable Ingredients:");
        for (int i = 0; i < availableIngredients.size(); i++) {
            logger.info((i + 1) + ". " + availableIngredients.get(i).getName());
        }
        
        // Get ingredients from user
        List<String> selectedIngredients = new ArrayList<>();
        boolean selecting = true;
        
        while (selecting) {
            logger.info("\nEnter ingredient number to add (0 to finish): ");
            String choice = scanner.nextLine();
            
            if (choice.equals("0")) {
                selecting = false;
            } else {
                try {
                    int ingredientIndex = Integer.parseInt(choice) - 1;
                    if (ingredientIndex >= 0 && ingredientIndex < availableIngredients.size()) {
                        String ingredientName = availableIngredients.get(ingredientIndex).getName();
                        
                        // Check if ingredient is available
                        if (checkIngredientAvailability(ingredientName)) {
                            // Add to selected ingredients
                            selectedIngredients.add(ingredientName);
                            logger.info("Added to selection: " + ingredientName);
                            
                            // Validate the current combination
                            validateIngredients(selectedIngredients);
                        }
                    } else {
                        logger.info("Invalid ingredient number. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    logger.info("Invalid input. Please enter a number.");
                }
            }
        }
        
        // If no ingredients were selected, return
        if (selectedIngredients.isEmpty()) {
            logger.info("No ingredients selected. Returning to main menu.");
            logger.info("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        
        // Create the custom meal
        Meal customMeal = createCustomMeal(selectedIngredients);
        
        // Ask if the user wants to order this meal
        logger.info("\nWould you like to place an order with this custom meal? (y/n): ");
        String orderChoice = scanner.nextLine();
        
        if (orderChoice.equalsIgnoreCase("y")) {
            // Create a new order
            Order order = orderSystem.createOrder(currentUser);
            logger.info("New order created: " + order.getOrderId());
            
            // Add the custom meal to the order
            order.addMeal(customMeal);
            
            // Update order status
            orderSystem.updateOrderStatus(order.getOrderId(), "pending");
            logger.info("\nOrder placed successfully!");
            System.out.println("Order #" + order.getOrderId() + " - Total: $" + 
                    String.format("%.2f", order.getTotalPrice()));
            
            // Assign to chef
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime end = now.plusMinutes(45);
            Task task = new Task(order.getOrderId(), "chef", 
                    "Prepare custom order #" + order.getOrderId(), now, end);
            chefSchedule.addTask(task);
            
            // Generate invoice
            Invoice invoice = billingSystem.generateInvoice(order);
            invoice.issue();
            
            // Send notification
            notificationSystem.notifyOrderStatusChange(currentUser, order.getOrderId(), "pending");
            notificationSystem.notifyChefAssignment("chef", order.getOrderId());
            notificationSystem.notifyNewInvoice(currentUser, invoice);
        }
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Get the order history for a specific customer.
     * 
     * @param customerId ID of the customer
     * @return List of orders for the specified customer
     */
    private static List<Order> getOrderHistory(String customerId) {
        User user = loginSystem.getUserDatabase().get(customerId);
        if (user == null) {
            logger.info("User not found: " + customerId);
            return new ArrayList<>();
        }
        
        List<Order> orders = user.getOrderHistory();
        return orders;
    }
    
    /**
     * Get the complete order history for all customers.
     * 
     * @return List of all orders in the system
     */
    private static List<Order> getAllOrderHistory() {
        List<Order> allOrders = new ArrayList<>();
        
        for (User user : loginSystem.getUserDatabase().values()) {
            allOrders.addAll(user.getOrderHistory());
        }
        
        return allOrders;
    }
    
    /**
     * Get order statistics for a specified date range.
     * 
     * @param startDate Start date for the statistics
     * @param endDate End date for the statistics
     * @return Map containing statistics like total orders, completed orders, etc.
     */
    private static Map<String, Object> getOrderStatistics(Date startDate, Date endDate) {
        Map<String, Object> statistics = new HashMap<>();
        
        List<Order> allOrders = getAllOrderHistory();
        List<Order> ordersInRange = new ArrayList<>();
        
        // Filter orders in the date range
        for (Order order : allOrders) {
            LocalDateTime orderDateTime = order.getOrderDate();
            Date orderDate = java.util.Date.from(orderDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());
            
            if (!orderDate.before(startDate) && !orderDate.after(endDate)) {
                ordersInRange.add(order);
            }
        }
        
        // Calculate statistics
        int totalOrders = ordersInRange.size();
        int completedOrders = 0;
        int pendingOrders = 0;
        double totalSales = 0.0;
        
        Map<String, Integer> mealPopularity = new HashMap<>();
        
        for (Order order : ordersInRange) {
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
        
        // Calculate average order value
        double averageOrderValue = totalOrders > 0 ? totalSales / totalOrders : 0;
        
        // Store statistics
        statistics.put("totalOrders", totalOrders);
        statistics.put("completedOrders", completedOrders);
        statistics.put("pendingOrders", pendingOrders);
        statistics.put("totalSales", totalSales);
        statistics.put("averageOrderValue", averageOrderValue);
        statistics.put("mealPopularity", mealPopularity);
        
        return statistics;
    }
    
    /**
     * View and analyze order history reports (admin function).
     */
    private static void orderHistoryReports() {
        logger.info("\n===== Order History Reports =====");
        
        logger.info("Select report type:");
        logger.info("1. View All Orders");
        logger.info("2. View Orders by Customer");
        logger.info("3. View Order Statistics by Date Range");
        logger.info("4. Back to Reports Menu");
        logger.info("Enter your choice: ");
        
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                viewAllOrders();
                break;
                
            case "2":
                viewOrdersByCustomer();
                break;
                
            case "3":
                viewOrderStatisticsByDateRange();
                break;
                
            case "4":
                return;
                
            default:
                logger.info("Invalid choice. Please try again.");
        }
        
        logger.info("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * View all orders in the system.
     */
    private static void viewAllOrders() {
        logger.info("\n===== All Orders =====");
        
        List<Order> allOrders = getAllOrderHistory();
        
        if (allOrders.isEmpty()) {
            logger.info("No orders found in the system.");
        } else {
            logger.info("Total Orders: " + allOrders.size());
            
            for (Order order : allOrders) {
                logger.info("\nOrder #" + order.getOrderId());
                logger.info("Customer: " + order.getUsername());
                logger.info("Date: " + order.getOrderDate());
                logger.info("Status: " + order.getStatus());
                logger.info("Total: $" + String.format("%.2f", order.getTotalPrice()));
                
                logger.info("Meals:");
                for (Meal meal : order.getMeals()) {
                    System.out.println("- " + meal.getName() + 
                            " - $" + String.format("%.2f", meal.getPrice()));
                }
            }
        }
    }
    
    /**
     * View orders for a specific customer.
     */
    private static void viewOrdersByCustomer() {
        logger.info("\n===== Orders by Customer =====");
        
        // Show list of customers
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
        
        logger.info("\nAvailable Customers:");
        for (int i = 0; i < customers.size(); i++) {
            logger.info((i + 1) + ". " + customers.get(i));
        }
        
        logger.info("\nEnter customer number: ");
        String choice = scanner.nextLine();
        
        try {
            int customerIndex = Integer.parseInt(choice) - 1;
            if (customerIndex >= 0 && customerIndex < customers.size()) {
                String customerId = customers.get(customerIndex);
                
                // Get and display customer's orders
                List<Order> customerOrders = getOrderHistory(customerId);
                
                logger.info("\nOrders for Customer: " + customerId);
                
                if (customerOrders.isEmpty()) {
                    logger.info("No orders found for this customer.");
                } else {
                    for (Order order : customerOrders) {
                        logger.info("\nOrder #" + order.getOrderId());
                        logger.info("Date: " + order.getOrderDate());
                        logger.info("Status: " + order.getStatus());
                        logger.info("Total: $" + String.format("%.2f", order.getTotalPrice()));
                        
                        logger.info("Meals:");
                        for (Meal meal : order.getMeals()) {
                            System.out.println("- " + meal.getName() + 
                                    " - $" + String.format("%.2f", meal.getPrice()));
                        }
                    }
                }
            } else {
                logger.info("Invalid customer number.");
            }
        } catch (NumberFormatException e) {
            logger.info("Invalid input. Please enter a number.");
        }
    }
    
    /**
     * View order statistics for a specified date range.
     */
    private static void viewOrderStatisticsByDateRange() {
        logger.info("\n===== Order Statistics by Date Range =====");
        
        // Simple date input (in real implementation, use proper date parsing)
        logger.info("Enter start date (MM/DD/YYYY):");
        String startDateStr = scanner.nextLine();
        
        logger.info("Enter end date (MM/DD/YYYY):");
        String endDateStr = scanner.nextLine();
        
        try {
            // Parse dates (simple parsing, in real implementation use DateFormat)
            String[] startParts = startDateStr.split("/");
            String[] endParts = endDateStr.split("/");
            
            if (startParts.length != 3 || endParts.length != 3) {
                logger.info("Invalid date format. Please use MM/DD/YYYY.");
                return;
            }
            
            // Create java.util.Date objects
            Date startDate = new Date(
                    Integer.parseInt(startParts[2]) - 1900,  // Year (YYYY - 1900)
                    Integer.parseInt(startParts[0]) - 1,     // Month (0-based)
                    Integer.parseInt(startParts[1])          // Day
            );
            
            Date endDate = new Date(
                    Integer.parseInt(endParts[2]) - 1900,   // Year (YYYY - 1900)
                    Integer.parseInt(endParts[0]) - 1,      // Month (0-based)
                    Integer.parseInt(endParts[1])           // Day
            );
            
            // Get statistics
            Map<String, Object> statistics = getOrderStatistics(startDate, endDate);
            
            // Display statistics
            logger.info("\nOrder Statistics for " + startDateStr + " to " + endDateStr + ":");
            logger.info("Total Orders: " + statistics.get("totalOrders"));
            logger.info("Completed Orders: " + statistics.get("completedOrders"));
            logger.info("Pending Orders: " + statistics.get("pendingOrders"));
            logger.info("Total Sales: $" + String.format("%.2f", (Double)statistics.get("totalSales")));
            logger.info("Average Order Value: $" + String.format("%.2f", (Double)statistics.get("averageOrderValue")));
            
            // Display popular meals
            @SuppressWarnings("unchecked")
            Map<String, Integer> mealPopularity = (Map<String, Integer>)statistics.get("mealPopularity");
            
            logger.info("\nMost Popular Meals:");
            
            // Sort meals by popularity
            List<Map.Entry<String, Integer>> sortedMeals = new ArrayList<>(mealPopularity.entrySet());
            sortedMeals.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            for (int i = 0; i < Math.min(5, sortedMeals.size()); i++) {
                Map.Entry<String, Integer> entry = sortedMeals.get(i);
                logger.info((i + 1) + ". " + entry.getKey() + " - " + entry.getValue() + " orders");
            }
            
        } catch (NumberFormatException e) {
            logger.info("Invalid date format. Please use MM/DD/YYYY.");
        } catch (Exception e) {
            logger.info("Error processing date range: " + e.getMessage());
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

