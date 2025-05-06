package app;

import java.time.LocalDateTime;
import java.util.UUID;

public class Task {
    private String id;
    private String orderId;
    private String chefId;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // "pending", "in-progress", "completed", "cancelled"
    private int priority; // 1 (highest) to 5 (lowest)
    
    public Task(String orderId, String chefId, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = "TASK-" + UUID.randomUUID().toString().substring(0, 8);
        this.orderId = orderId;
        this.chefId = chefId;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = "pending";
        this.priority = 3; // Default priority
    }
    
    public Task(String orderId, String chefId, String description, LocalDateTime startTime, LocalDateTime endTime, int priority) {
        this(orderId, chefId, description, startTime, endTime);
        this.priority = priority;
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public String getChefId() {
        return chefId;
    }
    
    public void setChefId(String chefId) {
        this.chefId = chefId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        if (priority < 1) {
            this.priority = 1;
        } else if (priority > 5) {
            this.priority = 5;
        } else {
            this.priority = priority;
        }
    }
    
    // Start the task
    public void start() {
        if (status.equals("pending")) {
            status = "in-progress";
        }
    }
    
    // Complete the task
    public void complete() {
        if (status.equals("in-progress")) {
            status = "completed";
        }
    }
    
    // Cancel the task
    public void cancel() {
        if (!status.equals("completed")) {
            status = "cancelled";
        }
    }
    
    // Check if the task is active (pending or in-progress)
    public boolean isActive() {
        return status.equals("pending") || status.equals("in-progress");
    }
    
    // Check if the task overlaps with another time period
    public boolean overlaps(LocalDateTime start, LocalDateTime end) {
        return !startTime.isAfter(end) && !endTime.isBefore(start);
    }
    
    // Calculate duration in minutes
    public long getDurationMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }
    
    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                ", priority=" + priority +
                '}';
    }
}
