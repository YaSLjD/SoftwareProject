Feature: Custom meal requests with ingredient selection

  Scenario: Customer selects ingredients to customize a meal
    Given the customer is logged in to customize a meal
    And there is a customizable meal available
    When the customer selects the meal for customization
    And the customer adds ingredient "Chicken" to the meal
    And the customer adds ingredient "Brown Rice" to the meal
    And the customer removes ingredient "White Rice" from the meal
    Then the meal should contain ingredient "Chicken"
    And the meal should contain ingredient "Brown Rice"
    And the meal should not contain ingredient "White Rice"
    And the price should be updated accordingly

  Scenario: Customer tries to select incompatible ingredients
    Given the customer is logged in to customize a meal
    And there is a customizable meal available
    When the customer selects the meal for customization
    And the customer adds ingredient "Shrimp" to the meal
    And the customer attempts to add incompatible ingredient "Chocolate Sauce"
    Then the system should display an incompatibility warning
    And the meal should not contain ingredient "Chocolate Sauce"

  Scenario: Customer tries to select unavailable ingredients
    Given the customer is logged in to customize a meal
    And there is a customizable meal available
    When the customer selects the meal for customization
    And the customer attempts to add unavailable ingredient "Truffles"
    Then the system should display an unavailability message
    And the meal should not contain ingredient "Truffles"

  Scenario: Customer completes a custom meal order
    Given the customer is logged in to customize a meal
    And the customer has customized a meal
    When the customer places the order
    Then the order should be created with the customized meal
    And the order should appear in the customer's order history
