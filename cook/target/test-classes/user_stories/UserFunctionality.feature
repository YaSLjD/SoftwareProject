Feature: Core User Functionality

  Scenario: Updating the username
    Given a user is initialized with username "olduser", password "mypassword", and role "customer"
    When the user updates their username to "newuser"
    Then the user's username should be "newuser"

  Scenario: Updating the password
    Given a user is initialized with username "anyuser", password "oldpass", and role "customer"
    When the user updates their password to "newpass"
    Then the user's password should be "newpass"

  Scenario: Changing the user role
    Given a user is initialized with username "adminuser", password "pass123", and role "customer"
    When the user role is changed to "admin"
    Then the user should have the role "admin"

  Scenario: Removing a dietary preference
    Given a user is initialized with username "user1", password "pass", and role "customer"
    And the user has "Vegan" as a dietary preference
    When the user removes "Vegan" from their dietary preferences
    Then "Vegan" should not be in the dietary preferences

  Scenario: Removing an allergy
    Given a user is initialized with username "user2", password "pass", and role "customer"
    And the user has "Shellfish" listed as an allergy
    When the user removes "Shellfish" from their allergies
    Then "Shellfish" should not be in the allergy list
