Feature: Inventory Management

  Scenario: Adding new ingredients to inventory
    Given the admin is logged into the system
    When they add a new ingredient with details:
      | name      | Flour       |
      | unit      | kg          |
      | quantity  | 25          |
      | minLevel  | 5           |
      | supplierId| SUP001      |
    Then the ingredient should be added to the inventory
    And the current stock level should be 25 kg

  Scenario: Checking low stock ingredients
    Given the inventory has the following ingredients:
      | name      | quantity | minLevel |
      | Flour     | 3        | 5        |
      | Sugar     | 10       | 5        |
      | Salt      | 2        | 1        |
    When the admin checks for low stock ingredients
    Then the system should identify "Flour" as low stock
    And the system should not identify "Sugar" as low stock
    And the system should not identify "Salt" as low stock

  Scenario: Updating inventory after order preparation
    Given an order with the following ingredients:
      | name      | quantity |
      | Flour     | 0.5      |
      | Sugar     | 0.25     |
    When the chef marks the order as prepared
    Then the inventory should be updated with reduced quantities
    And the new quantity of "Flour" should be 2.5 kg
    And the new quantity of "Sugar" should be 9.75 kg

  Scenario: Receiving new inventory from supplier
    Given a purchase order has been placed with a supplier
    When the admin records the receipt of new inventory
    Then the system should update the inventory quantities
    And record the transaction in the inventory history

  Scenario: Setting up automatic reordering
    Given the admin has configured automatic reordering for "Flour"
    When the "Flour" inventory falls below the minimum level
    Then the system should automatically generate a purchase order
    And notify the admin about the automatic reorder

  Scenario: Tracking ingredient usage over time
    Given the system has recorded ingredient usage for the past month
    When the admin requests an ingredient usage report
    Then the system should display usage statistics for each ingredient
    And highlight the most frequently used ingredients
