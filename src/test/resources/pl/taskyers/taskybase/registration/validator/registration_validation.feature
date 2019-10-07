Feature: Registration validation

  Background:
    Given I have following users in database
      | id | username | email        |
      | 1  | u1       | u1@email.com |
      | 2  | u2       | u2@email.com |

  Scenario: Validating user without providing any data
    Given I have following user
      | id  | username | email | password | name | surname |
      | 100 |          |       |          |      |         |

    When I validate user for registration
    Then Container contains following messages
      | type  | message                  | field    |
      | ERROR | Username cannot be empty | username |
      | ERROR | Email cannot be empty    | email    |
      | ERROR | Password cannot be empty | password |
      | ERROR | Name cannot be empty     | name     |
      | ERROR | Surname cannot be empty  | surname  |

  Scenario: Validating user with invalid data formats
    Given I have following user
      | id  | username | email           | password | name | surname |
      | 100 | test     | test@@email.com | zaq1WSX  | test | test    |

    When I validate user for registration
    Then Container contains following messages
      | type  | message                                | field    |
      | ERROR | Provided email is invalid              | email    |
      | ERROR | Provided name is invalid               | name     |
      | ERROR | Provided surname is invalid            | surname  |
      | ERROR | Provided password is not strong enough | password |

  Scenario: Validating user when provided username exists in database
    Given I have following user
      | id  | username | email          | password | name | surname |
      | 100 | u1       | test@email.com | zaq1@WSX | Test | Test    |

    When I validate user for registration
    Then Container contains following messages
      | type  | message                              | field    |
      | ERROR | User with u1 username already exists | username |

  Scenario: Validating user when provided email exists in database
    Given I have following user
      | id  | username | email        | password | name | surname |
      | 100 | test     | u2@email.com | zaq1@WSX | Test | Test    |

    When I validate user for registration
    Then Container contains following messages
      | type  | message                                     | field |
      | ERROR | User with u2@email.com email already exists | email |

  Scenario: Validating user when all data is valid
    Given I have following user
      | id  | username | email          | password | name | surname |
      | 100 | test     | test@email.com | zaq1@WSX | Test | Test    |

    When I validate user for registration
    Then Container is empty