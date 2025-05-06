package app;

public class Ingredient {
    private String id;
    private String name;
    private String unit; // e.g., "g", "ml", "piece"
    private double quantity;
    private double minimumStockLevel;
    private String supplierId;
    
    public Ingredient(String id, String name, String unit, double quantity, double minimumStockLevel) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
        this.minimumStockLevel = minimumStockLevel;
    }
    
    public Ingredient(String id, String name, String unit, double quantity, double minimumStockLevel, String supplierId) {
        this(id, name, unit, quantity, minimumStockLevel);
        this.supplierId = supplierId;
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    public double getMinimumStockLevel() {
        return minimumStockLevel;
    }
    
    public void setMinimumStockLevel(double minimumStockLevel) {
        this.minimumStockLevel = minimumStockLevel;
    }
    
    public String getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    
    public boolean isLowStock() {
        return quantity <= minimumStockLevel;
    }
    
    public void updateQuantity(double amount) {
        this.quantity += amount;
    }
    
    @Override
    public String toString() {
        return "Ingredient{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", quantity=" + quantity +
                ", minimumStockLevel=" + minimumStockLevel +
                '}';
    }
}
