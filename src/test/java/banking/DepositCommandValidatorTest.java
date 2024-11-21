package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DepositCommandValidatorTest {
    DepositCommandValidator depositCommandValidator;
    CreateCommandValidator createCommandValidator;

    CommandProcessor commandProcessor;
    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        createCommandValidator = new CreateCommandValidator(bank);
        depositCommandValidator = new DepositCommandValidator(bank);
        commandProcessor = new CommandProcessor(bank);
        commandProcessor.process("create banking.Checking 89456185 3.5");


    }
    @Test
    void valid_deposit_command() {
        boolean actual = depositCommandValidator.validate("deposit 89456185 100.0");
        assertTrue(actual, "Valid deposit command should pass validation");
    }

    @Test
    void missing_parameters() {
        boolean actual = depositCommandValidator.validate("deposit 89456185");
        assertFalse(actual, "Deposit command must include all required parameters");
    }

    @Test
    void invalid_account_id() {
        boolean actual = depositCommandValidator.validate("deposit abcdefgh 100.0");
        assertFalse(actual, "Account ID must be numeric");

        actual = depositCommandValidator.validate("deposit 12345 100.0");
        assertFalse(actual, "Account ID must be 8 digits long");
    }

    @Test
    void account_does_not_exist() {
        boolean actual = depositCommandValidator.validate("deposit 12345678 100.0");
        assertFalse(actual, "Account must exist for the deposit command to be valid");
    }

    @Test
    void invalid_deposit_amount_not_numeric() {
        boolean actual = depositCommandValidator.validate("deposit 89456185 abc");
        assertFalse(actual, "Deposit amount must be numeric");
    }

    @Test
    void negative_deposit_amount() {
        boolean actual = depositCommandValidator.validate("deposit 89456185 -100.0");
        assertFalse(actual, "Deposit amount must be positive");
    }

    @Test
    void zero_deposit_amount() {
        boolean actual = depositCommandValidator.validate("deposit 89456185 0.0");
        assertFalse(actual, "Deposit amount must be greater than zero");
    }
}
