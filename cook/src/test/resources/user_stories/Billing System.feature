Feature: Additional Billing System Tests

  Scenario: Testing BillingSystem constructor with only NotificationSystem
    Given I initialize a billing system with only notification system
    When I create and process an order with the billing system
    Then the billing system should function correctly

  Scenario: Testing retrieval of invoices by order ID
    Given I have created an order with an invoice
    When I retrieve all invoices for the order
    Then I should receive a list containing the invoice

  Scenario: Testing notifications when invoice status is updated to paid
    Given I have an invoice with status "issued"
    When I update the invoice status to "paid"
    Then a notification should be sent to the customer

  Scenario: Testing no notifications when invoice status is updated to non-paid status
    Given I have an invoice with status "issued"
    When I update the invoice status to "overdue"
    Then no notification should be sent to the customer