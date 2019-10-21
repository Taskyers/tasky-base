package pl.taskyers.taskybase.registration.validator;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pl.taskyers.taskybase.core.MessageCodeProvider;
import pl.taskyers.taskybase.core.users.dto.AccountDTO;
import pl.taskyers.taskybase.core.messages.Message;
import pl.taskyers.taskybase.core.messages.ValidationMessage;
import pl.taskyers.taskybase.core.messages.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.users.slo.UserSLO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RegistrationValidatorStepdefs {
    
    private List<AccountDTO> users = new ArrayList<>();
    
    private AccountDTO entry;
    
    private ValidationMessageContainer result = new ValidationMessageContainer();
    
    private RegistrationValidator registrationValidator;
    
    private UserSLO userSLO;
    
    @Before
    public void setUp() {
        MessageCodeProvider.setMessageSource();
        userSLO = mock(UserSLO.class);
        registrationValidator = new RegistrationValidator(userSLO);
    }
    
    @Given("^I have following users in database$")
    public void iHaveFollowingUsersInDatabase(List<AccountDTO> userEntities) {
        users.addAll(userEntities);
    }
    
    @Given("^I have following user$")
    public void iHaveFollowingUser(List<AccountDTO> user) {
        entry = user.get(0);
        if ( usernameExists(entry.getUsername()) != null ) {
            when(userSLO.getEntityByUsername(anyString()).isPresent()).thenAnswer(invocationOnMock -> Optional.of(entry));
        }
        if ( emailExists(entry.getEmail()) != null ) {
            when(userSLO.getEntityByEmail(anyString()).isPresent()).thenAnswer(invocationOnMock -> Optional.of(entry));
        }
    }
    
    @When("^I validate user for registration$")
    public void iValidateUserForRegistration() {
        registrationValidator.validate(entry, result);
    }
    
    @Then("^Container contains following messages$")
    public void containerContainsFollowingMessages(List<ValidationMessage> validationMessages) {
        for ( ValidationMessage validationMessage : validationMessages ) {
            assertNotNull(checkForType(result, validationMessage));
            assertNotNull(checkForMessage(result, validationMessage));
            assertNotNull(checkForField(result, validationMessage));
        }
        assertTrue(result.hasErrors());
        assertEquals(result.getErrors().size(), validationMessages.size());
    }
    
    @Then("^Container is empty$")
    public void containerIsEmpty() {
        assertFalse(result.hasErrors());
        assertEquals(0, result.getAll().size());
    }
    
    private AccountDTO usernameExists(String username) {
        return users.stream().filter(accountDTO -> accountDTO.getUsername().equals(username)).findFirst().orElse(null);
    }
    
    private AccountDTO emailExists(String email) {
        return users.stream().filter(accountDTO -> accountDTO.getEmail().equals(email)).findFirst().orElse(null);
    }
    
    private Message checkForType(ValidationMessageContainer validationMessageContainer, ValidationMessage validationMessage) {
        return validationMessageContainer.getAll().stream().filter(message -> message.getType().equals(validationMessage.getType())).findFirst()
                .orElse(null);
    }
    
    private Message checkForMessage(ValidationMessageContainer validationMessageContainer, ValidationMessage validationMessage) {
        return validationMessageContainer.getAll().stream().filter(message -> message.getMessage().equals(validationMessage.getMessage())).findFirst()
                .orElse(null);
    }
    
    private Message checkForField(ValidationMessageContainer validationMessageContainer, ValidationMessage validationMessage) {
        return validationMessageContainer.getAll().stream().filter(message -> message instanceof ValidationMessage)
                .map(message -> ((ValidationMessage) message))
                .filter(validationMessage1 -> validationMessage1.getField().equals(validationMessage.getField())).findFirst()
                .orElse(null);
    }
    
}
