package testing_cases.step_definition;
import app.*;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

public class NotificationSteps {

    NotificationSystem system;
    List<Notification> notifications;
    Notification notification;
    List<Ingredient> lowIngredients;
    Invoice invoice;
    String message;

    @Given("a user has multiple notifications of different types")
    public void user_has_multiple_notifications() {
        system = new NotificationSystem();
        system.sendNotification("user1", "Bill", "Invoice ready", "billing");
        system.sendNotification("user1", "Order", "Order ready", "order");
    }

    @When("they request notifications of type {string}")
    public void request_notifications_by_type(String type) {
        notifications = system.getNotificationsByType("user1", type);
    }

    @Then("only billing notifications should be returned")
    public void only_billing_notifications_returned() {
        assertEquals(1, notifications.size());
        assertEquals("billing", notifications.get(0).getType());
    }

    @Given("a user has notifications linked to specific entities")
    public void user_has_notifications_with_entities() {
        system = new NotificationSystem();
        system.sendNotification("user1", "Invoice", "Invoice info", "billing", "INV123");
        system.sendNotification("user1", "Invoice2", "Another invoice", "billing", "INV456");
    }

    @When("they filter by related entity ID {string}")
    public void filter_by_entity(String id) {
        notifications = system.getNotificationsByRelatedEntity("user1", id);
    }

    @Then("only notifications related to {string} should be returned")
    public void assert_entity_filtered(String id) {
        assertTrue(notifications.stream().allMatch(n -> id.equals(n.getRelatedEntityId())));
    }

    @Given("a user has notifications with different timestamps")
    public void notifications_with_different_timestamps() {
        system = new NotificationSystem();
    
        Notification n1 = new Notification("user1", "T1", "M1", "type");
        n1.setTimestamp(LocalDateTime.of(2025, 5, 10, 12, 0));
    
        Notification n2 = new Notification("user1", "T2", "M2", "type");
        n2.setTimestamp(LocalDateTime.of(2025, 5, 15, 12, 0));
    
        Notification n3 = new Notification("user1", "T3", "M3", "type");
        n3.setTimestamp(LocalDateTime.of(2025, 5, 16, 12, 0));  
    
        system.addNotificationDirectly(n1);
        system.addNotificationDirectly(n2);
        system.addNotificationDirectly(n3);
    }
    
    

    @When("they request notifications between {string} and {string}")
    public void request_notifications_in_range(String startStr, String endStr) {
        LocalDateTime start = LocalDateTime.parse(startStr);
        LocalDateTime end = LocalDateTime.parse(endStr);
        notifications = system.getNotificationsInDateRange("user1", start, end);
    }

    @Then("only notifications within that range should be returned")
    public void only_notifications_in_range_returned() {
        assertFalse(notifications.isEmpty());
        for (Notification n : notifications) {
            assertTrue(!n.getTimestamp().isBefore(LocalDateTime.of(2025, 5, 1, 0, 0)) &&
                       !n.getTimestamp().isAfter(LocalDateTime.of(2025, 5, 15, 23, 59)));
        }
    }

    @Given("the inventory is checked with no low-stock ingredients")
    public void inventory_checked_no_low_stock() {
        system = new NotificationSystem();
        lowIngredients = new ArrayList<>();
    }

    @When("the system evaluates inventory")
    public void system_evaluates_inventory() {
        system.notifyLowInventory(lowIngredients);
        notifications = system.getNotificationsByType("admin", "inventory");
    }

    @Then("no notifications should be sent")
    public void no_notifications_sent() {
        assertTrue(notifications.isEmpty());
    }

    @Given("the inventory has low stock of {string} with quantity {int} below minimum {int}")
    public void inventory_low_stock(String name, int qty, int min) {
        system = new NotificationSystem();
        Ingredient ingredient = new Ingredient("ING123", name, "units", qty, min);
        lowIngredients = new ArrayList<>();
        lowIngredients.add(ingredient);
    }

    @When("the system generates the alert message")
    public void system_generates_alert_message() {
        system.notifyLowInventory(lowIngredients);
        notifications = system.getNotificationsByType("admin", "inventory");
        assertFalse(notifications.isEmpty());
        message = notifications.get(0).getMessage();
    }

    @Then("the message should include the ingredient name, quantity, unit, and minimum level")
    public void message_includes_details() {
        assertTrue(message.contains("Tomato"));
        assertTrue(message.contains("2"));
        assertTrue(message.contains("units"));
        assertTrue(message.contains("5"));
    }

    @Given("an invoice with ID {string} and amount {string} exists")
    public void invoice_exists(String id, String amount) {
        system = new NotificationSystem();
        invoice = new Invoice(id, "order1", Double.parseDouble(amount));
    }

    @When("the notification is created for the invoice")
    public void notification_created_for_invoice() {
        notification = system.notifyNewInvoice("user1", invoice);
    }

    @Then("the message should include the invoice ID and amount")
    public void message_includes_invoice_details() {
        assertTrue(notification.getMessage().contains(invoice.getInvoiceId()));
        assertTrue(notification.getMessage().contains(String.format("%.2f", invoice.getTotalAmount())));
    }

    @Given("a user has a personalized meal plan with ID {string}")
    public void user_has_meal_plan(String mealPlanId) {
        system = new NotificationSystem();
    }

    @When("the system sends a meal plan recommendation")
    public void system_sends_meal_plan_recommendation() {
        notification = system.notifyMealPlanRecommendation("user1", "MP001");
    }

    @Then("the user should receive a notification with the correct title and message")
    public void user_receives_meal_plan_notification() {
        assertEquals("New Meal Plan Recommendation", notification.getTitle());
        assertTrue(notification.getMessage().contains("personalized meal plan"));
    }

    Notification testNotification;

@Given("a new notification is created")
public void create_new_notification() {
    system = new NotificationSystem();
    testNotification = new Notification("userX", "Old Title", "Old Message", "order", "ENT123");
}

@When("the title is updated to {string}")
public void update_title(String title) {
    testNotification.setTitle(title);
}

@When("the message is updated to {string}")
public void update_message(String message) {
    testNotification.setMessage(message);
}

@When("it is marked as unread")
public void mark_unread() {
    testNotification.markAsUnread();
}

@When("the related entity ID is set to {string}")
public void set_related_entity(String id) {
    testNotification.setRelatedEntityId(id);
}

@Then("the notification should reflect the updated values")
public void assert_notification_updates() {
    assertEquals("Updated Title", testNotification.getTitle());
    assertEquals("Updated Message", testNotification.getMessage());
    assertFalse(testNotification.isRead());
    assertEquals("ENT999", testNotification.getRelatedEntityId());
}

@Then("toString should return a string containing {string}")
public void check_to_string(String expectedSubstring) {
    assertTrue(testNotification.toString().contains(expectedSubstring));
}

}
