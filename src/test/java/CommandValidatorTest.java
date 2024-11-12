import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandValidatorTest {
    CommandValidator commandValidator;
    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
    }

    @Test
    void valid_create_command() {
        boolean actual = commandValidator.validate("create Checking 89456185 5.0");
        assertTrue(actual);
    }

    @Test
    void invalid_command_name() {
        boolean actual = commandValidator.validate("add Checking 89456185 5.0");
        assertFalse(actual, "Command name must be 'create'");
    }

    @Test
    void missing_parameters() {
        boolean actual = commandValidator.validate("create 89456185");
        assertFalse(actual, "Command must have all required parameters");
    }

    @Test
    void account_id_missing() {
        boolean actual = commandValidator.validate("create Checking 5.0");
        assertFalse(actual, "Account ID must be provided");
    }

    @Test
    void apr_missing() {
        boolean actual = commandValidator.validate("create Checking 89456185");
        assertFalse(actual, "APR must be provided");
    }

    @Test
    void account_type_missing() {
        boolean actual = commandValidator.validate("create 89456185 5.0");
        assertFalse(actual, "Account type must be provided");
    }

    @Test
    void invalid_account_type() {
        boolean actual = commandValidator.validate("create InvalidType 89456185 5.0");
        assertFalse(actual, "Account type must be CD, Checking, or Saving");
    }

    @Test
    void invalid_account_id_not_numeric() {
        boolean actual = commandValidator.validate("create Checking abcdefgh 5.0");
        assertFalse(actual, "Account ID must contain only digits");
    }

    @Test
    void invalid_account_id_wrong_length() {
        boolean actual = commandValidator.validate("create Checking 1234567 5.0"); // 7 digits instead of 8
        assertFalse(actual, "Account ID must be exactly 8 digits long");

        actual = commandValidator.validate("create Checking 123456789 5.0"); // 9 digits instead of 8
        assertFalse(actual, "Account ID must be exactly 8 digits long");
    }


    @Test
    void apr_is_not_a_decimal_number() {
        boolean actual = commandValidator.validate("create Checking 89456185 ABC");
        assertFalse(actual, "APR must be a valid decimal number");
    }

    @Test
    void apr_is_zero() {
        boolean actual = commandValidator.validate("create Checking 89456185 0.0");
        assertTrue(actual, "APR of 0.0 should be allowed if it is a valid format");
    }

    @Test
    void excessive_whitespace_in_command() {
        boolean actual = commandValidator.validate("create    Checking      89456185     5.0");
        assertTrue(actual, "Command with extra spaces should still be valid");
    }

    @Test
    void valid_apr_at_max_boundary() {
        boolean actual = commandValidator.validate("create Checking 89456185 10.0");
        assertTrue(actual, "APR of 10.0 should be valid as it is within the boundary");
    }

    @Test
    void invalid_command_empty_string() {
        boolean actual = commandValidator.validate("");
        assertFalse(actual, "Empty command should be invalid");
    }

    @Test
    void invalid_command_random_text() {
        boolean actual = commandValidator.validate("random text here");
        assertFalse(actual, "Command with random text should be invalid");
    }

    @Test
    void negative_apr_value() {
        boolean actual = commandValidator.validate("create Checking 89456185 -5.0");
        assertFalse(actual, "APR must be a non-negative decimal number");
    }
}
