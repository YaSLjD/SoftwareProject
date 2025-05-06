package app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private String username;
    private List<Meal> meals;
    private LocalDateTime orderDate;
    private double totalPrice;
    private String status; // "pending", "processing", "delivered", "cancelled"

    public Order(String orderId, String username) {
        this.orderId = orderId;
        this.username = username;
        this.meals = new ArrayList<>();
        this.orderDate = LocalDateTime.now();
        this.status = "pending";
        this.totalPrice = 0.0;
    }

    // Getters and setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void addMeal(Meal meal) {
        this.meals.add(meal);
        this.totalPrice += meal.getPrice();
    }

    public void removeMeal(Meal meal) {
        if (this.meals.remove(meal)) {
            this.totalPrice -= meal.getPrice();
        }
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId)
          .append(", Date: ").append(orderDate)
          .append(", Status: ").append(status)
          .append(", Total: $").append(String.format("%.2f", totalPrice))
          .append("\nMeals:");
        
        for (Meal meal : meals) {
            sb.append("\n- ").append(meal.getName())
              .append(" ($").append(String.format("%.2f", meal.getPrice())).append(")");
        }
        
        return sb.toString();
    }
}
