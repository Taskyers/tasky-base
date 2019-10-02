Feature: Getting messages from message source and MessageCode enum

  Scenario: Getting message without arguments
    Given Message code "test"
    And There are no arguments
    When I get message from message source
    Then Message is "test"

  Scenario: Getting message with one argument
    Given Message code "test_1"
    And "First" argument is "firstArgument"
    When I get message from message source
    Then Message is "test firstArgument"

  Scenario: Getting message with two arguments
    Given Message code "test_1_2"
    And "First" argument is "firstArgument"
    And "Second" argument is "secondArgument"
    When I get message from message source
    Then Message is "test firstArgument secondArgument"

  Scenario: Getting message with three arguments
    Given Message code "test_1_2_3"
    And "First" argument is "firstArgument"
    And "Second" argument is "secondArgument"
    And "Third" argument is "thirdArgument"
    When I get message from message source
    Then Message is "test firstArgument secondArgument thirdArgument"

  Scenario: Getting message when message not exists in message.properties but exists in enum
    Given Message code "test_1_2_3_4"
    And "First" argument is "firstArgument"
    And "Second" argument is "secondArgument"
    And "Third" argument is "thirdArgument"
    And "Fourth" argument is "fourthArgument"
    When I get message from message source
    Then Message is null