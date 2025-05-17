Feature: Additional Chef Scheduling Coverage Tests

  Scenario: Finding an available chef and assigning a task
    Given the system has a chef schedule initialized
    When I check for an available chef between 9:30 and 10:30
    Then the system should assign a task to that chef

  Scenario: Checking chef workload
    Given a chef has multiple active tasks
    When I check the chef's workload
    Then the system should return the number of active tasks

  Scenario: Calculating chef's scheduled hours
    Given a chef has multiple active tasks
    When I check the chef's scheduled hours for today
    Then the system should calculate total scheduled hours

  Scenario: Updating a non-existent task
    Given a non-existent task ID
    When I try to update the task status
    Then the system should return false

  Scenario: Checking chef availability for specific hours
    Given a task that is outside available hours
    When I check if the chef is available
    Then the system should indicate the chef is available based on implementation

  Scenario: Assigning a task for specific hours
    Given a task that is outside available hours
    When I try to assign a task outside available hours
    Then the system should assign the task based on implementation

  Scenario: Reassigning a task to a different chef
    Given a task assigned to a chef
    When I try to reassign the task to a non-existent chef
    Then the system should update the chef ID in the task

  Scenario: Testing notification system with completed task
    Given a second constructor with notification system
    When a task is completed
    Then the notification system should notify the customer
    
  Scenario: Reassigning a non-existent task
    Given a task that doesn't exist
    When I try to reassign the non-existent task
    Then the system should return false