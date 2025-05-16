package testing_cases.step_definition;

import app.User;
import static org.junit.Assert.*;
import io.cucumber.java.en.*;

public class UserCoreStep {

    User user;

    @Given("a user is initialized with username {string}, password {string}, and role {string}")
    public void aUserIsInitializedWithUsernamePasswordAndRole(String username, String password, String role) {
        user = new User(username, password, role);
    }

    @When("the user updates their username to {string}")
    public void theUserUpdatesTheirUsernameTo(String newUsername) {
        user.setUsername(newUsername);
    }

    @Then("the user's username should be {string}")
    public void theUserSUsernameShouldBe(String expectedUsername) {
        assertEquals(expectedUsername, user.getUsername());
    }

    @When("the user updates their password to {string}")
    public void theUserUpdatesTheirPasswordTo(String newPassword) {
        user.setPassword(newPassword);
    }

    @Then("the user's password should be {string}")
    public void theUserSPasswordShouldBe(String expectedPassword) {
        assertEquals(expectedPassword, user.getPassword());
    }

    @When("the user role is changed to {string}")
    public void theUserRoleIsChangedTo(String newRole) {
        user.setRole(newRole);
    }

    @Then("the user should have the role {string}")
    public void theUserShouldHaveTheRole(String role) {
        assertTrue(user.hasRole(role));
    }

    @And("the user has {string} as a dietary preference")
    public void theUserHasAsADietaryPreference(String preference) {
        user.addDietaryPreference(preference);
    }

    @When("the user removes {string} from their dietary preferences")
    public void theUserRemovesFromTheirDietaryPreferences(String preference) {
        user.removeDietaryPreference(preference);
    }

    @Then("{string} should not be in the dietary preferences")
    public void shouldNotBeInTheDietaryPreferences(String preference) {
        assertFalse(user.getDietaryPreferences().contains(preference));
    }

    @And("the user has {string} listed as an allergy")
    public void theUserHasListedAsAnAllergy(String allergy) {
        user.addAllergy(allergy);
    }

    @When("the user removes {string} from their allergies")
    public void theUserRemovesFromTheirAllergies(String allergy) {
        user.removeAllergy(allergy);
    }

    @Then("{string} should not be in the allergy list")
    public void shouldNotBeInTheAllergyList(String allergy) {
        assertFalse(user.getAllergies().contains(allergy));
    }
}
