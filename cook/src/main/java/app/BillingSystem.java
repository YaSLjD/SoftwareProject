package app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BillingSystem {
    private Map<String, User> userDatabase;
    private Map<String, Invoice> invoiceDatabase;
    private NotificationSystem notificationSystem;
    
    public BillingSystem(Map<String, User> userDatabase, NotificationSystem notificationSystem) {
        this.userDatabase = userDatabase;
        this.invoiceDatabase = new HashMap<>();
        this.notificationSystem = notificationSystem;
    }

    

    
    public BillingSystem(NotificationSystem notificationSystem) {
            this.userDatabase = new HashMap<>();
            this.invoiceDatabase = new HashMap<>();
            this.notificationSystem = notificationSystem;
    }
    
    // Generate an invoice for an order
    public Invoice generateInvoice(Order order) {
        if (order == null) {
            return null;
        }
        
        String customerId = order.getUsername();
        Invoice invoice = new Invoice(order.getOrderId(), customerId);
        
        // Add order items to invoice
        for (Meal meal : order.getMeals()) {
            invoice.addItem(meal.getName(), meal.getPrice(), 1);
        }
        
        // Set tax rate to 0 to match order total
        invoice.setTaxRate(0.0);
        
        // Store the invoice
        invoiceDatabase.put(invoice.getInvoiceId(), invoice);
        
        // Notify the customer
        if (notificationSystem != null) {
            notificationSystem.notifyNewInvoice(customerId, invoice);
        }
        
        return invoice;
    }
    
    // Get an invoice by ID
    public Invoice getInvoice(String invoiceId) {
        return invoiceDatabase.get(invoiceId);
    }
    
    // Get all invoices for a customer
    public List<Invoice> getCustomerInvoices(String customerId) {
        return invoiceDatabase.values().stream()
                .filter(invoice -> invoice.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }
    
    // Get all invoices for an order
    public List<Invoice> getOrderInvoices(String orderId) {
        return invoiceDatabase.values().stream()
                .filter(invoice -> invoice.getOrderId().equals(orderId))
                .collect(Collectors.toList());
    }
    
    // Update invoice status
    public boolean updateInvoiceStatus(String invoiceId, String status) {
        Invoice invoice = getInvoice(invoiceId);
        if (invoice == null) {
            return false;
        }
        
        invoice.setStatus(status);
        
        // If status is "paid", notify the customer
        if (status.equals("paid")) {
            String customerId = invoice.getCustomerId();
            if (notificationSystem != null) {
                notificationSystem.sendNotification(
                        customerId,
                        "Payment Received",
                        "Thank you for your payment of $" + String.format("%.2f", invoice.getTotalAmount()) + 
                        " for invoice #" + invoiceId,
                        "billing",
                        invoiceId
                );
            }
        }
        
        return true;
    }
    
    // Process payment for an invoice
    public boolean processPayment(String invoiceId, double amount, String paymentMethod) {
        Invoice invoice = getInvoice(invoiceId);
        if (invoice == null) {
            return false;
        }
        
        // In a real system, this would process the payment through a payment gateway
        // For now, we'll just simulate a successful payment
        
        // Check if payment amount matches invoice total
        if (Math.abs(amount - invoice.getTotalAmount()) > 0.01) {
            return false; // Payment amount doesn't match
        }
        
        // Mark invoice as paid
        invoice.markAsPaid();
        
        // Notify the customer
        String customerId = invoice.getCustomerId();
        if (notificationSystem != null) {
            notificationSystem.sendNotification(
                    customerId,
                    "Payment Processed",
                    "Your payment of $" + String.format("%.2f", amount) + 
                    " via " + paymentMethod + " for invoice #" + invoiceId + " has been processed.",
                    "billing",
                    invoiceId
            );
        }
        
        return true;
    }
    
    // Check for overdue invoices and send reminders
    public List<Invoice> checkOverdueInvoices() {
        List<Invoice> overdueInvoices = new ArrayList<>();
        
        for (Invoice invoice : invoiceDatabase.values()) {
            // Check if invoice is due and not paid
            if (invoice.getStatus().equals("issued") && LocalDateTime.now().isAfter(invoice.getDueDate())) {
                // Mark as overdue
                invoice.setStatus("overdue");
                overdueInvoices.add(invoice);
                
                // Send reminder notification
                if (notificationSystem != null) {
                    notificationSystem.notifyInvoicePaymentDue(invoice.getCustomerId(), invoice);
                }
            } else if (invoice.getStatus().equals("overdue")) {
                overdueInvoices.add(invoice);
            }
        }
        
        return overdueInvoices;
    }
    
    // Generate monthly statement for a customer
    public String generateMonthlyStatement(String customerId, int year, int month) {
        List<Invoice> customerInvoices = getCustomerInvoices(customerId);
        
        // Filter invoices for the specified month
        List<Invoice> monthlyInvoices = customerInvoices.stream()
                .filter(invoice -> {
                    LocalDateTime issueDate = invoice.getIssueDate();
                    return issueDate.getYear() == year && issueDate.getMonthValue() == month;
                })
                .collect(Collectors.toList());
        
        // Generate statement
        StringBuilder statement = new StringBuilder();
        statement.append("Monthly Statement for ").append(customerId)
                .append("\nPeriod: ").append(month).append("/").append(year)
                .append("\n\nInvoices:");
        
        double totalAmount = 0.0;
        double totalPaid = 0.0;
        double totalDue = 0.0;
        
        for (Invoice invoice : monthlyInvoices) {
            statement.append("\n- Invoice #").append(invoice.getInvoiceId())
                    .append(" (").append(invoice.getIssueDate()).append(")")
                    .append(": $").append(String.format("%.2f", invoice.getTotalAmount()))
                    .append(" - ").append(invoice.getStatus());
            
            totalAmount += invoice.getTotalAmount();
            
            if (invoice.getStatus().equals("paid")) {
                totalPaid += invoice.getTotalAmount();
            } else if (!invoice.getStatus().equals("cancelled")) {
                totalDue += invoice.getTotalAmount();
            }
        }
        
        statement.append("\n\nSummary:")
                .append("\nTotal Invoiced: $").append(String.format("%.2f", totalAmount))
                .append("\nTotal Paid: $").append(String.format("%.2f", totalPaid))
                .append("\nTotal Due: $").append(String.format("%.2f", totalDue));
        
        return statement.toString();
    }
    
    // Generate revenue report for a period
    public String generateRevenueReport(LocalDateTime start, LocalDateTime end) {
        // Filter invoices for the specified period
        List<Invoice> periodInvoices = invoiceDatabase.values().stream()
                .filter(invoice -> {
                    LocalDateTime issueDate = invoice.getIssueDate();
                    return !issueDate.isBefore(start) && !issueDate.isAfter(end);
                })
                .collect(Collectors.toList());
        
        // Generate report
        StringBuilder report = new StringBuilder();
        report.append("Revenue Report")
                .append("\nPeriod: ").append(start).append(" to ").append(end)
                .append("\n\nSummary:");
        
        double totalRevenue = periodInvoices.stream()
                .filter(invoice -> invoice.getStatus().equals("paid"))
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
        
        double pendingRevenue = periodInvoices.stream()
                .filter(invoice -> invoice.getStatus().equals("issued") || invoice.getStatus().equals("overdue"))
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
        
        int totalInvoices = periodInvoices.size();
        int paidInvoices = (int) periodInvoices.stream()
                .filter(invoice -> invoice.getStatus().equals("paid"))
                .count();
        
        report.append("\nTotal Invoices: ").append(totalInvoices)
                .append("\nPaid Invoices: ").append(paidInvoices)
                .append("\nTotal Revenue: $").append(String.format("%.2f", totalRevenue))
                .append("\nPending Revenue: $").append(String.format("%.2f", pendingRevenue));
        
        return report.toString();
    }
}
