package pl.taskyers.taskybase.registration.validator;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pl.taskyers.taskybase.core.MessageCodeProvider;
import pl.taskyers.taskybase.core.entity.UserEntity;
import pl.taskyers.taskybase.core.message.Message;
import pl.taskyers.taskybase.core.message.ValidationMessage;
import pl.taskyers.taskybase.core.message.container.ValidationMessageContainer;
import pl.taskyers.taskybase.core.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RegistrationValidatorStepdefs {
    
    private List<UserEntity> users = new ArrayList<>();
    
    private UserEntity entry;
    
    private ValidationMessageContainer result = new ValidationMessageContainer();
    
    private RegistrationValidator registrationValidator;
    
    private UserRepository userRepository;
    
    @Before
    public void setUp() {
        MessageCodeProvider.setMessageSource();
        userRepository = mock(UserRepository.class);
        registrationValidator = new RegistrationValidator(userRepository);
    }
    
    @Given("^I have following users in database$")
    public void iHaveFollowingUsersInDatabase(List<UserEntity> userEntities) {
        users.addAll(userEntities);
    }
    
    @Given("^I have following user$")
    public void iHaveFollowingUser(List<UserEntity> user) {
        entry = user.get(0);
        if ( usernameExists(entry.getUsername()) != null ) {
            when(userRepository.findByUsername(anyString()).isPresent()).thenAnswer(invocationOnMock -> Optional.of(entry));
        }
        if ( emailExists(entry.getEmail()) != null ) {
            when(userRepository.findByEmail(anyString()).isPresent()).thenAnswer(invocationOnMock -> Optional.of(entry));
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
    
    private UserEntity usernameExists(String username) {
        return users.stream().filter(userEntity -> userEntity.getUsername().equals(username)).findFirst().orElse(null);
    }
    
    private UserEntity emailExists(String email) {
        return users.stream().filter(userEntity -> userEntity.getEmail().equals(email)).findFirst().orElse(null);
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