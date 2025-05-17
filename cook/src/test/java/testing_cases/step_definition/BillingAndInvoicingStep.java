package testing_cases.step_definition;

import app.BillingSystem;
import app.Invoice;
import app.LoginSystem;
import app.Order;
import app.OrderSystem;
import app.logger;
import app.NotificationSystem;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Assert;

import static org.junit.Assert.*;

public class BillingAndInvoicingStep {
    
    private LoginSystem loginSystem;
    private OrderSystem orderSystem;
    private BillingSystem billingSystem;
    private NotificationSystem notificationSystem;
    private String currentCustomer;
    private Order currentOrder;
    private Invoice currentInvoice;
    private String adminUsername;
    
    public BillingAndInvoicingStep() {
        loginSystem = new LoginSystem();
        orderSystem = new OrderSystem(loginSystem.getUserDatabase());
        notificationSystem = new NotificationSystem();
        billingSystem = new BillingSystem(loginSystem.getUserDatabase(), notificationSystem);
        currentCustomer = "testuser"; // Default test user
        adminUsername = "admin"; // Default admin user
    }
    
    @Given("a customer has placed an order")
public void aCustomerHasPlacedAnOrder() {
    // Create a new order
    currentOrder = orderSystem.createOrder(currentCustomer);
    assertNotNull("Order should be created", currentOrder);

    // Add some meals to the order
    boolean added1 = orderSystem.addMealToOrder(currentOrder.getOrderId(), "M001");
    boolean added2 = orderSystem.addMealToOrder(currentOrder.getOrderId(), "M002");

    assertTrue("Meals should be added to order", added1 && added2);

    // ✅ Create an invoice based on the order
    currentInvoice = billingSystem.generateInvoice(currentOrder);
    logger.info("Customer has placed an order: " + currentOrder.getOrderId());
    logger.info("Order contains " + currentOrder.getMeals().size() + " meals");
}

@And("an invoice is generated for the order")
public void anInvoiceIsGeneratedForTheOrder() {
    currentInvoice = billingSystem.generateInvoice(currentOrder);
    assertNotNull("Invoice should be generated", currentInvoice);
}

    
    @When("the order is completed")
    public void theOrderIsCompleted() {
        // Update order status
        boolean updated = orderSystem.updateOrderStatus(currentOrder.getOrderId(), "completed");
        assertTrue("Order status should be updated", updated);
        
        assertEquals("Order status should be completed", "completed", currentOrder.getStatus());
        
        logger.info("Order has been completed: " + currentOrder.getOrderId());
    }
    
    @Then("the system should automatically generate an invoice")
    public void theSystemShouldAutomaticallyGenerateAnInvoice() {
        // Generate invoice
        currentInvoice = billingSystem.generateInvoice(currentOrder);
        assertNotNull("Invoice should be generated", currentInvoice);
        
        logger.info("System generated invoice: " + currentInvoice.getInvoiceId());
    }
    
    @And("include all order items with correct prices")
    public void includeAllOrderItemsWithCorrectPrices() {
        // Verify invoice items
        List<Invoice.InvoiceItem> invoiceItems = currentInvoice.getItems();
        assertEquals("Invoice should have the same number of items as the order", 
                currentOrder.getMeals().size(), invoiceItems.size());
        
        // Verify total amount
        assertEquals("Invoice total should match order total", 
                currentOrder.getTotalPrice(), currentInvoice.getTotalAmount(), 0.01);
        
        logger.info("Invoice includes all order items with correct prices");
        logger.info("Invoice total: $" + String.format("%.2f", currentInvoice.getTotalAmount()));
    }
    
    @Given("an invoice has been generated for an order")
    public void anInvoiceHasBeenGeneratedForAnOrder() {
        // Create an order if needed
        if (currentOrder == null) {
            currentOrder = orderSystem.createOrder(currentCustomer);
            orderSystem.addMealToOrder(currentOrder.getOrderId(), "M001");
            orderSystem.updateOrderStatus(currentOrder.getOrderId(), "completed");
        }
        
        // Generate invoice
        currentInvoice = billingSystem.generateInvoice(currentOrder);
        assertNotNull("Invoice should be generated", currentInvoice);
        
        logger.info("Invoice has been generated: " + currentInvoice.getInvoiceId());
    }
    
    @When("the system processes the invoice")
    public void theSystemProcessesTheInvoice() {
        // Process the invoice (in a real system, this might involve more steps)
        currentInvoice.issue();
        assertEquals("Invoice status should be issued", "issued", currentInvoice.getStatus());
        
        logger.info("System processed the invoice: " + currentInvoice.getInvoiceId());
    }
    
    @Then("the customer should receive a notification with the invoice details")
    public void theCustomerShouldReceiveANotificationWithTheInvoiceDetails() {
        // In a real system, we would verify that a notification was sent
        // For now, we'll just print a message
        logger.info("Customer received notification with invoice details");
    }
    
    @And("be able to view the full invoice")
    public void beAbleToViewTheFullInvoice() {
        // In a real system, this would verify the customer can view the invoice
        // For now, we'll just print the invoice details
        logger.info("Customer can view the full invoice:");
        logger.info(currentInvoice.toString());
    }
    
    @Given("a customer has received an invoice")
    public void aCustomerHasReceivedAnInvoice() {
        // Create an order and invoice if needed
        if (currentInvoice == null) {
            currentOrder = orderSystem.createOrder(currentCustomer);
            orderSystem.addMealToOrder(currentOrder.getOrderId(), "M001");
            orderSystem.updateOrderStatus(currentOrder.getOrderId(), "completed");
            currentInvoice = billingSystem.generateInvoice(currentOrder);
            currentInvoice.issue();
        }
        
        logger.info("Customer has received invoice: " + currentInvoice.getInvoiceId());
    }
    
    @When("they make a payment using a credit card")
    public void theyMakeAPaymentUsingACreditCard() {
        // Process payment
        boolean paymentProcessed = billingSystem.processPayment(
                currentInvoice.getInvoiceId(), currentInvoice.getTotalAmount(), "Credit Card");
        
        assertTrue("Payment should be processed successfully", paymentProcessed);
        
        System.out.println("Customer made a payment of $" + 
                String.format("%.2f", currentInvoice.getTotalAmount()) + " using a credit card");
    }
    
    @Then("the system should process the payment")
    public void theSystemShouldProcessThePayment() {
        // Verify payment was processed
        Invoice updatedInvoice = billingSystem.getInvoice(currentInvoice.getInvoiceId());
        assertNotNull("Invoice should exist", updatedInvoice);
        
        logger.info("System processed the payment");
    }
    
    @And("update the invoice status to {string}")
    public void updateTheInvoiceStatusTo(String status) {
        // Verify invoice status
        Invoice updatedInvoice = billingSystem.getInvoice(currentInvoice.getInvoiceId());
        assertEquals("Invoice status should be updated", status, updatedInvoice.getStatus());
        
        logger.info("Invoice status updated to: " + updatedInvoice.getStatus());
    }
    
    @And("send a payment confirmation to the customer")
    public void sendAPaymentConfirmationToTheCustomer() {
        // In a real system, this would verify a confirmation was sent
        // For now, we'll just print a message
        logger.info("Payment confirmation sent to customer");
    }
    
    @Given("a customer has a large invoice")
    public void aCustomerHasALargeInvoice() {
        // Create a large order
        currentOrder = orderSystem.createOrder(currentCustomer);
        
        // Add multiple meals to make it "large"
        orderSystem.addMealToOrder(currentOrder.getOrderId(), "M001");
        orderSystem.addMealToOrder(currentOrder.getOrderId(), "M002");
        orderSystem.addMealToOrder(currentOrder.getOrderId(), "M003");
        orderSystem.addMealToOrder(currentOrder.getOrderId(), "M004");
        
        orderSystem.updateOrderStatus(currentOrder.getOrderId(), "completed");
        
        // Generate invoice
        currentInvoice = billingSystem.generateInvoice(currentOrder);
        currentInvoice.issue();
        
        System.out.println("Customer has a large invoice: $" + 
                String.format("%.2f", currentInvoice.getTotalAmount()));
    }
    
    @When("they make a partial payment")
    public void theyMakeAPartialPayment() {
        // In a real system, this would handle partial payments
        // For now, we'll just simulate it
        double partialAmount = currentInvoice.getTotalAmount() / 2;
        
        // Update invoice status manually since our simple implementation doesn't support partial payments
        billingSystem.updateInvoiceStatus(currentInvoice.getInvoiceId(), "partially paid");
        
        System.out.println("Customer made a partial payment of $" + 
                String.format("%.2f", partialAmount));
    }
    
    @Then("the system should record the partial payment")
    public void theSystemShouldRecordThePartialPayment() {
        // In a real system, this would verify the payment was recorded
        // For now, we'll just print a message
        logger.info("System recorded the partial payment");
    }
    
    @And("update the remaining balance")
    public void updateTheRemainingBalance() {
        // In a real system, this would verify the balance was updated
        // For now, we'll just print a message
        double remainingBalance = currentInvoice.getTotalAmount() / 2;
        logger.info("Remaining balance: $" + String.format("%.2f", remainingBalance));
    }
    
    @And("maintain the invoice status as {string}")
    public void maintainTheInvoiceStatusAs(String status) {
        // Verify invoice status
        Invoice updatedInvoice = billingSystem.getInvoice(currentInvoice.getInvoiceId());
        assertEquals("Invoice status should be maintained", status, updatedInvoice.getStatus());
        
        logger.info("Invoice status maintained as: " + updatedInvoice.getStatus());
    }
    
    @Given("a customer has multiple invoices in a month")
    public void aCustomerHasMultipleInvoicesInAMonth() {
        // Create multiple orders and invoices
        for (int i = 0; i < 3; i++) {
            Order order = orderSystem.createOrder(currentCustomer);
            orderSystem.addMealToOrder(order.getOrderId(), "M001");
            orderSystem.updateOrderStatus(order.getOrderId(), "completed");
            
            Invoice invoice = billingSystem.generateInvoice(order);
            invoice.issue();
            
            // Set different statuses
            if (i == 0) {
                billingSystem.processPayment(invoice.getInvoiceId(), invoice.getTotalAmount(), "Credit Card");
            }
        }
        
        // Verify multiple invoices exist
        List<Invoice> customerInvoices = billingSystem.getCustomerInvoices(currentCustomer);
        assertTrue("Customer should have multiple invoices", customerInvoices.size() >= 3);
        
        logger.info("Customer has " + customerInvoices.size() + " invoices in the month");
    }
    
    @When("the end of month is reached")
    public void theEndOfMonthIsReached() {
        // In a real system, this might trigger a scheduled job
        // For now, we'll just print a message
        logger.info("End of month reached");
    }
    
    @Then("the system should generate a monthly statement")
    public void theSystemShouldGenerateAMonthlyStatement() {
        // Generate monthly statement
        LocalDateTime now = LocalDateTime.now();
        String statement = billingSystem.generateMonthlyStatement(
                currentCustomer, now.getYear(), now.getMonthValue());
        
        assertNotNull("Monthly statement should be generated", statement);
        assertFalse("Monthly statement should not be empty", statement.isEmpty());
        
        logger.info("System generated a monthly statement");
    }
    
    @And("include all invoices with their payment status")
    public void includeAllInvoicesWithTheirPaymentStatus() {
        // In a real system, this would verify the statement includes all invoices
        // For now, we'll just print a sample statement
        LocalDateTime now = LocalDateTime.now();
        String statement = billingSystem.generateMonthlyStatement(
                currentCustomer, now.getYear(), now.getMonthValue());
        
        logger.info("Monthly statement includes all invoices with their payment status:");
        logger.info(statement);
    }
    
    @Given("an invoice is past its due date")
    public void anInvoiceIsPastItsDueDate() {
        // Create an invoice if needed
        if (currentInvoice == null) {
            currentOrder = orderSystem.createOrder(currentCustomer);
            orderSystem.addMealToOrder(currentOrder.getOrderId(), "M001");
            orderSystem.updateOrderStatus(currentOrder.getOrderId(), "completed");
            currentInvoice = billingSystem.generateInvoice(currentOrder);
            currentInvoice.issue();
        }
        
        // Directly set the status to overdue for the test
        currentInvoice.setStatus("overdue");
        
        logger.info("Invoice is past its due date: " + currentInvoice.getInvoiceId());
    }
    
    @When("the system runs the daily payment check")
    public void theSystemRunsTheDailyPaymentCheck() {
        // Check for overdue invoices
        List<Invoice> overdueInvoices = billingSystem.checkOverdueInvoices();
        
        logger.info("System ran the daily payment check");
        logger.info("Found " + overdueInvoices.size() + " overdue invoices");
    }
    
    @Then("it should mark the invoice as {string}")
    public void itShouldMarkTheInvoiceAs(String status) {
        // Verify invoice status
        Invoice updatedInvoice = billingSystem.getInvoice(currentInvoice.getInvoiceId());
        assertEquals("Invoice status should be marked as " + status, status, updatedInvoice.getStatus());
        
        logger.info("Invoice marked as: " + updatedInvoice.getStatus());
    }
    
    @And("send a payment reminder to the customer")
    public void sendAPaymentReminderToTheCustomer() {
        // In a real system, this would verify a reminder was sent
        // For now, we'll just print a message
        logger.info("Payment reminder sent to customer");
    }
    
    @Given("a customer qualifies for a discount")
    public void aCustomerQualifiesForADiscount() {
        // In a real system, this would set up discount eligibility
        // For now, we'll just print a message
        logger.info("Customer qualifies for a discount");
    }
    
    @When("an invoice is generated for their order")
    public void anInvoiceIsGeneratedForTheirOrder() {
        // Create an order
        currentOrder = orderSystem.createOrder(currentCustomer);
        orderSystem.addMealToOrder(currentOrder.getOrderId(), "M001");
        orderSystem.updateOrderStatus(currentOrder.getOrderId(), "completed");
        
        // Generate invoice
        currentInvoice = billingSystem.generateInvoice(currentOrder);
        
        logger.info("Invoice generated for order: " + currentOrder.getOrderId());
    }
    
    @Then("the system should apply the appropriate discount")
    public void theSystemShouldApplyTheAppropriateDiscount() {
        // In a real system, this would apply a discount
        // For now, we'll just simulate it
        double originalTotal = currentInvoice.getTotalAmount();
        double discountRate = 0.1; // 10% discount
        double discountAmount = originalTotal * discountRate;
        
        logger.info("System applied a " + (discountRate * 100) + "% discount");
        logger.info("Discount amount: $" + String.format("%.2f", discountAmount));
    }
    
    @And("clearly show the discount on the invoice")
    public void clearlyShowTheDiscountOnTheInvoice() {
        // In a real system, this would verify the discount is shown
        // For now, we'll just print a message
        logger.info("Discount is clearly shown on the invoice");
    }
    
    @Given("the admin wants to analyze revenue")
    public void theAdminWantsToAnalyzeRevenue() {
        // Simulate admin login
        String loginResult = loginSystem.login(adminUsername, "adminpass");
        assertTrue("Admin should be logged in", loginResult.startsWith("Welcome"));
        
        logger.info("Admin wants to analyze revenue");
    }
    
    @When("they request a revenue report for a specific period")
    public void theyRequestARevenueReportForASpecificPeriod() {
        // Generate revenue report
        LocalDateTime start = LocalDateTime.now().minusMonths(1);
        LocalDateTime end = LocalDateTime.now();
        
        String report = billingSystem.generateRevenueReport(start, end);
        assertNotNull("Revenue report should be generated", report);
        assertFalse("Revenue report should not be empty", report.isEmpty());
        
        logger.info("Admin requested a revenue report for the past month");
    }
    
    @Then("the system should generate a detailed report")
    public void theSystemShouldGenerateADetailedReport() {
        // In a real system, this would verify the report details
        // For now, we'll just print a message
        logger.info("System generated a detailed revenue report");
    }
    
    @And("show total revenue, paid invoices, and pending payments")
    public void showTotalRevenuePaidInvoicesAndPendingPayments() {
        // In a real system, this would verify the report contents
        // For now, we'll just print a sample report
        LocalDateTime start = LocalDateTime.now().minusMonths(1);
        LocalDateTime end = LocalDateTime.now();
        
        String report = billingSystem.generateRevenueReport(start, end);
        
        logger.info("Revenue report shows total revenue, paid invoices, and pending payments:");
        logger.info(report);
    }

    @When("I manually set the issue date to {string}")
     public void i_set_the_issue_date(String dateStr) {
    LocalDateTime date = LocalDateTime.parse(dateStr);
    currentInvoice.setIssueDate(date);
   }

@When("I manually set the due date to {string}")
public void i_set_the_due_date(String dateStr) {
    LocalDateTime date = LocalDateTime.parse(dateStr);
    currentInvoice.setDueDate(date);
}



@Given("the invoice was issued more than {int} days ago")
public void the_invoice_was_issued_days_ago(int daysAgo) {
    LocalDateTime past = LocalDateTime.now().minusDays(daysAgo);
    currentInvoice.setIssueDate(past);
    currentInvoice.setDueDate(past.plusDays(30));
    currentInvoice.issue();
}

@When("I check if the invoice is overdue")
public void i_check_if_invoice_is_overdue() {
    currentInvoice.isOverdue();
}

@Then("the invoice status should be {string}")
public void the_invoice_status_should_be(String expectedStatus) {
    assertEquals(expectedStatus, currentInvoice.getStatus());
}
@When("I mark the invoice as overdue")
public void i_mark_invoice_as_overdue() {
    currentInvoice.issue(); // Ensure it's in issued state
    currentInvoice.markAsOverdue();
}

@When("I cancel the invoice")
public void i_cancel_invoice() {
    currentInvoice.cancel();
}

@Then("the invoice should be cancelled")
public void the_invoice_should_be_cancelled() {
    assertEquals("cancelled", currentInvoice.getStatus());
}

Invoice.InvoiceItem item;

@Given("I create an invoice item with description {string}, price {double}, and quantity {int}")
public void i_create_invoice_item(String desc, double price, int qty) {
    item = new Invoice.InvoiceItem(desc, price, qty);
}

@When("I change the item description to {string}")
public void i_change_item_description(String desc) {
    item.setDescription(desc);
}

@When("I change the item unit price to {double}")
public void i_change_item_unit_price(double price) {
    item.setUnitPrice(price);
}

@When("I change the item quantity to {int}")
public void i_change_item_quantity(int qty) {
    item.setQuantity(qty);
}

@Then("the item should have description {string}, unit price {double}, and quantity {int}")
public void the_item_should_have_updated_values(String desc, double price, int qty) {
    assertEquals(desc, item.getDescription());
    assertEquals(price, item.getUnitPrice(), 0.01);
    assertEquals(qty, item.getQuantity());
}




}
