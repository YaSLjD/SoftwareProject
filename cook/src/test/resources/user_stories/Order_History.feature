Feature: Order history storage and retrieval

  Scenario: Administrator views all customer order history
    Given the administrator is logged in
    When the administrator navigates to the "Order History" section
    Then the administrator should see a list of all customer orders
    And each order should show the customer name, order date, and total price

  Scenario: Administrator filters order history by customer
    Given the administrator is logged in
    When the administrator selects to filter by customer "testuser"
    Then the administrator should only see orders placed by "testuser"

  Scenario: Administrator views order history statistics
    Given the administrator is logged in
    When the administrator views order statistics
    Then the administrator should see summary data including total orders and average order value
