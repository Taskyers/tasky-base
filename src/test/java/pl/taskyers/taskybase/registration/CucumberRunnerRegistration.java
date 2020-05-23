package pl.taskyers.taskybase.registration;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty",
        "junit:target/cucumber/cucumber_registration.xml",
        "json:target/cucumber/cucumber_registration.json" },
        features = "src/test/resources/pl/taskyers/taskybase/registration/")
public class CucumberRunnerRegistration {
}
