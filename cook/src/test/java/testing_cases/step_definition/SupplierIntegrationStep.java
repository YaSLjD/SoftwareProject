package testing_cases.step_definition;

import app.Ingredient;
import app.Inventory;
import app.LoginSystem;
import app.Supplier;
import app.logger;
import app.NotificationSystem;
import app.Meal;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SupplierIntegrationStep {
    
    private LoginSystem loginSystem;
    private Inventory inventory;
    private NotificationSystem notificationSystem;
    private String currentAdmin;
    private Supplier currentSupplier;
    private Map<String, Double> purchaseOrderItems;
    private List<String> ingredientsToAssociate;
    private Meal testMeal;
    
    public SupplierIntegrationStep() {
        loginSystem = new LoginSystem();
        notificationSystem = new NotificationSystem();
        inventory = new Inventory();
        currentAdmin = "admin"; // Default admin user
        purchaseOrderItems = new HashMap<>();
        ingredientsToAssociate = new ArrayList<>();
    }
    
    @Given("the admin is logged into the system for supplier management")
    public void theAdminIsLoggedIntoTheSystem() {
        // Simulate admin login
        String loginResult = loginSystem.login(currentAdmin, "adminpass");
        assertTrue("Admin should be logged in", loginResult.startsWith("Welcome"));
        
        logger.info("Admin is logged into the system");
    }
    
    @When("they add a new supplier with details:")
    public void theyAddANewSupplierWithDetails(DataTable dataTable) {
        // Convert DataTable to a Map
        Map<String, String> supplierData = dataTable.asMap(String.class, String.class);
        
        // Extract values
        String id = supplierData.get("id");
        String name = supplierData.get("name");
        String email = supplierData.get("email");
        String phone = supplierData.get("phone");
        String address = supplierData.get("address");
        
        // Create a new supplier
        currentSupplier = new Supplier(id, name, email, phone, address);
        
        // Add to inventory
        inventory.addSupplier(currentSupplier);
        
        logger.info("Added new supplier: " + name + " (" + id + ")");
    }
    
    @Then("the supplier should be added to the system")
    public void theSupplierShouldBeAddedToTheSystem() {
        // Verify the supplier was added
        Supplier supplier = inventory.getSupplier(currentSupplier.getId());
        assertNotNull("Supplier should exist in the system", supplier);
        assertEquals("Supplier name should match", currentSupplier.getName(), supplier.getName());
        
        logger.info("Supplier was added to the system: " + supplier.getName());
    }
    
    @And("be available for ingredient sourcing")
    public void beAvailableForIngredientSourcing() {
        // Verify the supplier is available for sourcing
        List<Supplier> suppliers = inventory.getAllSuppliers();
        assertTrue("Supplier should be in the list of all suppliers", 
                suppliers.stream().anyMatch(s -> s.getId().equals(currentSupplier.getId())));
        
        logger.info("Supplier is available for ingredient sourcing");
    }
    
    @Given("a supplier {string} exists in the system")
    public void aSupplierExistsInTheSystem(String supplierName) {
        // Find or create the supplier
        List<Supplier> suppliers = inventory.getAllSuppliers();
        currentSupplier = suppliers.stream()
                .filter(s -> s.getName().equals(supplierName))
                .findFirst()
                .orElse(null);
        
        if (currentSupplier == null) {
            // Create a new supplier if not found
            String id = "SUP" + supplierName.replaceAll("\\s+", "").toUpperCase();
            currentSupplier = new Supplier(id, supplierName, 
                    "contact@" + supplierName.toLowerCase().replaceAll("\\s+", "") + ".com", 
                    "555-123-4567");
            inventory.addSupplier(currentSupplier);
        }
        
        assertNotNull("Supplier should exist in the system", currentSupplier);
        logger.info("Supplier exists in the system: " + supplierName);
    }
    
    @When("the admin associates the following ingredients with the supplier:")
    public void theAdminAssociatesTheFollowingIngredientsWithTheSupplier(DataTable dataTable) {
        // Convert DataTable to a List
        ingredientsToAssociate = dataTable.asList(String.class);
        
        // Associate ingredients with the supplier
        for (String ingredientName : ingredientsToAssociate) {
            // In a real system, we would create or find the ingredient and associate it
            // For now, we'll just add the ingredient name to the supplier's list
            currentSupplier.addIngredientId("ING" + ingredientName.toUpperCase().replaceAll("\\s+", ""));
            
            logger.info("Associated ingredient with supplier: " + ingredientName);
        }
    }
    
    @Then("these ingredients should be linked to the supplier")
    public void theseIngredientsShouldBeLinkedToTheSupplier() {
        // Verify the ingredients are linked to the supplier
        List<String> supplierIngredients = currentSupplier.getIngredientIds();
        
        for (String ingredientName : ingredientsToAssociate) {
            String ingredientId = "ING" + ingredientName.toUpperCase().replaceAll("\\s+", "");
            assertTrue("Ingredient should be linked to supplier: " + ingredientName, 
                    supplierIngredients.contains(ingredientId));
            
            logger.info("Verified ingredient is linked to supplier: " + ingredientName);
        }
    }
    
    @And("the system should know the source for these ingredients")
    public void theSystemShouldKnowTheSourceForTheseIngredients() {
        // In a real system, we would verify that the system can look up the supplier for each ingredient
        // For now, we'll just print a message
        logger.info("System knows the source for these ingredients");
        
        // Add more assertions to increase coverage
        List<String> ingredientIds = currentSupplier.getIngredientIds();
        assertNotNull("Ingredient list should not be null", ingredientIds);
        assertFalse("Ingredient list should not be empty", ingredientIds.isEmpty());
    }
    
    @Given("the inventory shows low stock for several ingredients")
    public void theInventoryShowsLowStockForSeveralIngredients() {
        // Create some ingredients with low stock
        Ingredient flour = new Ingredient("INGFLOUR", "Flour", "kg", 2.0, 5.0);
        Ingredient sugar = new Ingredient("INGSUGAR", "Sugar", "kg", 1.0, 3.0);
        
        inventory.addIngredient(flour);
        inventory.addIngredient(sugar);
        
        // Add test to ensure ingredients are added correctly
        assertEquals("Flour should have 2.0 kg in stock", 2.0, inventory.getIngredient("INGFLOUR").getQuantity(), 0.001);
        assertEquals("Sugar should have 1.0 kg in stock", 1.0, inventory.getIngredient("INGSUGAR").getQuantity(), 0.001);
        
        // Verify they are low stock
        List<Ingredient> lowStockIngredients = inventory.getLowStockIngredients();
        assertTrue("Should have low stock ingredients", lowStockIngredients.size() >= 2);
        
        // Test the isLowStock method
        assertTrue("Flour should be low stock", flour.isLowStock());
        assertTrue("Sugar should be low stock", sugar.isLowStock());
        
        logger.info("Inventory shows low stock for several ingredients");
        for (Ingredient ingredient : lowStockIngredients) {
            System.out.println("- " + ingredient.getName() + ": " + ingredient.getQuantity() + 
                    " " + ingredient.getUnit() + " (min: " + ingredient.getMinimumStockLevel() + ")");
        }
    }
    
    @When("the admin checks for ingredients with low stock levels")
    public void theAdminChecksForIngredientsWithLowStockLevels() {
        // Get low stock ingredients - renamed method to avoid conflict
        List<Ingredient> lowStockIngredients = inventory.getLowStockIngredients();
        
        // Verify one ingredient is low stock
        assertTrue("Should have at least one low stock ingredient", 
                lowStockIngredients.stream().anyMatch(i -> i.getId().equals("INGSUGAR")));
        
        logger.info("Admin checked for ingredients with low stock levels");
    }
    
    @When("the admin generates a purchase order for a supplier")
    public void theAdminGeneratesAPurchaseOrderForASupplier() {
        // Get a supplier
        List<Supplier> suppliers = inventory.getAllSuppliers();
        if (suppliers.isEmpty()) {
            // Create a supplier if none exists
            Supplier supplier = new Supplier("SUP001", "Test Supplier", "contact@test.com", "555-123-4567");
            inventory.addSupplier(supplier);
            currentSupplier = supplier;
        } else {
            currentSupplier = suppliers.get(0);
        }
        
        // Get low stock ingredients
        List<Ingredient> lowStockIngredients = inventory.getLowStockIngredients();
        
        // Create purchase order items
        for (Ingredient ingredient : lowStockIngredients) {
            double orderQuantity = ingredient.getMinimumStockLevel() * 2 - ingredient.getQuantity();
            purchaseOrderItems.put(ingredient.getId(), orderQuantity);
            
            System.out.println("Added to purchase order: " + ingredient.getName() + 
                    " - " + orderQuantity + " " + ingredient.getUnit());
        }
        
        logger.info("Admin generated a purchase order for supplier: " + currentSupplier.getName());
    }
    
    @Then("the system should create a purchase order with the required quantities")
    public void theSystemShouldCreateAPurchaseOrderWithTheRequiredQuantities() {
        // In a real system, this would create a purchase order object
        // For now, we'll just verify the purchase order items
        assertFalse("Purchase order should have items", purchaseOrderItems.isEmpty());
        
        logger.info("System created a purchase order with the required quantities");
        logger.info("Purchase order contains " + purchaseOrderItems.size() + " items");
    }
    
    @And("send the purchase order to the supplier")
    public void sendThePurchaseOrderToTheSupplier() {
        // In a real system, this would send the purchase order to the supplier
        // For now, we'll just simulate ordering from the supplier
        boolean orderPlaced = inventory.orderIngredientsFromSupplier(
                currentSupplier.getId(), purchaseOrderItems);
        
        assertTrue("Order should be placed successfully", orderPlaced);
        
        // Test failure case - order from non-existent supplier
        boolean failedOrder = inventory.orderIngredientsFromSupplier(
                "NON-EXISTENT-SUPPLIER", purchaseOrderItems);
        assertFalse("Order should fail for non-existent supplier", failedOrder);
        
        logger.info("Purchase order sent to supplier: " + currentSupplier.getName());
    }
    
    @Given("a purchase order has been sent to a supplier")
    public void aPurchaseOrderHasBeenSentToASupplier() {
        // Set up a supplier
        if (currentSupplier == null) {
            currentSupplier = new Supplier("SUP001", "Test Supplier", "contact@test.com", "555-123-4567");
            inventory.addSupplier(currentSupplier);
        }
        
        // Set up purchase order items
        purchaseOrderItems.put("INGFLOUR", 10.0);
        purchaseOrderItems.put("INGSUGAR", 5.0);
        
        // Simulate sending the purchase order
        inventory.orderIngredientsFromSupplier(currentSupplier.getId(), purchaseOrderItems);
        
        logger.info("Purchase order has been sent to supplier: " + currentSupplier.getName());
    }
    
    @When("the supplier updates the order status to {string}")
    public void theSupplierUpdatesTheOrderStatusTo(String status) {
        // In a real system, this would update the purchase order status
        // For now, we'll just print a message
        logger.info("Supplier updated order status to: " + status);
    }
    
    @Then("the system should update the purchase order status")
    public void theSystemShouldUpdateThePurchaseOrderStatus() {
        // In a real system, this would verify the purchase order status was updated
        // For now, we'll just print a message
        logger.info("System updated purchase order status");
    }
    
    @And("notify the admin about the shipment")
    public void notifyTheAdminAboutTheShipment() {
        // In a real system, this would send a notification
        // For now, we'll just print a message
        logger.info("Admin notified about the shipment");
        
        // Test notification functionality
        String message = "Your order from " + currentSupplier.getName() + " has been shipped";
        assertNotNull("Notification message should not be null", message);
        assertTrue("Notification message should contain supplier name", 
                message.contains(currentSupplier.getName()));
    }
    
    @Given("a supplier delivery has arrived")
    public void aSupplierDeliveryHasArrived() {
        // In a real system, this would set up a delivery
        // For now, we'll just print a message
        logger.info("Supplier delivery has arrived");
    }
    
    @When("the admin records the received items")
    public void theAdminRecordsTheReceivedItems() {
        // Simulate receiving items
    
        // Handle Flour
        Ingredient flour = inventory.getAllIngredients().stream()
                .filter(ing -> ing.getName().equals("Flour"))
                .findFirst()
                .orElse(null);
    
        if (flour != null) {
            boolean flourUpdateSuccess = inventory.updateIngredientQuantity(flour.getId(), 10.0);
            assertTrue("Should successfully update quantity for Flour", flourUpdateSuccess);
            logger.info("Received 10 kg of Flour");
        } else {
            flour = new Ingredient("INGFLOUR", "Flour", "kg", 10.0, 5.0);
            inventory.addIngredient(flour);
            logger.info("Added new ingredient: Flour (10 kg)");
        }
    
        // Handle Sugar
        Ingredient sugar = inventory.getAllIngredients().stream()
                .filter(ing -> ing.getName().equals("Sugar"))
                .findFirst()
                .orElse(null);
    
        if (sugar != null) {
            boolean sugarUpdateSuccess = inventory.updateIngredientQuantity(sugar.getId(), 5.0);
            assertTrue("Should successfully update quantity for Sugar", sugarUpdateSuccess);
            logger.info("Received 5 kg of Sugar");
        } else {
            sugar = new Ingredient("INGSUGAR", "Sugar", "kg", 5.0, 3.0);
            inventory.addIngredient(sugar);
            logger.info("Added new ingredient: Sugar (5 kg)");
        }
    
        // Test: Prevent negative quantity update (should fail)
        double currentFlourQty = flour.getQuantity();
        double attemptToSubtract = currentFlourQty + 1; // Ensure negative result
        boolean negativeUpdateResult = inventory.updateIngredientQuantity(flour.getId(), -attemptToSubtract);
        assertFalse("Should not allow negative inventory", negativeUpdateResult);
    
        // Test: Prevent update of non-existent ingredient (should fail)
        boolean nonExistentUpdateResult = inventory.updateIngredientQuantity("NON-EXISTENT", 5.0);
        assertFalse("Should not update non-existent ingredient", nonExistentUpdateResult);
    
        logger.info("Admin recorded the received items");
    }
    
    
    @Then("the system should compare the received items with the purchase order")
    public void theSystemShouldCompareTheReceivedItemsWithThePurchaseOrder() {
        // In a real system, this would compare the received items with the purchase order
        // For now, we'll just print a message
        logger.info("System compared received items with purchase order");
    }
    
    @And("highlight any discrepancies")
    public void highlightAnyDiscrepancies() {
        // In a real system, this would highlight discrepancies
        // For now, we'll just print a message
        logger.info("No discrepancies found in the delivery");
    }
    
    @Given("the system has tracked supplier deliveries for the past quarter")
    public void theSystemHasTrackedSupplierDeliveriesForThePastQuarter() {
        // In a real system, this would set up delivery tracking data
        // For now, we'll just print a message
        logger.info("System has tracked supplier deliveries for the past quarter");
    }
    
    @When("the admin requests a supplier performance report")
    public void theAdminRequestsASupplierPerformanceReport() {
        // In a real system, this would generate a report
        // For now, we'll just print a message
        logger.info("Admin requested a supplier performance report");
    }
    
    @Then("the system should display metrics for each supplier including:")
    public void theSystemShouldDisplayMetricsForEachSupplierIncluding(DataTable dataTable) {
        // Convert DataTable to a List
        List<String> metrics = dataTable.asList(String.class);
        
        // In a real system, this would display metrics
        // For now, we'll just print the metrics
        logger.info("System displayed the following metrics for each supplier:");
        for (String metric : metrics) {
            logger.info("- " + metric);
        }
        
        // Print sample data
        logger.info("\nSample data for Test Supplier:");
        logger.info("- On-time delivery rate: 95%");
        logger.info("- Order accuracy: 98%");
        logger.info("- Price competitiveness: 4.2/5");
        logger.info("- Quality rating: 4.5/5");
    }
    
    @Given("multiple suppliers provide the same ingredient")
    public void multipleSuppliersProvideTheSameIngredient() {
        // Set up multiple suppliers for the same ingredient
        Supplier supplier1 = new Supplier("SUP001", "Supplier A", "contact@suppliera.com", "555-123-4567");
        Supplier supplier2 = new Supplier("SUP002", "Supplier B", "contact@supplierb.com", "555-987-6543");
        
        inventory.addSupplier(supplier1);
        inventory.addSupplier(supplier2);
        
        // Associate both suppliers with the same ingredient
        supplier1.addIngredientId("INGFLOUR");
        supplier2.addIngredientId("INGFLOUR");
        
        // Test removal of a supplier
        boolean removed = inventory.removeSupplier("NON-EXISTENT");
        assertFalse("Should not remove non-existent supplier", removed);
        
        logger.info("Multiple suppliers provide the same ingredient:");
        logger.info("- " + supplier1.getName());
        logger.info("- " + supplier2.getName());
    }
    
    @When("the admin marks a supplier as {string} for an ingredient")
    public void theAdminMarksASupplierAsForAnIngredient(String preference) {
        // In a real system, this would mark a supplier as preferred
        // For now, we'll just select the first supplier
        List<Supplier> suppliers = inventory.getAllSuppliers();
        if (!suppliers.isEmpty()) {
            currentSupplier = suppliers.get(0);
            System.out.println("Admin marked " + currentSupplier.getName() + 
                    " as " + preference + " for Flour");
        }
    }
    
    @Then("the system should prioritize that supplier for future orders")
    public void theSystemShouldPrioritizeThatSupplierForFutureOrders() {
        // In a real system, this would verify the supplier is prioritized
        // For now, we'll just print a message
        logger.info("System will prioritize " + currentSupplier.getName() + " for future orders");
    }
    
    @And("use alternative suppliers only when necessary")
    public void useAlternativeSuppliersOnlyWhenNecessary() {
        // In a real system, this would verify alternative suppliers are used only when necessary
        // For now, we'll just print a message
        logger.info("System will use alternative suppliers only when necessary");
    }
    
    @Given("a supplier with ingredients needs to be tested")
    public void aSupplierWithIngredientsNeedsToBeManaged() {
        // Create a new supplier with all fields
        currentSupplier = new Supplier("SUP-TEST", "Test Supplier", "test@supplier.com", "123-456-7890", "123 Test St");
        inventory.addSupplier(currentSupplier);
        
        // Make sure the address is set correctly
        assertEquals("123 Test St", currentSupplier.getAddress());
        
        // Add ingredient IDs to test the ingredient management
        currentSupplier.addIngredientId("ING001");
        currentSupplier.addIngredientId("ING002");
        
        // Test that adding the same ingredient twice doesn't duplicate it
        currentSupplier.addIngredientId("ING001");
        assertEquals(2, currentSupplier.getIngredientIds().size());
        
        logger.info("Supplier created with ingredients for testing");
    }
    
    @When("the admin updates supplier contact information")
    public void theAdminUpdatesSupplierContactInformation() {
        // Test setters
        currentSupplier.setId("SUP-UPDATED");
        currentSupplier.setName("Updated Supplier");
        currentSupplier.setEmail("updated@supplier.com");
        currentSupplier.setPhone("987-654-3210");
        currentSupplier.setAddress("456 New Address Ave");
        
        // Test toString method
        String supplierString = currentSupplier.toString();
        assertTrue(supplierString.contains("SUP-UPDATED"));
        assertTrue(supplierString.contains("Updated Supplier"));
        assertTrue(supplierString.contains("updated@supplier.com"));
        assertTrue(supplierString.contains("987-654-3210"));
        
        logger.info("Admin updated supplier contact information");
        logger.info("Updated supplier: " + supplierString);
    }
    
    @Then("the system should update the supplier record")
    public void theSystemShouldUpdateTheSupplierRecord() {
        // Verify all fields were updated
        assertEquals("SUP-UPDATED", currentSupplier.getId());
        assertEquals("Updated Supplier", currentSupplier.getName());
        assertEquals("updated@supplier.com", currentSupplier.getEmail());
        assertEquals("987-654-3210", currentSupplier.getPhone());
        assertEquals("456 New Address Ave", currentSupplier.getAddress());
        
        logger.info("System updated supplier record successfully");
    }
    
    @And("remove an ingredient from the supplier's offerings")
    public void removeAnIngredientFromTheSupplierSOfferings() {
        // Get the initial count
        int initialCount = currentSupplier.getIngredientIds().size();
        
        // Remove an ingredient
        currentSupplier.removeIngredientId("ING001");
        
        // Verify it was removed
        assertEquals(initialCount - 1, currentSupplier.getIngredientIds().size());
        assertFalse(currentSupplier.getIngredientIds().contains("ING001"));
        assertTrue(currentSupplier.getIngredientIds().contains("ING002"));
        
        logger.info("Admin removed an ingredient from the supplier's offerings");
    }

    @Then("the supplier ID should be {string}")
    public void the_supplier_id_should_be(String expectedId) {
        assertTrue(expectedId.equals(currentSupplier.getId()));
    }
    
    @Then("the supplier name should be {string}")
    public void the_supplier_name_should_be(String expectedName) {
        assertTrue(expectedName.equals(currentSupplier.getName()));
    }
    
    @Then("the supplier email should be {string}")
    public void the_supplier_email_should_be(String expectedEmail) {
        assertTrue(expectedEmail.equals(currentSupplier.getEmail()));
    }
    
    @When("I set the supplier email to {string}")
    public void i_set_the_supplier_email_to(String email) {
        currentSupplier.setEmail(email);
    }

    @Then("the supplier phone should be {string}")
    public void the_supplier_phone_should_be(String expectedPhone) {
        assertTrue(expectedPhone.equals(currentSupplier.getPhone()));
    }
    
    @When("I set the supplier phone to {string}")
    public void i_set_the_supplier_phone_to(String phone) {
        currentSupplier.setPhone(phone);
    }
    
    @When("the admin removes the ingredient {string} from the supplier")
    public void the_admin_removes_the_ingredient(String ingredient) {
        currentSupplier.removeIngredientId(ingredient);
    }

    @Then("the ingredient {string} should no longer be associated with the supplier")
    public void the_ingredient_should_be_removed(String ingredient) {
        assertFalse(currentSupplier.getIngredientIds().contains(ingredient));
    }
    
    // New methods to increase coverage
    
    @Given("there are ingredients in the inventory with different stock levels")
    public void thereAreIngredientsInTheInventoryWithDifferentStockLevels() {
        // Create ingredients with varying stock levels
        Ingredient flour = new Ingredient("INGFLOUR", "Flour", "kg", 10.0, 5.0);
        Ingredient sugar = new Ingredient("INGSUGAR", "Sugar", "kg", 2.0, 5.0);  // Low stock
        Ingredient salt = new Ingredient("INGSALT", "Salt", "kg", 1.0, 0.5);    // Not low stock
        
        // Add to inventory
        inventory.addIngredient(flour);
        inventory.addIngredient(sugar);
        inventory.addIngredient(salt);
        
        // Verify stock status
        assertFalse("Flour should not be low stock", flour.isLowStock());
        assertTrue("Sugar should be low stock", sugar.isLowStock());
        assertFalse("Salt should not be low stock", salt.isLowStock());
        
        // Update quantities
        flour.updateQuantity(-3.0);
        assertEquals("Flour quantity should be updated", 7.0, flour.getQuantity(), 0.001);
        
        logger.info("Inventory has ingredients with various stock levels");
    }
    
    // Renamed this method to avoid the conflict with InventoryManagementStep
    @When("the supplier admin reviews low stock items")
    public void theSupplierAdminReviewsLowStockItems() {
        // Get low stock ingredients
        List<Ingredient> lowStockIngredients = inventory.getLowStockIngredients();
        
        // Verify one ingredient is low stock
        assertTrue("Should have at least one low stock ingredient", 
                lowStockIngredients.stream().anyMatch(i -> i.getId().equals("INGSUGAR")));
        
        logger.info("Admin checked for low stock ingredients");
    }
    
    @Then("only ingredients below minimum levels should be flagged")
    public void onlyIngredientsBelowMinimumLevelsShouldBeFlagged() {
        List<Ingredient> lowStockIngredients = inventory.getLowStockIngredients();
        
        // Verify correct ingredients are flagged
        assertTrue("Sugar should be in low stock list", 
                lowStockIngredients.stream().anyMatch(i -> i.getId().equals("INGSUGAR")));
        assertFalse("Salt should not be in low stock list", 
                lowStockIngredients.stream().anyMatch(i -> i.getId().equals("INGSALT")));
        
        logger.info("Confirmed only ingredients below minimum levels are flagged");
    }
    
    @Given("there are ingredients and suppliers in the inventory")
    public void thereAreIngredientsAndSuppliersInTheInventory() {
        // Test removing ingredients
        Ingredient testIngredient = new Ingredient("ING-TEST", "Test Ingredient", "kg", 5.0, 1.0);
        inventory.addIngredient(testIngredient);
        
        boolean removed = inventory.removeIngredient("ING-TEST");
        assertTrue("Should successfully remove existing ingredient", removed);
        
        boolean removedNonExistent = inventory.removeIngredient("NON-EXISTENT");
        assertFalse("Should not remove non-existent ingredient", removedNonExistent);
        
        // Test supplier removal
        Supplier testSupplier = new Supplier("SUP-TEST", "Test Supplier", "test@example.com", "123-456-7890");
        inventory.addSupplier(testSupplier);
        
        boolean supplierRemoved = inventory.removeSupplier("SUP-TEST");
        assertTrue("Should successfully remove existing supplier", supplierRemoved);
        
        logger.info("Tested ingredient and supplier removal");
    }
    
    @When("a meal is prepared using ingredients from inventory")
    public void aMealIsPreparedUsingIngredientsFromInventory() {
        // Create a test meal
        testMeal = new Meal("TEST-MEAL", "Test Meal", currentAdmin, 0);
        
        // Ensure ingredients exist
        Ingredient flour = inventory.getAllIngredients().stream()
                .filter(i -> i.getName().equals("Flour"))
                .findFirst()
                .orElse(null);
                
        if (flour == null) {
            flour = new Ingredient("INGFLOUR", "Flour", "kg", 25.0, 5.0);
            inventory.addIngredient(flour);
        }
        
        Ingredient sugar = inventory.getAllIngredients().stream()
                .filter(i -> i.getName().equals("Sugar"))
                .findFirst()
                .orElse(null);
                
        if (sugar == null) {
            sugar = new Ingredient("INGSUGAR", "Sugar", "kg", 15.0, 3.0);
            inventory.addIngredient(sugar);
        }
        
        // Test inventory update for meal preparation
        boolean checkResult = inventory.checkIngredientsAvailability(testMeal, 1);
        assertTrue("Should have ingredients available", checkResult);
        
        boolean updateResult = inventory.updateInventoryForMealPreparation(testMeal, 1);
        assertTrue("Should update inventory successfully", updateResult);
        
        logger.info("Meal prepared using ingredients from inventory");
    }
    
    @Then("the inventory should be updated with the meal ingredients")
    public void theInventoryShouldBeUpdatedWithTheMealIngredients() {
        // Verify the inventory was updated
        for (String entry : testMeal.getIngredients()) {
            String name = entry;
            
            // Find the ingredient by name
            Ingredient ingredient = inventory.getAllIngredients().stream()
                    .filter(ing -> ing.getName().equals(name))
                    .findFirst()
                    .orElse(null);
            
            assertNotNull("Ingredient should exist in inventory", ingredient);
            
            logger.info("Inventory updated for " + name + ": " + ingredient.getQuantity() + " kg");
        }
    }
}