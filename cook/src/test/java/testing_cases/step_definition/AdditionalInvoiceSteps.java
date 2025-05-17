package testing_cases.step_definition;

import app.Invoice;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.*;

public class AdditionalInvoiceSteps {
    
    private Invoice invoice;
    private String testInvoiceId;
    private String testOrderId;
    private double testAmount;
    
    @Given("I create an invoice with specific ID, order ID, and total amount")
    public void i_create_invoice_with_specific_details() {
        testInvoiceId = "INV-" + UUID.randomUUID().toString().substring(0, 8);
        testOrderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        testAmount = 100.00;
        invoice = new Invoice(testInvoiceId, testOrderId, testAmount);
    }
    
    @Then("the invoice should have the specified ID, order ID, and total amount")
    public void invoice_should_have_specified_details() {
        assertEquals(testInvoiceId, invoice.getInvoiceId());
        assertEquals(testOrderId, invoice.getOrderId());
        assertEquals(testAmount, invoice.getTotalAmount(), 0.01);
        assertEquals("defaultCustomer", invoice.getCustomerId());
    }
    
    @When("I add multiple items to the invoice")
    public void i_add_multiple_items_to_invoice() {
        invoice.addItem("Item 1", 10.0, 2);
        invoice.addItem("Item 2", 15.0, 1);
        invoice.addItem("Item 3", 5.0, 4);
    }
    
    @Then("the invoice subtotal should be calculated correctly")
    public void invoice_subtotal_should_be_calculated_correctly() {
        // 10.0 * 2 + 15.0 * 1 + 5.0 * 4 = 20 + 15 + 20 = 55
        assertEquals(55.0, invoice.getSubtotal(), 0.01);
    }
    
    @And("the invoice tax amount should be calculated correctly")
    public void invoice_tax_amount_should_be_calculated_correctly() {
        // Default tax rate is 0.1, so tax amount = 55.0 * 0.1 = 5.5
        assertEquals(5.5, invoice.getTaxAmount(), 0.01);
    }
    
    @And("the invoice total amount should be updated correctly")
    public void invoice_total_amount_should_be_updated_correctly() {
        // subtotal + tax = 55.0 + 5.5 = 60.5
        assertEquals(60.5, invoice.getTotalAmount(), 0.01);
    }
    
    @When("I remove an item from the invoice at index {int}")
    public void i_remove_item_from_invoice_at_index(int index) {
        invoice.removeItem(index);
    }
    
    @Then("the invoice should have {int} items")
    public void invoice_should_have_expected_items(int expectedCount) {
        assertEquals(expectedCount, invoice.getItems().size());
    }
    
    @When("I try to remove an item with invalid index {int}")
    public void i_try_to_remove_item_with_invalid_index(int index) {
        boolean result = invoice.removeItem(index);
        assertFalse("Removing item with invalid index should return false", result);
    }
    
    @When("I set a new tax rate of {double}")
    public void i_set_new_tax_rate(double taxRate) {
        invoice.setTaxRate(taxRate);
    }
    
    @Then("the invoice tax rate should be {double}")
    public void invoice_tax_rate_should_be(double expectedTaxRate) {
        assertEquals(expectedTaxRate, invoice.getTaxRate(), 0.01);
    }
    
    @And("the invoice tax amount should be recalculated to {double}")
    public void invoice_tax_amount_should_be_recalculated(double expectedTaxAmount) {
        assertEquals(expectedTaxAmount, invoice.getTaxAmount(), 0.01);
    }
    
    @When("I mark the invoice as paid")
    public void i_mark_invoice_as_paid() {
        invoice.issue(); // First ensure it's issued
        invoice.markAsPaid();
    }
    
    @Then("I should not be able to cancel the invoice")
    public void i_should_not_be_able_to_cancel_invoice() {
        String previousStatus = invoice.getStatus();
        invoice.cancel();
        assertEquals("Invoice status should not change when trying to cancel a paid invoice", 
                previousStatus, invoice.getStatus());
    }
    
    @Given("I have a draft invoice")
    public void i_have_a_draft_invoice() {
        invoice = new Invoice("ORDER123", "CUSTOMER456");
        assertEquals("draft", invoice.getStatus());
    }
    
    @When("I issue the invoice")
    public void i_issue_the_invoice() {
        invoice.issue();
    }
    
    @Then("the invoice status should change to {string}")
    public void invoice_status_should_change_to(String expectedStatus) {
        assertEquals(expectedStatus, invoice.getStatus());
    }
    
    @Given("I have an issued invoice")
    public void i_have_an_issued_invoice() {
        invoice = new Invoice("ORDER123", "CUSTOMER456");
        invoice.issue();
        assertEquals("issued", invoice.getStatus());
    }
    
    @When("I check if the invoice is overdue and it isn't")
    public void i_check_if_invoice_is_overdue_and_it_isnt() {
        // Set due date to future to ensure it's not overdue
        invoice.setDueDate(LocalDateTime.now().plusDays(10));
        boolean isOverdue = invoice.isOverdue();
        assertFalse("Invoice should not be overdue", isOverdue);
    }
    
    @Then("the invoice overdue status should be {string}")
    public void invoice_overdue_status_should_be(String expectedStatus) {
        assertEquals(expectedStatus, invoice.getStatus());
    }
}