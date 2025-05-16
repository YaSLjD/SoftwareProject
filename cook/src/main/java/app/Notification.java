package app;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notification {
    private String id;
    private String recipientId;
    private String title;
    private String message;
    private String type; // "order", "inventory", "billing", "system"
    private LocalDateTime timestamp;
    private boolean isRead;
    private String relatedEntityId; // ID of related order, invoice, etc.
    
    public Notification(String recipientId, String title, String message, String type) {
        this.id = "NOTIF-" + UUID.randomUUID().toString().substring(0, 8);
        this.recipientId = recipientId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }
    
    public Notification(String recipientId, String title, String message, String type, String relatedEntityId) {
        this(recipientId, title, message, type);
        this.relatedEntityId = relatedEntityId;
    }

    
    
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public String getRecipientId() {
        return recipientId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getType() {
        return type;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void markAsRead() {
        this.isRead = true;
    }
    
    public void markAsUnread() {
        this.isRead = false;
    }
    
    public String getRelatedEntityId() {
        return relatedEntityId;
    }
    
    public void setRelatedEntityId(String relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }
    
    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", isRead=" + isRead +
                '}';
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
}
