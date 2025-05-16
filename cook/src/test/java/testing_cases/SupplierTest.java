package testing_cases;

import org.junit.Before;
import org.junit.Test;

import app.Supplier;

import static org.junit.Assert.*;

public class SupplierTest {
    
    private Supplier supplier;
    
    @Before
    public void setUp() {
        // Setup a supplier with all constructor parameters
        supplier = new Supplier("SUP001", "Test Supplier", "test@example.com", "123-456-7890", "123 Test St");
    }
    
    @Test
    public void testFullConstructor() {
        assertEquals("SUP001", supplier.getId());
        assertEquals("Test Supplier", supplier.getName());
        assertEquals("test@example.com", supplier.getEmail());
        assertEquals("123-456-7890", supplier.getPhone());
        assertEquals("123 Test St", supplier.getAddress());
        assertNotNull(supplier.getIngredientIds());
        assertTrue(supplier.getIngredientIds().isEmpty());
    }
    
    @Test
    public void testSimpleConstructor() {
        Supplier simpleSupplier = new Supplier("SUP002", "Simple Supplier", "simple@example.com", "987-654-3210");
        assertEquals("SUP002", simpleSupplier.getId());
        assertEquals("Simple Supplier", simpleSupplier.getName());
        assertEquals("simple@example.com", simpleSupplier.getEmail());
        assertEquals("987-654-3210", simpleSupplier.getPhone());
        assertNull(simpleSupplier.getAddress());
        assertNotNull(simpleSupplier.getIngredientIds());
        assertTrue(simpleSupplier.getIngredientIds().isEmpty());
    }
    
    @Test
    public void testAddressGetterAndSetter() {
        supplier.setAddress("456 New Address Ave");
        assertEquals("456 New Address Ave", supplier.getAddress());
    }
    
    @Test
    public void testIdGetterAndSetter() {
        supplier.setId("SUP999");
        assertEquals("SUP999", supplier.getId());
    }
    
    @Test
    public void testNameGetterAndSetter() {
        supplier.setName("Updated Supplier");
        assertEquals("Updated Supplier", supplier.getName());
    }
    
    @Test
    public void testEmailGetterAndSetter() {
        supplier.setEmail("updated@example.com");
        assertEquals("updated@example.com", supplier.getEmail());
    }
    
    @Test
    public void testPhoneGetterAndSetter() {
        supplier.setPhone("555-555-5555");
        assertEquals("555-555-5555", supplier.getPhone());
    }
    
    @Test
    public void testAddIngredientId() {
        supplier.addIngredientId("ING001");
        assertTrue(supplier.getIngredientIds().contains("ING001"));
        assertEquals(1, supplier.getIngredientIds().size());
        
        // Test adding duplicate - should not add again
        supplier.addIngredientId("ING001");
        assertEquals(1, supplier.getIngredientIds().size());
    }
    
    @Test
    public void testRemoveIngredientId() {
        supplier.addIngredientId("ING001");
        supplier.addIngredientId("ING002");
        assertEquals(2, supplier.getIngredientIds().size());
        
        supplier.removeIngredientId("ING001");
        assertEquals(1, supplier.getIngredientIds().size());
        assertFalse(supplier.getIngredientIds().contains("ING001"));
        assertTrue(supplier.getIngredientIds().contains("ING002"));
        
        // Test removing non-existent ingredient
        supplier.removeIngredientId("ING003");
        assertEquals(1, supplier.getIngredientIds().size());
    }
    
    @Test
    public void testToString() {
        String result = supplier.toString();
        assertTrue(result.contains("SUP001"));
        assertTrue(result.contains("Test Supplier"));
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("123-456-7890"));
    }
}