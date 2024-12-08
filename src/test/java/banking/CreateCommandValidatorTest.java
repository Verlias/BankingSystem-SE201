package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateCommandValidatorTest {
    CreateCommandValidator createCommandValidator;
    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        createCommandValidator = new CreateCommandValidator(bank);
    }

    @Test
    void valid_create_command() {
        boolean actual = createCommandValidator.validate("create banking.Checking 89456185 5.0");
        assertTrue(actual);
    }

    @Test
    void missing_parameters() {
        boolean actual = createCommandValidator.validate("create 89456185");
        assertFalse(actual, "Command must have all required parameters");
    }

    @Test
    void account_id_missing() {
        boolean actual = createCommandValidator.validate("create banking.Checking 5.0");
        assertFalse(actual, "Account ID must be provided");
    }

    @Test
    void apr_missing() {
        boolean actual = createCommandValidator.validate("create banking.Checking 89456185");
        assertFalse(actual, "APR must be provided");
    }

    @Test
    void account_type_missing() {
        boolean actual = createCommandValidator.validate("create 89456185 5.0");
        assertFalse(actual, "Account type must be provided");
    }

    @Test
    void invalid_account_type() {
        boolean actual = createCommandValidator.validate("create InvalidType 89456185 5.0");
        assertFalse(actual, "Account type must be CD, banking.Checking, or Saving");
    }

    @Test
    void invalid_account_id_not_numeric() {
        boolean actual = createCommandValidator.validate("create banking.Checking abcdefgh 5.0");
        assertFalse(actual, "Account ID must contain only digits");
    }

    @Test
    void invalid_account_id_wrong_length() {
        boolean actual = createCommandValidator.validate("create banking.Checking 1234567 5.0");
        assertFalse(actual, "Account ID must be exactly 8 digits long");

        actual = createCommandValidator.validate("create banking.Checking 123456789 5.0");
        assertFalse(actual, "Account ID must be exactly 8 digits long");
    }

    @Test
    void apr_is_not_a_decimal_number() {
        boolean actual = createCommandValidator.validate("create banking.Checking 89456185 ABC");
        assertFalse(actual, "APR must be a valid decimal number");
    }

    @Test
    void negative_apr_value() {
        boolean actual = createCommandValidator.validate("create banking.Checking 89456185 -5.0");
        assertFalse(actual, "APR must be a non-negative decimal number");
    }

    @Test
    void apr_exceeds_max_boundary() {
        boolean actual = createCommandValidator.validate("create banking.Checking 89456185 10.1");
        assertFalse(actual, "APR must not exceed 10.0");
    }

    @Test
    void apr_is_slightly_below_zero() {
        boolean actual = createCommandValidator.validate("create banking.Checking 89456185 -0.01");
        assertFalse(actual, "APR must be non-negative and not slightly below zero");
    }

    @Test
    void excessive_whitespace_in_command() {
        boolean actual = createCommandValidator.validate("create    banking.Checking      89456185     5.0");
        assertTrue(actual, "Command with extra spaces should still be valid");
    }

    @Test
    void valid_apr_at_max_boundary() {
        boolean actual = createCommandValidator.validate("create banking.Checking 89456185 10.0");
        assertTrue(actual, "APR of 10.0 should be valid as it is within the boundary");
    }

    @Test
    void apr_with_multiple_decimal_points() {
        boolean actual = createCommandValidator.validate("create banking.Checking 89456185 5.0.0");
        assertFalse(actual, "APR must be a valid decimal number, not containing multiple decimal points");
    }

    @Test
    void apr_with_extra_large_decimal_value() {
        boolean actual = createCommandValidator.validate("create banking.Checking 89456185 5.0000000000000001");
        assertTrue(actual, "APR with extra decimal precision should still be valid if it rounds to within bounds");
    }

    @Test
    void apr_with_whitespace_around() {
        boolean actual = createCommandValidator.validate("create banking.Checking 89456185    5.0   ");
        assertTrue(actual, "APR with extra whitespace around it should be valid");
    }

    @Test
    void apr_without_decimal_point() {
        boolean actual = createCommandValidator.validate("create banking.Checking 89456185 5");
        assertTrue(actual, "APR without decimal point (like '5') should be considered valid");
    }
}
