Feature: Billing and Invoicing

  Scenario: Generating an invoice for an order
    Given a customer has placed an order
    When the order is completed
    Then the system should automatically generate an invoice
    And include all order items with correct prices

  Scenario: Sending invoice to customer
    Given an invoice has been generated for an order
    When the system processes the invoice
    Then the customer should receive a notification with the invoice details
    And be able to view the full invoice

  Scenario: Processing payment for an invoice
    Given a customer has received an invoice
    When they make a payment using a credit card
    Then the system should process the payment
    And update the invoice status to "paid"
    And send a payment confirmation to the customer

  Scenario: Handling partial payments
    Given a customer has a large invoice
    When they make a partial payment
    Then the system should record the partial payment
    And update the remaining balance
    And maintain the invoice status as "partially paid"

  Scenario: Generating monthly statements
    Given a customer has multiple invoices in a month
    When the end of month is reached
    Then the system should generate a monthly statement
    And include all invoices with their payment status

  Scenario: Handling overdue invoices
    Given an invoice is past its due date
    When the system runs the daily payment check
    Then it should mark the invoice as "overdue"
    And send a payment reminder to the customer

  Scenario: Applying discounts to invoices
    Given a customer qualifies for a discount
    When an invoice is generated for their order
    Then the system should apply the appropriate discount
    And clearly show the discount on the invoice

  Scenario: Generating revenue reports
    Given the admin wants to analyze revenue
    When they request a revenue report for a specific period
    Then the system should generate a detailed report
    And show total revenue, paid invoices, and pending payments

    Scenario: Manually setting issue and due date
  Given a customer has placed an order
  When the order is completed
  And I manually set the issue date to "2023-01-01T10:00:00"
  And I manually set the due date to "2023-01-31T10:00:00"
  Then the invoice status should be "draft"


