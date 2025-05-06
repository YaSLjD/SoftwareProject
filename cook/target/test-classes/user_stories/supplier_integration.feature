Feature: Supplier Integration

  Scenario: Adding a new supplier
    Given the admin is logged into the system
    When they add a new supplier with details:
      | id        | SUP003      |
      | name      | Fresh Foods |
      | email     | contact@freshfoods.com |
      | phone     | 555-123-4567 |
      | address   | 123 Market St, City |
    Then the supplier should be added to the system
    And be available for ingredient sourcing

  Scenario: Associating ingredients with suppliers
    Given a supplier "Organic Goods" exists in the system
    When the admin associates the following ingredients with the supplier:
      | Organic Flour |
      | Organic Sugar |
      | Organic Eggs  |
    Then these ingredients should be linked to the supplier
    And the system should know the source for these ingredients

  Scenario: Generating a purchase order
    Given the inventory shows low stock for several ingredients
    When the admin generates a purchase order for a supplier
    Then the system should create a purchase order with the required quantities
    And send the purchase order to the supplier

  Scenario: Tracking order status from suppliers
    Given a purchase order has been sent to a supplier
    When the supplier updates the order status to "shipped"
    Then the system should update the purchase order status
    And notify the admin about the shipment

  Scenario: Receiving and verifying deliveries
    Given a supplier delivery has arrived
    When the admin records the received items
    Then the system should compare the received items with the purchase order
    And highlight any discrepancies

  Scenario: Managing supplier performance
    Given the system has tracked supplier deliveries for the past quarter
    When the admin requests a supplier performance report
    Then the system should display metrics for each supplier including:
      | On-time delivery rate |
      | Order accuracy        |
      | Price competitiveness |
      | Quality rating        |

  Scenario: Setting preferred suppliers for ingredients
    Given multiple suppliers provide the same ingredient
    When the admin marks a supplier as "preferred" for an ingredient
    Then the system should prioritize that supplier for future orders
    And use alternative suppliers only when necessary
