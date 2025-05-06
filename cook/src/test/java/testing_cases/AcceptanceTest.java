package testing_cases;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/user_stories", 
    plugin = { "html:target/cucumber-reports/report.html" },
    glue = "testing_cases.step_definition", 
    monochrome = true,
    snippets = SnippetType.CAMELCASE
)

public class AcceptanceTest {
}
