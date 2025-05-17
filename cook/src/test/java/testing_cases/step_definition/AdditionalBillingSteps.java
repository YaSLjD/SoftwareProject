package testing_cases.step_definition;

import app.BillingSystem;
import app.Invoice;
import app.Notification;
import app.Order;
import app.NotificationSystem;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import java.util.List;
import java.util.HashMap;

import static org.junit.Assert.*;

public class AdditionalBillingSteps {
    
    private NotificationSystem notificationSystem;
    private BillingSystem billingSystem;
    private Order testOrder;
    private Invoice testInvoice;
    private String testCustomerId = "testuser";
    private List<Invoice> invoiceList;
    private boolean notificationSent = false;
    
    @Given("I initialize a billing system with only notification system")
    public void i_initialize_billing_system_with_only_notification_system() {
        // Create notification system with functionality to track notifications
        notificationSystem = new NotificationSystem();
        
        // Create a spy/wrapper for the notification system
        NotificationSystem originalSystem = notificationSystem;
        notificationSystem = new NotificationSystem() {
            @Override
            public Notification sendNotification(String userId, String title, String message, String category, String reference) {
                Notification result = originalSystem.sendNotification(userId, title, message, category, reference);
                notificationSent = true;
                return result;
            }
        };
        
        // Test the single parameter constructor
        billingSystem = new BillingSystem(notificationSystem);
        assertNotNull("Billing system should be created", billingSystem);
    }
    
    @When("I create and process an order with the billing system")
    public void i_create_and_process_order_with_billing_system() {
        // Create a mock order
        testOrder = new Order(testCustomerId, "testuser");
        
        // Generate invoice for the order
        testInvoice = billingSystem.generateInvoice(testOrder);
        assertNotNull("Invoice should be created", testInvoice);
    }
    
    @Then("the billing system should function correctly")
    public void the_billing_system_should_function_correctly() {
        // Verify invoice was stored in the system
        Invoice retrievedInvoice = billingSystem.getInvoice(testInvoice.getInvoiceId());
        assertNotNull("Should be able to retrieve the invoice", retrievedInvoice);
        assertEquals("Invoice should have correct customer ID", testCustomerId, retrievedInvoice.getCustomerId());
    }
    
    @Given("I have created an order with an invoice")
    public void i_have_created_order_with_invoice() {
        // Initialize components if not already initialized
        if (notificationSystem == null) {
            notificationSystem = new NotificationSystem();
            
            // Create a spy/wrapper for the notification system
            NotificationSystem originalSystem = notificationSystem;
            notificationSystem = new NotificationSystem() {
                @Override
                public Notification sendNotification(String userId, String title, String message, String category, String reference) {
                    Notification result = originalSystem.sendNotification(userId, title, message, category, reference);
                    notificationSent = true;
                    return result;
                }
            };
            
            billingSystem = new BillingSystem(notificationSystem);
        }
        
        // Create a mock order
        testOrder = new Order(testCustomerId, "ORD54321");
        
        // Generate invoice for the order
        testInvoice = billingSystem.generateInvoice(testOrder);
        assertNotNull("Invoice should be created", testInvoice);
    }
    
    @When("I retrieve all invoices for the order")
    public void i_retrieve_all_invoices_for_order() {
        // Test the getOrderInvoices method
        invoiceList = billingSystem.getOrderInvoices(testOrder.getOrderId());
        assertNotNull("Invoice list should not be null", invoiceList);
    }
    
    @Then("I should receive a list containing the invoice")
    public void i_should_receive_list_containing_invoice() {
        // Verify the list contains our invoice
        assertFalse("Invoice list should not be empty", invoiceList.isEmpty());
        boolean found = false;
        for (Invoice inv : invoiceList) {
            if (inv.getInvoiceId().equals(testInvoice.getInvoiceId())) {
                found = true;
                break;
            }
        }
        assertTrue("List should contain the created invoice", found);
    }
    
    @Given("I have an invoice with status {string}")
    public void i_have_invoice_with_status(String status) {
        // Initialize components if not already initialized
        if (notificationSystem == null) {
            notificationSystem = new NotificationSystem();
            
            // Create a spy/wrapper for the notification system
            NotificationSystem originalSystem = notificationSystem;
            notificationSystem = new NotificationSystem() {
                @Override
                public Notification sendNotification(String userId, String title, String message, String category, String reference) {
                    Notification result = originalSystem.sendNotification(userId, title, message, category, reference);
                    notificationSent = true;
                    return result;
                }
                
                @Override
                public Notification notifyNewInvoice(String userId, Invoice invoice) {
                    Notification result = originalSystem.notifyNewInvoice(userId, invoice);
                    notificationSent = true;
                    return result;
                }
                
                @Override
                public Notification notifyInvoicePaymentDue(String userId, Invoice invoice) {
                    Notification result = originalSystem.notifyInvoicePaymentDue(userId, invoice);
                    notificationSent = true;
                    return result;
                }
            };
            
            billingSystem = new BillingSystem(notificationSystem);
        }
        
        // Create a mock order
        testOrder = new Order(testCustomerId, "ORD67890");
        
        // Generate invoice for the order
        testInvoice = billingSystem.generateInvoice(testOrder);
        assertNotNull("Invoice should be created", testInvoice);
        
        // Set initial status
        billingSystem.updateInvoiceStatus(testInvoice.getInvoiceId(), status);
    }
    
    @When("I update the invoice status to {string}")
    public void i_update_invoice_status_to(String status) {
        // Reset notification flag
        notificationSent = false;
        
        // Update status
        boolean updateResult = billingSystem.updateInvoiceStatus(testInvoice.getInvoiceId(), status);
        assertTrue("Status update should succeed", updateResult);
    }
    
    @Then("a notification should be sent to the customer")
    public void a_notification_should_be_sent_to_customer() {
        // Verify notification was sent
        assertTrue("Notification should have been sent", notificationSent);
    }
    
    @Then("no notification should be sent to the customer")
    public void no_notification_should_be_sent_to_customer() {
        // Verify notification was not sent
        assertFalse("Notification should not have been sent", notificationSent);
    }
}