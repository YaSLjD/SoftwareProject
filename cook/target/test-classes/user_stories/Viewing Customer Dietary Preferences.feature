Feature: Viewing Customer Dietary Preferences

  Scenario: Viewing Customer Dietary Preferences
    Given the chef is logged into the system
    When the chef views the customer's profile
    Then the chef should see the customer's dietary preferences and allergies
    And the chef should be able to customize meals based on those preferences

