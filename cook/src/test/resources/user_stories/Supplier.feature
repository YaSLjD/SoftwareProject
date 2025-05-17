Feature: Supplier Management Test Coverage

  Scenario: Create a supplier with basic information
    Given I create a supplier with ID "SUP001", name "Test Supplier", email "test@example.com", and phone "555-123-4567"
    Then the supplier ID should be "SUP001"
    And the supplier name should be "Test Supplier"
    And the supplier email should be "test@example.com"
    And the supplier phone should be "555-123-4567"
    And the supplier address should be null
    And the supplier should have 0 ingredients
    And the supplier toString should contain all the basic information

  Scenario: Update supplier information
    Given I create a supplier with ID "SUP002", name "Original Supplier", email "original@example.com", and phone "555-123-4567"
    When I set the supplier ID to "SUP002-UPDATED"
    And I set the supplier name to "Updated Supplier"
    And I set the supplier email to "updated@example.com"
    And I set the supplier phone to "555-987-6543"
    And I set the supplier address to "123 Test Street"
    Then the supplier ID should be "SUP002-UPDATED"
    And the supplier name should be "Updated Supplier"
    And the supplier email should be "updated@example.com"
    And the supplier phone should be "555-987-6543"
    And the supplier address should be "123 Test Street"

  Scenario: Create a supplier with all fields
    Given I initialize a supplier with ID "SUP003", name "Full Supplier", email "full@example.com", phone "555-123-9876", and address "456 Full Address Lane"
    Then the supplier ID should be "SUP003"
    And the supplier name should be "Full Supplier"
    And the supplier email should be "full@example.com"
    And the supplier phone should be "555-123-9876"
    And the supplier address should be "456 Full Address Lane"
    And the supplier should have 0 ingredients

  Scenario: Manage supplier ingredients
    Given I create a supplier with ID "SUP004", name "Ingredient Supplier", email "ingredients@example.com", and phone "555-456-7890"
    When I add ingredient "ING001" to the supplier
    Then the ingredient list should contain "ING001"
    And the supplier should have 1 ingredients
    When I add the same ingredient "ING001" again
    Then the supplier should have 1 ingredients
    When I add ingredient "ING002" to the supplier
    Then the supplier should have 2 ingredients
    When I remove a non-existent ingredient "ING999"
    Then the supplier should have 2 ingredients
    When the admin removes the ingredient "ING001" from the supplier
    Then the ingredient "ING001" should no longer be associated with the supplier
    And the supplier should have 1 ingredients