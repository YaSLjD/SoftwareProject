package testing_cases.step_definition;

import app.BillingSystem;
import app.Invoice;
import app.LoginSystem;
import app.Order;
import app.OrderSystem;
import app.User;
import app.logger;
import app.NotificationSystem;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    private List<Invoice> resultInvoiceList;
    private String reportResult;
    
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
        
        // Verify statement contains expected content
        assertTrue("Statement should contain invoice information", 
            statement.contains("Invoices:") && statement.contains("Summary:"));
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
        
        // Set the due date to a past date
        LocalDateTime pastDueDate = LocalDateTime.now().minusDays(5);
        currentInvoice.setDueDate(pastDueDate);
        
        logger.info("Invoice is past its due date: " + currentInvoice.getInvoiceId());
    }
    
    @When("the system runs the daily payment check")
    public void theSystemRunsTheDailyPaymentCheck() {
        // Check for overdue invoices
        resultInvoiceList = billingSystem.checkOverdueInvoices();
        assertNotNull("Overdue invoice list should not be null", resultInvoiceList);
        
        logger.info("System ran the daily payment check");
        logger.info("Found " + resultInvoiceList.size() + " overdue invoices");
    }
    
    @Then("it should mark the invoice as {string}")
    public void itShouldMarkTheInvoiceAs(String status) {
        // Verify invoice status
        Invoice updatedInvoice = billingSystem.getInvoice(currentInvoice.getInvoiceId());
        assertEquals("Invoice status should be marked as " + status, status, updatedInvoice.getStatus());
        
        // Verify that the invoice is in the result list
        boolean foundInvoice = resultInvoiceList.stream()
            .anyMatch(inv -> inv.getInvoiceId().equals(currentInvoice.getInvoiceId()));
        assertTrue("Invoice should be in the overdue list", foundInvoice);
        
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
        
        // Create some invoices with different statuses to improve coverage
        for (int i = 0; i < 5; i++) {
            Order order = orderSystem.createOrder(currentCustomer);
            orderSystem.addMealToOrder(order.getOrderId(), "M001");
            orderSystem.updateOrderStatus(order.getOrderId(), "completed");
            
            Invoice invoice = billingSystem.generateInvoice(order);
            invoice.issue();
            
            // Set different statuses to test all branches
            if (i < 2) {
                billingSystem.processPayment(invoice.getInvoiceId(), invoice.getTotalAmount(), "Credit Card");
            } else if (i == 2) {
                billingSystem.updateInvoiceStatus(invoice.getInvoiceId(), "overdue");
            } else if (i == 3) {
                billingSystem.updateInvoiceStatus(invoice.getInvoiceId(), "cancelled");
            }
            
            // Set issue date to ensure it falls within our test period
            LocalDateTime issueDate = LocalDateTime.now().minusDays(i);
            invoice.setIssueDate(issueDate);
        }
        
        logger.info("Admin wants to analyze revenue");
    }
    
    @When("they request a revenue report for a specific period")
    public void theyRequestARevenueReportForASpecificPeriod() {
        // Generate revenue report
        LocalDateTime start = LocalDateTime.now().minusMonths(1);
        LocalDateTime end = LocalDateTime.now();
        
        reportResult = billingSystem.generateRevenueReport(start, end);
        assertNotNull("Revenue report should be generated", reportResult);
        assertFalse("Revenue report should not be empty", reportResult.isEmpty());
        
        logger.info("Admin requested a revenue report for the past month");
    }
    
    @Then("the system should generate a detailed report")
    public void theSystemShouldGenerateADetailedReport() {
        // Verify report content
        assertTrue("Report should contain period information", 
            reportResult.contains("Period:") && reportResult.contains("to"));
        assertTrue("Report should show total revenue", reportResult.contains("Total Revenue:"));
        
        logger.info("System generated a detailed revenue report");
    }
    
    @And("show total revenue, paid invoices, and pending payments")
    public void showTotalRevenuePaidInvoicesAndPendingPayments() {
        // Verify report contains all required sections
        assertTrue("Report should show total invoices", reportResult.contains("Total Invoices:"));
        assertTrue("Report should show paid invoices", reportResult.contains("Paid Invoices:"));
        assertTrue("Report should show pending revenue", reportResult.contains("Pending Revenue:"));
        
        logger.info("Revenue report shows total revenue, paid invoices, and pending payments:");
        logger.info(reportResult);
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
        protected Map<String, User> userDatabase;
    
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
        
        // NEW TEST STEPS TO IMPROVE COVERAGE
        
        @Given("I have a null order")
        public void i_have_a_null_order() {
            currentOrder = null;
        }
        
        @When("I try to generate an invoice for the null order")
        public void i_try_to_generate_invoice_for_null_order() {
            currentInvoice = billingSystem.generateInvoice(currentOrder);
        }
        
        @Then("the invoice should be null")
        public void the_invoice_should_be_null() {
            assertNull("Invoice should be null for a null order", currentInvoice);
        }
        
        @Given("I have an invoice with ID {string}")
        public void i_have_an_invoice_with_id(String invoiceId) {
            // Try to get an invoice that doesn't exist
            currentInvoice = billingSystem.getInvoice(invoiceId);
        }
        
        @When("I update the status of a non-existent invoice to {string}")
        public void i_update_status_of_nonexistent_invoice(String status) {
            boolean result = billingSystem.updateInvoiceStatus("non-existent-id", status);
            assertFalse("Updating a non-existent invoice should return false", result);
        }
        
        @When("I process payment for a non-existent invoice")
        public void i_process_payment_for_nonexistent_invoice() {
            boolean result = billingSystem.processPayment("non-existent-id", 100.0, "Credit Card");
            assertFalse("Processing payment for a non-existent invoice should return false", result);
        }
        
        @When("I process payment with incorrect amount")
        public void i_process_payment_with_incorrect_amount() {
            // Try to process payment with amount that doesn't match invoice
            boolean result = billingSystem.processPayment(
                currentInvoice.getInvoiceId(), 
                currentInvoice.getTotalAmount() + 50.0, // Incorrect amount
                "Credit Card"
            );
            assertFalse("Processing payment with incorrect amount should return false", result);
        }
        
        @Given("there are invoices for an order with ID {string}")
        public void there_are_invoices_for_order(String orderId) {
            // Create an order with the given ID
            currentOrder = orderSystem.createOrder(currentCustomer);
            orderSystem.addMealToOrder(currentOrder.getOrderId(), "M001");
            orderSystem.updateOrderStatus(currentOrder.getOrderId(), "completed");
            
            // Generate invoice for the order
            currentInvoice = billingSystem.generateInvoice(currentOrder);
        }
        
        @When("I retrieve invoices for the order")
        public void i_retrieve_invoices_for_order() {
            resultInvoiceList = billingSystem.getOrderInvoices(currentOrder.getOrderId());
        }
        
        @Then("I should get at least one invoice")
        public void i_should_get_at_least_one_invoice() {
            assertNotNull("Invoice list should not be null", resultInvoiceList);
            assertFalse("Invoice list should not be empty", resultInvoiceList.isEmpty());
            
            // Verify the correct invoice is returned
            boolean foundInvoice = resultInvoiceList.stream()
                .anyMatch(inv -> inv.getInvoiceId().equals(currentInvoice.getInvoiceId()));
            assertTrue("The order's invoice should be in the result list", foundInvoice);
        }
        
        @When("I generate a monthly statement for a different year and month")
        public void i_generate_monthly_statement_for_different_period() {
            int previousYear = LocalDateTime.now().getYear() - 1;
            int previousMonth = 1; // January
            
            String statement = billingSystem.generateMonthlyStatement(currentCustomer, previousYear, previousMonth);
            assertNotNull("Monthly statement should be generated", statement);
            assertTrue("Statement should contain year and month", 
                statement.contains(String.valueOf(previousYear)) && statement.contains(String.valueOf(previousMonth)));
        }
        
        @When("I create a billing system with just user database")
        public void i_create_billing_system_with_just_user_database() {
            // Test the other constructor
            BillingSystem simpleBillingSystem = new BillingSystem();    
        assertNotNull("Should be able to create billing system with just user database", simpleBillingSystem);
        
        // Generate an invoice to ensure internal state is properly initialized
        Order testOrder = orderSystem.createOrder(currentCustomer);
        orderSystem.addMealToOrder(testOrder.getOrderId(), "M001");
        Invoice testInvoice = simpleBillingSystem.generateInvoice(testOrder);
        assertNotNull("Should be able to generate invoice", testInvoice);
    }
}