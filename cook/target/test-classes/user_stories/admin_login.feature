Feature: Login

  Scenario: Successfully logging in with correct credentials
    Given the user is on the login page
    When the user enters the username "testuser" and the password "password123"
    And clicks the "Login" button
    Then the user should be redirected to the homepage successfully
    And a welcome message "Welcome testuser" should be displayed

  Scenario: Failed login due to incorrect password
    Given the user is on the login page
    When the user enters the username "testuser" and the password "wrongpassword"
    And clicks the "Login" button
    Then an error message "Invalid username or password" should be displayed
    And the user should remain on the login page

  Scenario: Failed login due to non-existent username
    Given the user is on the login page
    When the user enters the username "nonexistentuser" and the password "password123"
    And clicks the "Login" button
    Then an error message "Invalid username or password" should be displayed
    And the user should remain on the login page
