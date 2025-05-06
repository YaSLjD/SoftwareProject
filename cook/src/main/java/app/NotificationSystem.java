package app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotificationSystem {
    private Map<String, List<Notification>> userNotifications; // Map of user ID to their notifications
    
    public NotificationSystem() {
        this.userNotifications = new HashMap<>();
    }
    
    // Send a notification to a user
    public Notification sendNotification(String userId, String title, String message, String type) {
        Notification notification = new Notification(userId, title, message, type);
        addNotificationToUser(userId, notification);
        return notification;
    }
    
    // Send a notification related to a specific entity (order, invoice, etc.)
    public Notification sendNotification(String userId, String title, String message, String type, String relatedEntityId) {
        Notification notification = new Notification(userId, title, message, type, relatedEntityId);
        addNotificationToUser(userId, notification);
        return notification;
    }
    
    // Add notification to user's list
    private void addNotificationToUser(String userId, Notification notification) {
        if (!userNotifications.containsKey(userId)) {
            userNotifications.put(userId, new ArrayList<>());
        }
        userNotifications.get(userId).add(notification);
        
        // In a real system, this would also trigger sending the notification via email, SMS, etc.
        System.out.println("Notification sent to user " + userId + ": " + notification.getTitle());
    }
    
    // Get all notifications for a user
    public List<Notification> getUserNotifications(String userId) {
        return userNotifications.getOrDefault(userId, new ArrayList<>());
    }
    
    // Get unread notifications for a user
    public List<Notification> getUnreadNotifications(String userId) {
        return getUserNotifications(userId).stream()
                .filter(notification -> !notification.isRead())
                .collect(Collectors.toList());
    }
    
    // Mark a notification as read
    public boolean markNotificationAsRead(String userId, String notificationId) {
        List<Notification> notifications = getUserNotifications(userId);
        for (Notification notification : notifications) {
            if (notification.getId().equals(notificationId)) {
                notification.markAsRead();
                return true;
            }
        }
        return false;
    }
    
    // Mark all notifications as read for a user
    public void markAllNotificationsAsRead(String userId) {
        getUserNotifications(userId).forEach(Notification::markAsRead);
    }
    
    // Delete a notification
    public boolean deleteNotification(String userId, String notificationId) {
        List<Notification> notifications = getUserNotifications(userId);
        return notifications.removeIf(notification -> notification.getId().equals(notificationId));
    }
    
    // Get notifications by type
    public List<Notification> getNotificationsByType(String userId, String type) {
        return getUserNotifications(userId).stream()
                .filter(notification -> notification.getType().equals(type))
                .collect(Collectors.toList());
    }
    
    // Get notifications related to a specific entity
    public List<Notification> getNotificationsByRelatedEntity(String userId, String relatedEntityId) {
        return getUserNotifications(userId).stream()
                .filter(notification -> relatedEntityId.equals(notification.getRelatedEntityId()))
                .collect(Collectors.toList());
    }
    
    // Get notifications within a date range
    public List<Notification> getNotificationsInDateRange(String userId, LocalDateTime start, LocalDateTime end) {
        return getUserNotifications(userId).stream()
                .filter(notification -> {
                    LocalDateTime timestamp = notification.getTimestamp();
                    return !timestamp.isBefore(start) && !timestamp.isAfter(end);
                })
                .collect(Collectors.toList());
    }
    
    // Notification for order status change
    public Notification notifyOrderStatusChange(String userId, String orderId, String newStatus) {
        String title = "Order Status Updated";
        String message = "Your order #" + orderId + " status has been updated to: " + newStatus;
        return sendNotification(userId, title, message, "order", orderId);
    }
    
    // Notification for low inventory
    public void notifyLowInventory(List<Ingredient> lowStockIngredients) {
        if (lowStockIngredients.isEmpty()) {
            return;
        }
        
        // Notify all admin users
        // In a real system, we would query for all admin users
        String adminId = "admin";
        
        StringBuilder messageBuilder = new StringBuilder("The following ingredients are low in stock:\n");
        for (Ingredient ingredient : lowStockIngredients) {
            messageBuilder.append("- ").append(ingredient.getName())
                    .append(": ").append(ingredient.getQuantity())
                    .append(" ").append(ingredient.getUnit())
                    .append(" (minimum: ").append(ingredient.getMinimumStockLevel())
                    .append(")\n");
        }
        
        sendNotification(adminId, "Low Inventory Alert", messageBuilder.toString(), "inventory");
    }
    
    // Notification for new invoice
    public Notification notifyNewInvoice(String userId, Invoice invoice) {
        String title = "New Invoice Generated";
        String message = "A new invoice #" + invoice.getInvoiceId() + 
                " has been generated for your order #" + invoice.getOrderId() + 
                " with a total amount of $" + String.format("%.2f", invoice.getTotalAmount());
        
        return sendNotification(userId, title, message, "billing", invoice.getInvoiceId());
    }
    
    // Notification for invoice payment due
    public Notification notifyInvoicePaymentDue(String userId, Invoice invoice) {
        String title = "Payment Due Reminder";
        String message = "Payment for invoice #" + invoice.getInvoiceId() + 
                " is due on " + invoice.getDueDate() + 
                ". Total amount due: $" + String.format("%.2f", invoice.getTotalAmount());
        
        return sendNotification(userId, title, message, "billing", invoice.getInvoiceId());
    }
    
    // Notification for chef assignment
    public Notification notifyChefAssignment(String chefId, String orderId) {
        String title = "New Order Assignment";
        String message = "You have been assigned to prepare order #" + orderId;
        
        return sendNotification(chefId, title, message, "order", orderId);
    }
    
    // Notification for meal plan recommendation
    public Notification notifyMealPlanRecommendation(String userId, String mealPlanId) {
        String title = "New Meal Plan Recommendation";
        String message = "A new personalized meal plan has been created for you. Check it out!";
        
        return sendNotification(userId, title, message, "system", mealPlanId);
    }
}
