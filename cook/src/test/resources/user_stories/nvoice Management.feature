Feature: Invoice Management

  Scenario: Creating invoice with specific details
    Given I create an invoice with specific ID, order ID, and total amount
    Then the invoice should have the specified ID, order ID, and total amount

  Scenario: Calculating invoice totals with multiple items
    Given I create an invoice with specific ID, order ID, and total amount
    When I add multiple items to the invoice
    Then the invoice subtotal should be calculated correctly
    And the invoice tax amount should be calculated correctly
    And the invoice total amount should be updated correctly

  Scenario: Removing items from an invoice
    Given I create an invoice with specific ID, order ID, and total amount
    When I add multiple items to the invoice
    And I remove an item from the invoice at index 1
    Then the invoice should have 2 items

  Scenario: Handling invalid item removal
    Given I create an invoice with specific ID, order ID, and total amount
    When I add multiple items to the invoice
    And I try to remove an item with invalid index 5
    Then the invoice should have 3 items

  Scenario: Updating tax rate
    Given I create an invoice with specific ID, order ID, and total amount
    When I add multiple items to the invoice
    And I set a new tax rate of 0.15
    Then the invoice tax rate should be 0.15
    And the invoice tax amount should be recalculated to 8.25

  Scenario: Cannot cancel a paid invoice
    Given I create an invoice with specific ID, order ID, and total amount
    When I mark the invoice as paid
    Then I should not be able to cancel the invoice

  Scenario: Issue a draft invoice
    Given I have a draft invoice
    When I issue the invoice
    Then the invoice status should change to "issued"

  Scenario: Checking non-overdue invoice
    Given I have an issued invoice
    When I check if the invoice is overdue and it isn't
    Then the invoice overdue status should be "issued"