/*
package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DepositCommandValidatorTest {
    DepositCommandValidator depositCommandValidator;
    CreateCommandValidator createCommandValidator;
    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        createCommandValidator = new CreateCommandValidator(bank);
        depositCommandValidator = new DepositCommandValidator(bank);
    }

    private void createAccount(String command) {
        boolean isValid = createCommandValidator.validate(command);
        if (!isValid) {
            throw new IllegalArgumentException("Invalid account creation command: " + command);
        }
    }

    @Test
    void valid_deposit_command() {
        createAccount("create banking.Checking 89456185 5.0");
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
        createAccount("create banking.Checking 89456185 5.0");
        boolean actual = depositCommandValidator.validate("deposit 89456185 abc");
        assertFalse(actual, "Deposit amount must be numeric");
    }

    @Test
    void negative_deposit_amount() {
        createAccount("create banking.Checking 89456185 5.0");
        boolean actual = depositCommandValidator.validate("deposit 89456185 -100.0");
        assertFalse(actual, "Deposit amount must be positive");
    }

    @Test
    void zero_deposit_amount() {
        createAccount("create banking.Checking 89456185 5.0");
        boolean actual = depositCommandValidator.validate("deposit 89456185 0.0");
        assertFalse(actual, "Deposit amount must be greater than zero");
    }
    /*
    @Test
    void excessive_whitespace_in_command() {
        createAccount("create banking.Checking 89456185 5.0");
        boolean actual = depositCommandValidator.validate("deposit    89456185     100.0");
        assertTrue(actual, "Command with extra spaces should still be valid");
    }
    */

    /*
    @Test
    void deposit_amount_with_large_precision() {
        createAccount("create banking.Checking 89456185 5.0");
        boolean actual = depositCommandValidator.validate("deposit 89456185 100.0000000000001");
        assertTrue(actual, "Deposit amount with extra precision should still be valid");
    }
     */
    /*
    @Test
    void invalid_command_name() {
        boolean actual = depositCommandValidator.validate("add 89456185 100.0");
        assertFalse(actual, "Command must start with 'deposit'");
    }

    /*
    @Test
    void deposit_amount_with_whitespace_around() {
        createAccount("create banking.Checking 89456185 5.0");
        boolean actual = depositCommandValidator.validate("deposit 89456185    100.0   ");
        assertTrue(actual, "Amount with extra whitespace should be valid");
    }
    */
    /*
    @Test
    void invalid_account_type_for_deposit() {
        createAccount("create banking.CD 89456185 5.0");
        boolean actual = depositCommandValidator.validate("deposit 89456185 100.0");
        assertFalse(actual, "Deposits are not allowed for CD accounts");
    }
}

     */

