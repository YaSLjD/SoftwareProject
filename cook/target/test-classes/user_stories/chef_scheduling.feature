Feature: Chef Scheduling and Task Management

  Scenario: Assigning tasks to chefs
    Given there are multiple chefs available in the system
    When an order is placed with preparation time of 45 minutes
    Then the system should assign the order to an available chef
    And add the task to the chef's schedule

  Scenario: Viewing chef's daily schedule
    Given a chef is logged into the system
    When they request to view their schedule for today
    Then the system should display all assigned tasks for the day
    And show the start time and estimated completion time for each task

  Scenario: Marking tasks as completed
    Given a chef has tasks assigned for the day
    When they mark a task as completed
    Then the system should update the task status
    And notify the customer that their order is ready

  Scenario: Reassigning tasks between chefs
    Given a chef is unavailable for their assigned task
    When an admin reassigns the task to another chef
    Then the system should update the task assignment
    And notify both chefs about the change

  Scenario: Prioritizing urgent orders
    Given there are multiple orders in the system
    When an admin marks an order as "urgent"
    Then the system should assign a high priority to the related tasks
    And schedule them before non-urgent tasks

  Scenario: Handling chef availability
    Given a chef sets their availability for the week
    When a new order comes in during their available hours
    Then the system should consider the chef for task assignment
    But not assign tasks during their unavailable hours
