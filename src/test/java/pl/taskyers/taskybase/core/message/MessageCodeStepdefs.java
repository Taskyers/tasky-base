package pl.taskyers.taskybase.core.message;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.context.annotation.PropertySource;
import pl.taskyers.taskybase.core.MessageCodeProvider;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@PropertySource("classpath:messages.properties")
public class MessageCodeStepdefs {
    
    private MessageCode entry;
    
    private List<Object> arguments = new ArrayList<>();
    
    private String result;
    
    @Before
    public void setUp() {
        MessageCodeProvider.setMessageSource();
    }
    
    @Given("^Message code \"([^\"]*)\"$")
    public void messageCode(String code) {
        entry = MessageCode.valueOf(code);
    }
    
    @And("^There are no arguments$")
    public void thereAreNoArguments() {
        arguments = null;
    }
    
    @And("^\"([^\"]*)\" argument is \"([^\"]*)\"$")
    public void firstArgumentIs(String number, String firstArg) {
        arguments.add(firstArg);
    }
    
    @When("^I get message from message source$")
    public void iGetMessageFromMessageSource() throws Exception {
        if ( convertArguments(arguments) != null ) {
            result = entry.getMessage(convertArguments(arguments));
        } else {
            result = entry.getMessage();
        }
    }
    
    @Then("^Message is \"([^\"]*)\"$")
    public void messageIs(String expected) {
        assertEquals(expected, result);
    }
    
    @Then("^Message is null$")
    public void messageIsNull() {
        assertNull(result);
    }
    
    private Object[] convertArguments(List<Object> arguments) {
        if ( arguments != null ) {
            Object[] argumentsArray = new Object[arguments.size()];
            return arguments.toArray(argumentsArray);
        }
        return null;
    }
    
}
