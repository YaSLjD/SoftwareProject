Feature: User Dietary Preferences and Allergies

  Scenario: Entering Dietary Preferences
    Given the user is logged into the app
    When the user selects "Vegetarian" as a preference
    Then the system should recommend only vegetarian meals

  Scenario: Adding Allergy Information
    Given the user is logged into the app
    When the user adds "Peanuts" as an allergy
    Then the system should exclude meals containing peanuts

  Scenario: Managing Multiple Preferences
    Given the user is logged into the app
    When the user selects "Gluten-free" and "Vegan"
    Then the system should recommend meals that match the selected preferences
