Feature: Ingredient Management

  Scenario: Creating an ingredient with basic details
    Given a new ingredient with ID "ING001", name "Vanilla Extract", unit "ml", quantity 500.0, and minimum stock level 100.0
    Then the ingredient ID should be "ING001"
    And the ingredient name should be "Vanilla Extract"
    And the ingredient unit should be "ml"
    And the ingredient quantity should be 500.0
    And the ingredient minimum stock level should be 100.0
    And the ingredient supplier ID should be null
    And the ingredient toString method should contain all ingredient details

  Scenario: Creating an ingredient with supplier details
    Given a new ingredient with ID "ING002", name "Chocolate Chips", unit "g", quantity 2000.0, minimum stock level 500.0, and supplier ID "SUP001"
    Then the ingredient ID should be "ING002"
    And the ingredient name should be "Chocolate Chips"
    And the ingredient unit should be "g"
    And the ingredient quantity should be 2000.0
    And the ingredient minimum stock level should be 500.0
    And the ingredient supplier ID should be "SUP001"
    And the ingredient toString method should contain all ingredient details

  Scenario: Testing all setter methods
    Given a new ingredient with ID "ING003", name "Baking Powder", unit "g", quantity 300.0, and minimum stock level 50.0
    When the ingredient ID is set to "ING003-UPDATED"
    And the ingredient name is set to "Baking Powder Premium"
    And the ingredient unit is set to "kg"
    And the ingredient quantity is set to 0.3
    And the ingredient minimum stock level is set to 0.05
    And the ingredient supplier ID is set to "SUP002"
    Then the ingredient ID should be "ING003-UPDATED"
    And the ingredient name should be "Baking Powder Premium"
    And the ingredient unit should be "kg"
    And the ingredient quantity should be 0.3
    And the ingredient minimum stock level should be 0.05
    And the ingredient supplier ID should be "SUP002"

  Scenario: Testing quantity update method
    Given a new ingredient with ID "ING004", name "Cinnamon", unit "g", quantity 100.0, and minimum stock level 20.0
    When the ingredient quantity is updated by 50.0
    Then the ingredient quantity should be 150.0
    When the ingredient quantity is updated by -30.0
    Then the ingredient quantity should be 120.0

  Scenario: Testing low stock detection at edge cases
    Given a new ingredient with ID "ING005", name "Nutmeg", unit "g", quantity 50.0, and minimum stock level 50.0
    Then the ingredient should be in low stock status
    When the ingredient quantity is set to 49.9
    Then the ingredient should be in low stock status
    When the ingredient quantity is set to 50.1
    Then the ingredient should not be in low stock status
    When the ingredient minimum stock level is set to 75.0
    Then the ingredient should be in low stock status