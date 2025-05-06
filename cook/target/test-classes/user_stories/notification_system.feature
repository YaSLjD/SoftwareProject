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
