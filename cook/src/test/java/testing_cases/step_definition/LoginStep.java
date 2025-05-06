package testing_cases.step_definition;

import app.LoginSystem;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;

public class LoginStep {

    private LoginSystem loginSystem;
    private String actualMessage;

    public LoginStep() {
        loginSystem = new LoginSystem();
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        loginSystem.navigateToLoginPage();
    }

    @When("the user enters the username {string} and the password {string}")
    public void theUserEntersTheUsernameAndThePassword(String username, String password) {
        actualMessage = loginSystem.login(username, password);
    }

    @When("clicks the {string} button")
    public void clicksTheButton(String button) {
        // نحاكي النقر على زر تسجيل الدخول
        System.out.println("User clicked the " + button + " button.");
    }

    @Then("the user should be redirected to the homepage successfully")
    public void theUserShouldBeRedirectedToTheHomepageSuccessfully() {
        assertTrue("User should be redirected to homepage", actualMessage.startsWith("Welcome"));
    }

    @Then("a welcome message {string} should be displayed")
    public void aWelcomeMessageShouldBeDisplayed(String expectedMessage) {
        assertEquals("Welcome message should match", expectedMessage, actualMessage);
    }

    @Then("an error message {string} should be displayed")
    public void anErrorMessageShouldBeDisplayed(String expectedMessage) {
        assertEquals("Error message should match", expectedMessage, actualMessage);
    }

    @Then("the user should remain on the login page")
    public void theUserShouldRemainOnTheLoginPage() {
        assertFalse("User should remain on login page", actualMessage.startsWith("Welcome"));
    }
}
