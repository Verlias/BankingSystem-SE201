package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WithdrawCommandValidatorTest {

    CommandValidator commandValidator;
    CommandProcessor commandProcessor;
    Bank bank;

    @BeforeEach
    void set_up() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
        commandProcessor = new CommandProcessor(bank);

        // Create a checking account for testing
        commandProcessor.process("create Checking 89456185 3.5");
        commandProcessor.process("deposit 89456185 200.0"); // Deposit some money to the account
    }

    @Test
    void test_valid_withdraw() {
        boolean result = commandValidator.validate("withdraw 89456185 100.0");
        assertTrue(result, "Withdrawal command should be valid when account has sufficient funds.");
    }

    @Test
    void test_insufficient_funds() {
        boolean result = commandValidator.validate("withdraw 89456185 300.0");
        assertFalse(result, "Withdrawal command should be invalid when there are insufficient funds.");
    }

    @Test
    void test_invalid_account() {
        boolean result = commandValidator.validate("withdraw 12345678 50.0");
        assertFalse(result, "Withdrawal command should be invalid when account does not exist.");
    }

    @Test
    void test_negative_amount() {
        boolean result = commandValidator.validate("withdraw 89456185 -50.0");
        assertFalse(result, "Withdrawal command should be invalid when amount is negative.");
    }

    @Test
    void test_zero_amount() {
        boolean result = commandValidator.validate("withdraw 89456185 0.0");
        assertFalse(result, "Withdrawal command should be invalid when amount is zero.");
    }

    @Test
    void test_invalid_command_format() {
        boolean result = commandValidator.validate("withdraw 89456185");
        assertFalse(result, "Withdrawal command should be invalid when the command format is incorrect.");
    }

    @Test
    void test_account_with_no_funds() {
        commandProcessor.process("withdraw 89456185 200.0"); // Withdraw all funds
        boolean result = commandValidator.validate("withdraw 89456185 100.0");
        assertFalse(result, "Withdrawal command should be invalid when the account has no funds.");
    }

    @Test
    void test_account_balance_exactly_equal_to_amount() {
        boolean result = commandValidator.validate("withdraw 89456185 200.0");
        assertTrue(result, "Withdrawal should be valid when the account balance is exactly equal to the withdrawal amount.");
    }

    @Test
    void test_max_amount_withdrawable() {
        commandProcessor.process("deposit 89456185 1000.0"); // Deposit large amount
        boolean result = commandValidator.validate("withdraw 89456185 400.0");
        assertTrue(result, "Withdrawal should be valid when the withdrawal amount is within the allowed limit.");
    }

    @Test
    void test_exceeding_maximum_withdrawal_limit() {
        boolean result = commandValidator.validate("withdraw 89456185 10000.0");
        assertFalse(result, "Withdrawal should be invalid when the amount exceeds the maximum allowed limit.");
    }

    @Test
    void test_withdraw_with_spaces_in_command() {
        boolean result = commandValidator.validate("withdraw   89456185   100.0");
        assertTrue(result, "Withdrawal command with extra spaces should still be valid.");
    }

    @Test
    void test_withdraw_from_savings_account() {
        commandProcessor.process("create Savings 12345678 1.0");
        commandProcessor.process("deposit 12345678 300.0");
        boolean result = commandValidator.validate("withdraw 12345678 150.0");
        assertTrue(result, "Withdrawal from a savings account should be valid.");
    }

    @Test
    void test_withdraw_to_zero_balance() {
        commandProcessor.process("withdraw 89456185 200.0"); // Withdraw all funds
        boolean result = commandValidator.validate("withdraw 89456185 0.0");
        assertFalse(result, "Withdrawal command should be invalid when the account balance is already zero.");
    }

    @Test
    void test_withdraw_negative_balance_after_withdrawal() {
        commandProcessor.process("withdraw 89456185 250.0"); // Attempt to withdraw more than balance
        boolean result = commandValidator.validate("withdraw 89456185 50.0");
        assertFalse(result, "Withdrawal should not be allowed if the balance goes negative.");
    }

    @Test
    void test_non_existent_account_id() {
        boolean result = commandValidator.validate("withdraw nonExistentID 100.0");
        assertFalse(result, "Withdrawal command should fail for a non-existent account.");
    }

    @Test
    void test_command_with_non_numeric_amount() {
        boolean result = commandValidator.validate("withdraw 89456185 abc");
        assertFalse(result, "Withdrawal command should be invalid if the amount is not a number.");
    }

    @Test
    void test_withdraw_after_multiple_deposits() {
        commandProcessor.process("deposit 89456185 1000.0");
        boolean result = commandValidator.validate("withdraw 89456185 400.0");
        assertTrue(result, "Withdrawal should be valid after multiple deposits.");
    }

    @Test
    void test_max_withdrawal_limit_per_transaction() {
        double limit = 400;
        commandProcessor.process("deposit 89456185 1000.0");
        boolean result = commandValidator.validate("withdraw 89456185 " + limit);
        assertTrue(result, "Withdrawal command should be valid when the withdrawal is within the transaction limit.");
    }

    @Test
    void test_withdraw_with_no_deposits_made() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        boolean result = commandValidator.validate("withdraw 12345678 100.0");
        assertFalse(result, "Withdrawal should not be allowed if no deposits have been made.");
    }

    @Test
    void test_withdraw_from_cd_account() {
        commandProcessor.process("create banking.CD 11223344 1000.0");
        boolean result = commandValidator.validate("withdraw 11223344 100.0");
        assertFalse(result, "Withdrawal should be invalid for a CD account.");
    }

    @Test
    void test_withdraw_with_account_under_maintenance() {
        commandProcessor.process("create banking.Checking 56789012 1000.0");
        commandProcessor.process("withdraw 56789012 100.0");
        boolean result = commandValidator.validate("withdraw 56789012 50.0");
        assertFalse(result, "Withdrawal should be invalid if the account is under maintenance.");
    }

    @Test
    void test_minimal_valid_withdrawal() {
        boolean result = commandValidator.validate("withdraw 89456185 0.01");
        assertTrue(result, "Minimal valid withdrawal amount should be accepted.");
    }

    @Test
    void test_large_account_number() {
        boolean result = commandValidator.validate("withdraw 9999999999 100.0");
        assertFalse(result, "Large account numbers should be invalid if not supported.");
    }

    @Test
    void test_null_command_string() {
        boolean result = commandValidator.validate(null);
        assertFalse(result, "Withdrawal command should be invalid if the input is null.");
    }

    @Test
    void test_empty_command_string() {
        boolean result = commandValidator.validate("");
        assertFalse(result, "Withdrawal command should be invalid if the input is empty.");
    }

    @Test
    void test_special_characters_in_amount() {
        boolean result = commandValidator.validate("withdraw 89456185 @#$%^");
        assertFalse(result, "Withdrawal command should be invalid if the amount contains special characters.");
    }

    @Test
    void test_special_characters_in_account_number() {
        boolean result = commandValidator.validate("withdraw abc@# 100.0");
        assertFalse(result, "Withdrawal command should be invalid if the account number contains special characters.");
    }

    @Test
    void test_withdrawal_command_with_typo() {
        boolean result = commandValidator.validate("withraw 89456185 100.0");
        assertFalse(result, "Withdrawal command should be invalid if there is a typo in the keyword.");
    }

    @Test
    void test_withdraw_from_closed_account() {
        commandProcessor.process("create banking.cd 12345679 1 0"); // Assuming there is a close command
        commandProcessor.process("passtime 1");
        boolean result = commandValidator.validate("withdraw 12345679 100.0");
        assertFalse(result, "Withdrawal should be invalid if the account is closed.");
    }

    @Test
    void test_premature_cd_withdrawal_penalty() {
        commandProcessor.process("create banking.CD 11223344 1000.0");
        boolean result = commandValidator.validate("withdraw 11223344 100.0");
        assertFalse(result, "Premature withdrawal from a CD should be invalid.");
    }

    @Test
    void test_checking_account_maximum_limit_exceeded() {
        commandProcessor.process("create banking.Checking 12345678 1000.0");
        boolean result = commandValidator.validate("withdraw 12345678 5000.0");
        assertFalse(result, "Withdrawal should not exceed the checking account's maximum withdrawal limit.");
    }
}
