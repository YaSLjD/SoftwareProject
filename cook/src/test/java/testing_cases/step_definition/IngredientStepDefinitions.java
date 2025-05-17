package testing_cases.step_definition;

import app.Ingredient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.*;

public class IngredientStepDefinitions {
    
    private Ingredient testIngredient;

    @Given("a new ingredient with ID {string}, name {string}, unit {string}, quantity {double}, and minimum stock level {double}")
    public void aNewIngredientWithDetails(String id, String name, String unit, Double quantity, Double minLevel) {
        testIngredient = new Ingredient(id, name, unit, quantity, minLevel);
        
        // Verify the constructor works correctly
        assertEquals("ID should match", id, testIngredient.getId());
        assertEquals("Name should match", name, testIngredient.getName());
        assertEquals("Unit should match", unit, testIngredient.getUnit());
        assertEquals("Quantity should match", quantity, testIngredient.getQuantity(), 0.001);
        assertEquals("Minimum stock level should match", minLevel, testIngredient.getMinimumStockLevel(), 0.001);
    }
    
    @Given("a new ingredient with ID {string}, name {string}, unit {string}, quantity {double}, minimum stock level {double}, and supplier ID {string}")
    public void aNewIngredientWithDetailsAndSupplier(String id, String name, String unit, Double quantity, Double minLevel, String supplierId) {
        testIngredient = new Ingredient(id, name, unit, quantity, minLevel, supplierId);
        
        // Verify both constructors work correctly
        assertEquals("ID should match", id, testIngredient.getId());
        assertEquals("Name should match", name, testIngredient.getName());
        assertEquals("Unit should match", unit, testIngredient.getUnit());
        assertEquals("Quantity should match", quantity, testIngredient.getQuantity(), 0.001);
        assertEquals("Minimum stock level should match", minLevel, testIngredient.getMinimumStockLevel(), 0.001);
        assertEquals("Supplier ID should match", supplierId, testIngredient.getSupplierId());
    }
    
    @When("the ingredient quantity is updated by {double}")
    public void theIngredientQuantityIsUpdatedBy(Double amount) {
        double originalQuantity = testIngredient.getQuantity();
        testIngredient.updateQuantity(amount);
        assertEquals("Quantity should be updated by the specified amount", 
                originalQuantity + amount, testIngredient.getQuantity(), 0.001);
    }
    
    @When("the ingredient ID is set to {string}")
    public void theIngredientIdIsSetTo(String newId) {
        testIngredient.setId(newId);
        assertEquals("ID should be updated", newId, testIngredient.getId());
    }
    
    @When("the ingredient name is set to {string}")
    public void theIngredientNameIsSetTo(String newName) {
        testIngredient.setName(newName);
        assertEquals("Name should be updated", newName, testIngredient.getName());
    }
    
    @When("the ingredient unit is set to {string}")
    public void theIngredientUnitIsSetTo(String newUnit) {
        testIngredient.setUnit(newUnit);
        assertEquals("Unit should be updated", newUnit, testIngredient.getUnit());
    }
    
    @When("the ingredient quantity is set to {double}")
    public void theIngredientQuantityIsSetTo(Double newQuantity) {
        testIngredient.setQuantity(newQuantity);
        assertEquals("Quantity should be updated", newQuantity, testIngredient.getQuantity(), 0.001);
    }
    
    @When("the ingredient minimum stock level is set to {double}")
    public void theIngredientMinimumStockLevelIsSetTo(Double newMinLevel) {
        testIngredient.setMinimumStockLevel(newMinLevel);
        assertEquals("Minimum stock level should be updated", newMinLevel, testIngredient.getMinimumStockLevel(), 0.001);
    }
    
    @When("the ingredient supplier ID is set to {string}")
    public void theIngredientSupplierIdIsSetTo(String newSupplierId) {
        testIngredient.setSupplierId(newSupplierId);
        assertEquals("Supplier ID should be updated", newSupplierId, testIngredient.getSupplierId());
    }
    
    @Then("the ingredient should be in low stock status")
    public void theIngredientShouldBeInLowStockStatus() {
        assertTrue("Ingredient should be in low stock status", testIngredient.isLowStock());
    }
    
    @Then("the ingredient should not be in low stock status")
    public void theIngredientShouldNotBeInLowStockStatus() {
        assertFalse("Ingredient should not be in low stock status", testIngredient.isLowStock());
    }
    
    @Then("the ingredient toString method should contain all ingredient details")
    public void theIngredientToStringMethodShouldContainAllIngredientDetails() {
        String result = testIngredient.toString();
        assertTrue("toString should contain the ID", result.contains(testIngredient.getId()));
        assertTrue("toString should contain the name", result.contains(testIngredient.getName()));
        assertTrue("toString should contain the unit", result.contains(testIngredient.getUnit()));
        assertTrue("toString should contain the quantity", result.contains(String.valueOf(testIngredient.getQuantity())));
        assertTrue("toString should contain the minimum stock level", result.contains(String.valueOf(testIngredient.getMinimumStockLevel())));
    }
    
    @Then("the ingredient ID should be {string}")
    public void theIngredientIdShouldBe(String expectedId) {
        assertEquals("Ingredient ID should match expected value", expectedId, testIngredient.getId());
    }
    
    @Then("the ingredient name should be {string}")
    public void theIngredientNameShouldBe(String expectedName) {
        assertEquals("Ingredient name should match expected value", expectedName, testIngredient.getName());
    }
    
    @Then("the ingredient unit should be {string}")
    public void theIngredientUnitShouldBe(String expectedUnit) {
        assertEquals("Ingredient unit should match expected value", expectedUnit, testIngredient.getUnit());
    }
    
    @Then("the ingredient quantity should be {double}")
    public void theIngredientQuantityShouldBe(Double expectedQuantity) {
        assertEquals("Ingredient quantity should match expected value", expectedQuantity, testIngredient.getQuantity(), 0.001);
    }
    
    @Then("the ingredient minimum stock level should be {double}")
    public void theIngredientMinimumStockLevelShouldBe(Double expectedMinLevel) {
        assertEquals("Ingredient minimum stock level should match expected value", expectedMinLevel, testIngredient.getMinimumStockLevel(), 0.001);
    }
    
    @Then("the ingredient supplier ID should be {string}")
    public void theIngredientSupplierIdShouldBe(String expectedSupplierId) {
        assertEquals("Ingredient supplier ID should match expected value", expectedSupplierId, testIngredient.getSupplierId());
    }
    
    @Then("the ingredient supplier ID should be null")
    public void theIngredientSupplierIdShouldBeNull() {
        assertNull("Ingredient supplier ID should be null", testIngredient.getSupplierId());
    }
}