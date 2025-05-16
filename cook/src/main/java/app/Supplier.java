package app;

import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private List<String> ingredientIds; // IDs of ingredients this supplier provides
    
    public Supplier(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.ingredientIds = new ArrayList<>();
    }
    
    public Supplier(String id, String name, String email, String phone, String address) {
        this(id, name, email, phone);
        this.address = address;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public List<String> getIngredientIds() {
        return ingredientIds;
    }
    
    public void addIngredientId(String ingredientId) {
        if (!ingredientIds.contains(ingredientId)) {
            ingredientIds.add(ingredientId);
        }
    }
    
    public void removeIngredientId(String ingredientId) {
        ingredientIds.remove(ingredientId);
    }
    
    @Override
    public String toString() {
        return "Supplier{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}