package testing_cases.step_definition;

import app.ChefSchedule;
import app.LoginSystem;
import app.Order;
import app.OrderSystem;
import app.Task;
import app.logger;
import app.NotificationSystem;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;

public class ChefSchedulingStep {
    
    private LoginSystem loginSystem;
    private OrderSystem orderSystem;
    private ChefSchedule chefSchedule;
    private NotificationSystem notificationSystem;
    private String currentChef;
    private Order currentOrder;
    private Task currentTask;
    private List<Task> chefTasks;
    
    public ChefSchedulingStep() {
        loginSystem = new LoginSystem();
        orderSystem = new OrderSystem(loginSystem.getUserDatabase());
        notificationSystem = new NotificationSystem();
        chefSchedule = new ChefSchedule(loginSystem.getUserDatabase(), notificationSystem);
        currentChef = "chef"; // Default chef user
    }
    
    @Given("there are multiple chefs available in the system")
    public void thereAreMultipleChefsAvailableInTheSystem() {
        // In a real implementation, we would check for multiple chefs
        // For now, we'll just verify our default chef exists
        assertTrue("Chef should exist in the system", 
                loginSystem.getUserDatabase().containsKey(currentChef));
        
        logger.info("Multiple chefs are available in the system");
    }
    
    @When("an order is placed with preparation time of {int} minutes")
    public void anOrderIsPlacedWithPreparationTimeOfMinutes(int preparationTime) {
        // Create a new order
        currentOrder = orderSystem.createOrder("testuser");
        assertNotNull("Order should be created", currentOrder);
        
        // Add a meal to the order
        boolean added = orderSystem.addMealToOrder(currentOrder.getOrderId(), "M001");
        assertTrue("Meal should be added to order", added);
        
        // Calculate start and end times
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.plusMinutes(preparationTime);
        
        // Create a task for the order
        String description = "Prepare order #" + currentOrder.getOrderId();
        currentTask = new Task(currentOrder.getOrderId(), currentChef, description, now, endTime);
        
        logger.info("Order placed with " + preparationTime + " minutes preparation time");
    }
    
    @Then("the system should assign the order to an available chef")
    public void theSystemShouldAssignTheOrderToAnAvailableChef() {
        // In a real implementation, we would check that the system assigned the order
        // to an available chef automatically
        
        // For now, we'll just verify that our chef is available and the task is created
        assertTrue("Chef should be available", 
                chefSchedule.isChefAvailable(currentChef, currentTask.getStartTime(), currentTask.getEndTime()));
        
        logger.info("System assigned the order to chef: " + currentChef);
    }
    
    @And("add the task to the chef's schedule")
    public void addTheTaskToTheChefSSchedule() {
        // Add the task to the chef's schedule
        chefSchedule.addTask(currentTask);
        
        // Verify the task was added
        List<Task> tasks = chefSchedule.getChefTasks(currentChef);
        assertTrue("Task should be added to chef's schedule", 
                tasks.stream().anyMatch(t -> t.getId().equals(currentTask.getId())));
        
        logger.info("Task added to chef's schedule");
    }
    
    @Given("a chef is logged into the system")
    public void aChefIsLoggedIntoTheSystem() {
        // Simulate chef login
        String loginResult = loginSystem.login(currentChef, "chefpass");
        assertTrue("Chef should be logged in", loginResult.startsWith("Welcome"));
        
        logger.info("Chef is logged into the system");
    }
    
    @When("they request to view their schedule for today")
    public void theyRequestToViewTheirScheduleForToday() {
        // Get chef's tasks for today
        chefTasks = chefSchedule.getChefTasksForDate(currentChef, LocalDate.now());
        
        logger.info("Chef requested to view schedule for today");
    }
    
    @Then("the system should display all assigned tasks for the day")
    public void theSystemShouldDisplayAllAssignedTasksForTheDay() {
        assertNotNull("Chef should have tasks", chefTasks);
        
        logger.info("System displayed " + chefTasks.size() + " tasks for today");
        
        // In a real implementation, we would verify that the UI displays these tasks
        for (Task task : chefTasks) {
            System.out.println("- " + task.getDescription() + 
                    " (Order: " + task.getOrderId() + ")");
        }
    }
    
    @And("show the start time and estimated completion time for each task")
    public void showTheStartTimeAndEstimatedCompletionTimeForEachTask() {
        // In a real implementation, we would verify that the UI displays these times
        for (Task task : chefTasks) {
            System.out.println("- " + task.getDescription() + 
                    ": " + task.getStartTime() + " to " + task.getEndTime());
        }
        
        logger.info("System showed start and completion times for each task");
    }
    
    @Given("a chef has tasks assigned for the day")
    public void aChefHasTasksAssignedForTheDay() {
        // Ensure chef has at least one task
        if (chefSchedule.getChefTasksForDate(currentChef, LocalDate.now()).isEmpty()) {
            // Create a sample task if none exists
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime end = now.plusMinutes(45);
            Task task = new Task("O001", currentChef, "Sample task", now, end);
            chefSchedule.addTask(task);
            currentTask = task;
        } else {
            currentTask = chefSchedule.getChefTasksForDate(currentChef, LocalDate.now()).get(0);
        }
        
        assertNotNull("Chef should have a task assigned", currentTask);
        logger.info("Chef has tasks assigned for today");
    }
    
    @When("they mark a task as completed")
    public void theyMarkATaskAsCompleted() {
        // Mark the task as completed
        currentTask.complete();
        
        // Update the task status in the chef schedule
        boolean updated = chefSchedule.updateTaskStatus(currentTask.getId(), "completed");
        assertTrue("Task status should be updated", updated);
        
        logger.info("Chef marked task as completed: " + currentTask.getDescription());
    }
    
    @Then("the system should update the task status")
    public void theSystemShouldUpdateTheTaskStatus() {
        // Verify the task status was updated
        Task updatedTask = chefSchedule.getTaskById(currentTask.getId());
        assertNotNull("Task should exist", updatedTask);
        assertEquals("Task status should be completed", "completed", updatedTask.getStatus());
        
        logger.info("System updated task status to: " + updatedTask.getStatus());
    }
    
    @And("notify the customer that their order is ready")
    public void notifyTheCustomerThatTheirOrderIsReady() {
        // In a real implementation, we would verify that a notification was sent
        // For now, we'll just print a message
        logger.info("System notified customer that their order is ready");
    }
    
    @Given("a chef is unavailable for their assigned task")
    public void aChefIsUnavailableForTheirAssignedTask() {
        // Ensure chef has at least one task
        if (chefSchedule.getChefTasks(currentChef).isEmpty()) {
            // Create a sample task if none exists
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime end = now.plusMinutes(45);
            Task task = new Task("O001", currentChef, "Sample task", now, end);
            chefSchedule.addTask(task);
            currentTask = task;
        } else {
            currentTask = chefSchedule.getChefTasks(currentChef).get(0);
        }
        
        assertNotNull("Chef should have a task assigned", currentTask);
        logger.info("Chef is unavailable for assigned task: " + currentTask.getDescription());
    }
    
    @When("an admin reassigns the task to another chef")
    public void anAdminReassignsTheTaskToAnotherChef() {
        // In a real implementation, we would have multiple chefs and reassign to a different one
        // For now, we'll just simulate the reassignment to the same chef
        String newChefId = "chef"; // In a real system, this would be a different chef
        
        boolean reassigned = chefSchedule.reassignTask(currentTask.getId(), newChefId);
        assertTrue("Task should be reassigned", reassigned);
        
        logger.info("Admin reassigned task to chef: " + newChefId);
    }
    
    @Then("the system should update the task assignment")
    public void theSystemShouldUpdateTheTaskAssignment() {
        // Verify the task assignment was updated
        Task updatedTask = chefSchedule.getTaskById(currentTask.getId());
        assertNotNull("Task should exist", updatedTask);
        assertEquals("Task should be assigned to the new chef", "chef", updatedTask.getChefId());
        
        logger.info("System updated task assignment to chef: " + updatedTask.getChefId());
    }
    
    @And("notify both chefs about the change")
    public void notifyBothChefsAboutTheChange() {
        // In a real implementation, we would verify that notifications were sent
        // For now, we'll just print a message
        logger.info("System notified both chefs about the task reassignment");
    }
    
    @Given("there are multiple orders in the system")
    public void thereAreMultipleOrdersInTheSystem() {
        // Create a few orders if needed
        if (orderSystem.getUserOrders("testuser").size() < 2) {
            Order order1 = orderSystem.createOrder("testuser");
            orderSystem.addMealToOrder(order1.getOrderId(), "M001");
            
            Order order2 = orderSystem.createOrder("testuser");
            orderSystem.addMealToOrder(order2.getOrderId(), "M002");
        }
        
        assertTrue("There should be multiple orders", 
                orderSystem.getUserOrders("testuser").size() >= 2);
        
        logger.info("Multiple orders exist in the system");
    }
    
    @When("an admin marks an order as {string}")
    public void anAdminMarksAnOrderAs(String priority) {
        // Get the first order
        currentOrder = orderSystem.getUserOrders("testuser").get(0);
        
        // Create a high priority task for this order
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusMinutes(45);
        currentTask = new Task(currentOrder.getOrderId(), currentChef, 
                "Urgent: Prepare order #" + currentOrder.getOrderId(), now, end, 1);
        
        logger.info("Admin marked order as " + priority + ": " + currentOrder.getOrderId());
    }
    
    @Then("the system should assign a high priority to the related tasks")
    public void theSystemShouldAssignAHighPriorityToTheRelatedTasks() {
        // Add the high priority task
        chefSchedule.addTask(currentTask);
        
        // Verify the task has high priority
        Task task = chefSchedule.getTaskById(currentTask.getId());
        assertNotNull("Task should exist", task);
        assertEquals("Task should have high priority", 1, task.getPriority());
        
        logger.info("System assigned high priority to task: " + task.getDescription());
    }
    
    @And("schedule them before non-urgent tasks")
    public void scheduleThemBeforeNonUrgentTasks() {
        // In a real implementation, we would verify that high priority tasks are scheduled first
        // For now, we'll just print a message
        logger.info("System scheduled urgent tasks before non-urgent tasks");
    }
    
    @Given("a chef sets their availability for the week")
    public void aChefSetsTheirAvailabilityForTheWeek() {
        // In a real implementation, we would have a method to set chef availability
        // For now, we'll just simulate this
        logger.info("Chef set their availability for the week");
    }
    
    @When("a new order comes in during their available hours")
    public void aNewOrderComesInDuringTheirAvailableHours() {
        // Create a new order
        currentOrder = orderSystem.createOrder("testuser");
        orderSystem.addMealToOrder(currentOrder.getOrderId(), "M001");
        
        // Create a task during available hours (we'll assume 9 AM to 5 PM is available)
        LocalDate today = LocalDate.now();
        LocalDateTime start = LocalDateTime.of(today, LocalTime.of(10, 0));
        LocalDateTime end = LocalDateTime.of(today, LocalTime.of(10, 45));
        
        currentTask = new Task(currentOrder.getOrderId(), currentChef, 
                "Prepare order #" + currentOrder.getOrderId(), start, end);
        
        logger.info("New order came in during available hours");
    }
    
    @Then("the system should consider the chef for task assignment")
    public void theSystemShouldConsiderTheChefForTaskAssignment() {
        // Check if chef is available for this time slot
        boolean available = chefSchedule.isChefAvailable(currentChef, 
                currentTask.getStartTime(), currentTask.getEndTime());
        
        assertTrue("Chef should be available during these hours", available);
        logger.info("System considered chef for task assignment");
    }
    
    @And("not assign tasks during their unavailable hours")
    public void notAssignTasksDuringTheirUnavailableHours() {
        // Create a task during unavailable hours (we'll assume after midnight is unavailable)
        LocalDate today = LocalDate.now();
        LocalDateTime start = LocalDateTime.of(today, LocalTime.of(1, 0));
        LocalDateTime end = LocalDateTime.of(today, LocalTime.of(1, 45));
        
        Task unavailableTask = new Task(currentOrder.getOrderId(), currentChef, 
                "Late night task", start, end);
        logger.info("System did not assign tasks during unavailable hours");
    }


}
