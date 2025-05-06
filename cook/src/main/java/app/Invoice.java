package app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Invoice {
    private String invoiceId;
    private String orderId;
    private String customerId;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private double subtotal;
    private double taxRate;
    private double taxAmount;
    private double totalAmount;
    private String status; // "draft", "issued", "paid", "overdue", "cancelled"
    private List<InvoiceItem> items;
    
    public Invoice(String orderId, String customerId) {
        this.invoiceId = "INV-" + UUID.randomUUID().toString().substring(0, 8);
        this.orderId = orderId;
        this.customerId = customerId;
        this.issueDate = LocalDateTime.now();
        this.dueDate = issueDate.plusDays(30); // Default 30 days payment term
        this.taxRate = 0.1; // Default 10% tax
        this.status = "draft";
        this.items = new ArrayList<>();
        this.subtotal = 0.0;
        this.taxAmount = 0.0;
        this.totalAmount = 0.0;
    }
    
    // Getters and setters
    public String getInvoiceId() {
        return invoiceId;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public LocalDateTime getIssueDate() {
        return issueDate;
    }
    
    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }
    
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public double getTaxRate() {
        return taxRate;
    }
    
    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
        recalculateAmounts();
    }
    
    public double getTaxAmount() {
        return taxAmount;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<InvoiceItem> getItems() {
        return items;
    }
    
    // Add an item to the invoice
    public void addItem(String description, double unitPrice, int quantity) {
        InvoiceItem item = new InvoiceItem(description, unitPrice, quantity);
        items.add(item);
        recalculateAmounts();
    }
    
    // Remove an item from the invoice
    public boolean removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            recalculateAmounts();
            return true;
        }
        return false;
    }
    
    // Recalculate subtotal, tax, and total
    private void recalculateAmounts() {
        subtotal = items.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
        
        taxAmount = subtotal * taxRate;
        totalAmount = subtotal + taxAmount;
    }
    
    // Issue the invoice
    public void issue() {
        if (status.equals("draft")) {
            status = "issued";
            issueDate = LocalDateTime.now();
            dueDate = issueDate.plusDays(30);
        }
    }
    
    // Mark invoice as paid
    public void markAsPaid() {
        if (status.equals("issued") || status.equals("overdue")) {
            status = "paid";
        }
    }
    
    // Check if invoice is overdue
    public boolean isOverdue() {
        if (status.equals("issued") && LocalDateTime.now().isAfter(dueDate)) {
            status = "overdue";
            return true;
        }
        return status.equals("overdue");
    }
    
    // Mark invoice as overdue
    public void markAsOverdue() {
        if (status.equals("issued")) {
            status = "overdue";
        }
    }
    
    // Cancel the invoice
    public void cancel() {
        if (!status.equals("paid")) {
            status = "cancelled";
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Invoice #").append(invoiceId)
          .append("\nOrder: ").append(orderId)
          .append("\nCustomer: ").append(customerId)
          .append("\nIssue Date: ").append(issueDate)
          .append("\nDue Date: ").append(dueDate)
          .append("\nStatus: ").append(status)
          .append("\n\nItems:");
        
        for (int i = 0; i < items.size(); i++) {
            InvoiceItem item = items.get(i);
            sb.append("\n").append(i + 1).append(". ")
              .append(item.getDescription())
              .append(" - ").append(item.getQuantity())
              .append(" x $").append(String.format("%.2f", item.getUnitPrice()))
              .append(" = $").append(String.format("%.2f", item.getQuantity() * item.getUnitPrice()));
        }
        
        sb.append("\n\nSubtotal: $").append(String.format("%.2f", subtotal))
          .append("\nTax (").append(taxRate * 100).append("%): $").append(String.format("%.2f", taxAmount))
          .append("\nTotal: $").append(String.format("%.2f", totalAmount));
        
        return sb.toString();
    }
    
    // Inner class for invoice items
    public static class InvoiceItem {
        private String description;
        private double unitPrice;
        private int quantity;
        
        public InvoiceItem(String description, double unitPrice, int quantity) {
            this.description = description;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public double getUnitPrice() {
            return unitPrice;
        }
        
        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
