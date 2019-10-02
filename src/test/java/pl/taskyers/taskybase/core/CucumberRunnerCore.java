package pl.taskyers.taskybase.core;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "junit:target/cucumber/cucumber_core.xml",
        "json:target/cucumber/cucumber_core.json" }, features = "src/test/resources/pl/taskyers/taskybase/core/")
public class CucumberRunnerCore {
}