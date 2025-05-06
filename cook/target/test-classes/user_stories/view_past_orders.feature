Feature: Track past orders and personalized meal plans

  Scenario: Customer views their past meal orders
    Given the customer is logged in
    When the customer navigates to the "Past Orders" section
    Then the customer should see a list of their past meal orders
    And each order should show the meal name, date, and price

  Scenario: Customer reorders a previously ordered meal
    Given the customer is logged in
    And the customer has previous meal orders
    When the customer selects a meal from their past orders
    Then the system should add that meal to the customer's current cart
    And the customer can proceed to checkout

  Scenario: Customer sees personalized meal recommendations based on past orders
    Given the customer is logged in
    When the customer views their personalized meal plan
    Then the system should display meal recommendations based on their past orders
    And the recommendations should be tailored to the customer's preferences
