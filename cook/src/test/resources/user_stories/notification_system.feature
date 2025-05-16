Feature: Notification System

  Scenario: Notifying customer about order status change
    Given a customer has placed an order for notification
    When the order status changes to "in preparation"
    Then the system should send a notification to the customer
    And include the updated status and estimated completion time

  Scenario: Notifying chef about new task assignment
    Given a chef is available in the system
    When a new order is assigned to the chef
    Then the system should send a notification to the chef
    And include the order details and preparation requirements

  Scenario: Alerting admin about low inventory
    Given the inventory system detects low stock of ingredients
    When the stock falls below the minimum threshold
    Then the system should send an alert to the admin
    And include the list of low stock ingredients

  Scenario: Sending invoice notifications
    Given an invoice has been generated for a customer
    When the system processes the invoice for notification
    Then the customer should receive a notification about the new invoice
    And the notification should include the invoice amount and due date

  Scenario: Payment reminder notifications
    Given an invoice is approaching its due date
    When the system runs the daily payment reminder check
    Then it should send a payment reminder to the customer
    And include the invoice details and payment options

  Scenario: Marking notifications as read
    Given a user has received multiple notifications
    When they view a notification
    Then the system should mark that notification as read
    And update the unread notification count

  Scenario: Notification preferences
    Given a user wants to customize their notification settings
    When they update their notification preferences
    Then the system should only send notifications according to their preferences
    And respect their preferred notification channels

  Scenario: Batch notification processing
    Given multiple notifications need to be sent
    When the system processes the notification queue
    Then it should send all pending notifications
    And record the delivery status for each notification

Scenario: Filtering notifications by type
    Given a user has multiple notifications of different types
    When they request notifications of type "billing"
    Then only billing notifications should be returned

  Scenario: Get notifications related to a specific entity
    Given a user has notifications linked to specific entities
    When they filter by related entity ID "INV123"
    Then only notifications related to "INV123" should be returned

  Scenario: Get notifications in date range
    Given a user has notifications with different timestamps
    When they request notifications between "2025-05-01T00:00" and "2025-05-15T23:59"
    Then only notifications within that range should be returned

  Scenario: Low inventory with no items
    Given the inventory is checked with no low-stock ingredients
    When the system evaluates inventory
    Then no notifications should be sent

  Scenario: Detailed low inventory alert
    Given the inventory has low stock of "Tomato" with quantity 2 below minimum 5
    When the system generates the alert message
    Then the message should include the ingredient name, quantity, unit, and minimum level

  Scenario: Notification message for new invoice
    Given an invoice with ID "INV001" and amount "30.00" exists
    When the notification is created for the invoice
    Then the message should include the invoice ID and amount

  Scenario: Meal plan recommendation notification
    Given a user has a personalized meal plan with ID "MP001"
    When the system sends a meal plan recommendation
    Then the user should receive a notification with the correct title and message
    
    Scenario: Modify a notification and verify its properties
  Given a new notification is created
  When the title is updated to "Updated Title"
  And the message is updated to "Updated Message"
  And it is marked as unread
  And the related entity ID is set to "ENT999"
  Then the notification should reflect the updated values
  And toString should return a string containing "Updated Title"
