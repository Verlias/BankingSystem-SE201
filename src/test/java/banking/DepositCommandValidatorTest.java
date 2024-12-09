package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// Starting Commit
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
        commandProcessor.process("create Checking 89456185 3.5");
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
    void checking_zero_deposit_amount() {
        boolean actual = depositCommandValidator.validate("deposit 89456185 0.0");
        assertFalse(actual, "Deposit amount must be greater than zero");
    }

    @Test
    void deposit_exceeds_checking_limit() {
        boolean actual = depositCommandValidator.validate("deposit 89456185 1500.0");
        assertFalse(actual, "Deposit amount exceeds limit for Checking account (max: $1000)");
    }

    @Test
    void valid_deposit_for_savings_account() {
        commandProcessor.process("create Savings 12345679 2.5");  // Create a Savings account
        boolean actual = depositCommandValidator.validate("deposit 12345679 2000.0");
        assertTrue(actual, "Valid deposit for Savings account should pass validation");
    }

    @Test
    void deposit_exceeds_savings_limit() {
        commandProcessor.process("create Savings 12345679 2.5");  // Create a Savings account
        boolean actual = depositCommandValidator.validate("deposit 12345679 3000.0");
        assertFalse(actual, "Deposit amount exceeds limit for Savings account (max: $2500)");
    }

    @Test
    void deposit_not_allowed_for_cd_account() {
        commandProcessor.process("create cd 12345680 4.0");  // Create a CD account
        boolean actual = depositCommandValidator.validate("deposit 12345680 100.0");
        assertFalse(actual, "Deposits are not allowed for CD accounts");
    }
}
