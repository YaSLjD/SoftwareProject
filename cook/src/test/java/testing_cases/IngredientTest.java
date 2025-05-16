package testing_cases;
import org.junit.Before;
import org.junit.Test;
import app.Ingredient;
import static org.junit.Assert.*;

public class IngredientTest {
    
    private Ingredient ingredient;
    private final String ID = "ING001";
    private final String NAME = "Flour";
    private final String UNIT = "kg";
    private final double QUANTITY = 10.0;
    private final double MIN_LEVEL = 5.0;
    private final String SUPPLIER_ID = "SUP001";
    
    @Before
    public void setUp() {
        // Create a basic ingredient for testing
        ingredient = new Ingredient(ID, NAME, UNIT, QUANTITY, MIN_LEVEL);
    }
    
    @Test
    public void testBasicConstructor() {
        assertEquals("ID should match", ID, ingredient.getId());
        assertEquals("Name should match", NAME, ingredient.getName());
        assertEquals("Unit should match", UNIT, ingredient.getUnit());
        assertEquals("Quantity should match", QUANTITY, ingredient.getQuantity(), 0.001);
        assertEquals("Minimum stock level should match", MIN_LEVEL, ingredient.getMinimumStockLevel(), 0.001);
        assertNull("Supplier ID should be null with basic constructor", ingredient.getSupplierId());
    }
    
    @Test
    public void testFullConstructor() {
        Ingredient fullIngredient = new Ingredient(ID, NAME, UNIT, QUANTITY, MIN_LEVEL, SUPPLIER_ID);
        
        assertEquals("ID should match", ID, fullIngredient.getId());
        assertEquals("Name should match", NAME, fullIngredient.getName());
        assertEquals("Unit should match", UNIT, fullIngredient.getUnit());
        assertEquals("Quantity should match", QUANTITY, fullIngredient.getQuantity(), 0.001);
        assertEquals("Minimum stock level should match", MIN_LEVEL, fullIngredient.getMinimumStockLevel(), 0.001);
        assertEquals("Supplier ID should match", SUPPLIER_ID, fullIngredient.getSupplierId());
    }
    
    @Test
    public void testSetId() {
        String newId = "ING002";
        ingredient.setId(newId);
        assertEquals("ID should be updated", newId, ingredient.getId());
    }
    
    @Test
    public void testSetName() {
        String newName = "Sugar";
        ingredient.setName(newName);
        assertEquals("Name should be updated", newName, ingredient.getName());
    }
    
    @Test
    public void testSetUnit() {
        String newUnit = "g";
        ingredient.setUnit(newUnit);
        assertEquals("Unit should be updated", newUnit, ingredient.getUnit());
    }
    
    @Test
    public void testSetQuantity() {
        double newQuantity = 15.5;
        ingredient.setQuantity(newQuantity);
        assertEquals("Quantity should be updated", newQuantity, ingredient.getQuantity(), 0.001);
    }
    
    @Test
    public void testSetMinimumStockLevel() {
        double newMinLevel = 7.5;
        ingredient.setMinimumStockLevel(newMinLevel);
        assertEquals("Minimum stock level should be updated", newMinLevel, ingredient.getMinimumStockLevel(), 0.001);
    }
    
    @Test
    public void testSetSupplierId() {
        String newSupplierId = "SUP002";
        ingredient.setSupplierId(newSupplierId);
        assertEquals("Supplier ID should be updated", newSupplierId, ingredient.getSupplierId());
    }
    
    @Test
    public void testIsLowStockWhenBelow() {
        // Set quantity below minimum stock level
        ingredient.setQuantity(3.0); // Below MIN_LEVEL of 5.0
        assertTrue("Should be considered low stock when below minimum level", ingredient.isLowStock());
    }
    
    @Test
    public void testIsLowStockWhenEqual() {
        // Set quantity equal to minimum stock level
        ingredient.setQuantity(MIN_LEVEL); // Equal to MIN_LEVEL of 5.0
        assertTrue("Should be considered low stock when equal to minimum level", ingredient.isLowStock());
    }
    
    @Test
    public void testIsLowStockWhenAbove() {
        // Quantity is already set to 10.0 which is above MIN_LEVEL of 5.0
        assertFalse("Should not be considered low stock when above minimum level", ingredient.isLowStock());
    }
    
    @Test
    public void testUpdateQuantityIncrease() {
        double initialQuantity = ingredient.getQuantity();
        double increase = 5.0;
        
        ingredient.updateQuantity(increase);
        assertEquals("Quantity should be increased", initialQuantity + increase, ingredient.getQuantity(), 0.001);
    }
    
    @Test
    public void testUpdateQuantityDecrease() {
        double initialQuantity = ingredient.getQuantity();
        double decrease = -3.0;
        
        ingredient.updateQuantity(decrease);
        assertEquals("Quantity should be decreased", initialQuantity + decrease, ingredient.getQuantity(), 0.001);
    }
    
    @Test
    public void testToString() {
        String result = ingredient.toString();
        
        assertTrue("toString should contain ID", result.contains(ID));
        assertTrue("toString should contain name", result.contains(NAME));
        assertTrue("toString should contain unit", result.contains(UNIT));
        assertTrue("toString should contain quantity", result.contains(String.valueOf(QUANTITY)));
        assertTrue("toString should contain minimum stock level", result.contains(String.valueOf(MIN_LEVEL)));
    }
}
