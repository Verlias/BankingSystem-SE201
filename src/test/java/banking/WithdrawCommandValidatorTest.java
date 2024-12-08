package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WithdrawCommandValidatorTest {
    WithdrawCommandValidator withdrawCommandValidator;
    CommandProcessor commandProcessor;
    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        withdrawCommandValidator = new WithdrawCommandValidator(bank);
        commandProcessor = new CommandProcessor(bank);

        // Create a checking account for testing
        commandProcessor.process("create banking.Checking 89456185 3.5");
        commandProcessor.process("deposit 89456185 200.0");  // Deposit some money to the account
    }

    @Test
    void testValidWithdraw() {
        boolean result = withdrawCommandValidator.validate("withdraw 89456185 100.0");
        assertTrue(result, "Withdrawal command should be valid when account has sufficient funds.");
    }

    @Test
    void testInsufficientFunds() {
        boolean result = withdrawCommandValidator.validate("withdraw 89456185 300.0");
        assertFalse(result, "Withdrawal command should be invalid when there are insufficient funds.");
    }

    @Test
    void testInvalidAccount() {
        boolean result = withdrawCommandValidator.validate("withdraw 12345678 50.0");
        assertFalse(result, "Withdrawal command should be invalid when account does not exist.");
    }

    @Test
    void testNegativeAmount() {
        boolean result = withdrawCommandValidator.validate("withdraw 89456185 -50.0");
        assertFalse(result, "Withdrawal command should be invalid when amount is negative.");
    }

    @Test
    void testZeroAmount() {
        boolean result = withdrawCommandValidator.validate("withdraw 89456185 0.0");
        assertFalse(result, "Withdrawal command should be invalid when amount is zero.");
    }

    @Test
    void testInvalidCommandFormat() {
        boolean result = withdrawCommandValidator.validate("withdraw 89456185");
        assertFalse(result, "Withdrawal command should be invalid when the command format is incorrect.");
    }

    @Test
    void testAccountWithNoFunds() {
        commandProcessor.process("withdraw 89456185 200.0");  // Withdraw all funds
        boolean result = withdrawCommandValidator.validate("withdraw 89456185 100.0");
        assertFalse(result, "Withdrawal command should be invalid when the account has no funds.");
    }

    @Test
    void testAccountBalanceExactlyEqualToAmount() {
        boolean result = withdrawCommandValidator.validate("withdraw 89456185 200.0");
        assertTrue(result, "Withdrawal should be valid when the account balance is exactly equal to the withdrawal amount.");
    }

    @Test
    void testMaxAmountWithdrawable() {
        // Test if maximum allowed withdrawal amount (e.g., 1000) can be withdrawn
        commandProcessor.process("deposit 89456185 1000.0");  // Deposit large amount
        boolean result = withdrawCommandValidator.validate("withdraw 89456185 400.0");
        assertTrue(result, "Withdrawal should be valid when the withdrawal amount is within the allowed limit.");
    }

    @Test
    void testExceedingMaximumWithdrawalLimit() {
        // Test if exceeding the maximum withdrawal limit is invalid
        boolean result = withdrawCommandValidator.validate("withdraw 89456185 10000.0");
        assertFalse(result, "Withdrawal should be invalid when the amount exceeds the maximum allowed limit.");
    }

    @Test
    void testWithdrawWithSpacesInCommand() {
        // Test if the command with extra spaces is handled correctly
        boolean result = withdrawCommandValidator.validate("withdraw   89456185   100.0");
        assertTrue(result, "Withdrawal command with extra spaces should still be valid.");
    }

    @Test
    void testWithdrawFromSavingsAccount() {
        // Test: Withdraw from a savings account (assuming account type is supported)
        commandProcessor.process("create banking.Savings 12345678 1.0");
        commandProcessor.process("deposit 12345678 300.0");
        boolean result = withdrawCommandValidator.validate("withdraw 12345678 150.0");
        assertTrue(result, "Withdrawal from a savings account should be valid.");
    }

    @Test
    void testWithdrawToZeroBalance() {
        // Test: Withdraw all money from the account
        commandProcessor.process("withdraw 89456185 200.0");  // Withdraw all funds
        boolean result = withdrawCommandValidator.validate("withdraw 89456185 0.0");
        assertFalse(result, "Withdrawal command should be invalid when the account balance is already zero.");
    }

    @Test
    void testWithdrawNegativeBalanceAfterWithdrawal() {
        // Test: Check if the system allows a negative balance after withdrawal
        commandProcessor.process("withdraw 89456185 250.0");  // Attempt to withdraw more than balance
        boolean result = withdrawCommandValidator.validate("withdraw 89456185 50.0");
        assertFalse(result, "Withdrawal should not be allowed if the balance goes negative.");
    }

    @Test
    void testNonExistentAccountID() {
        boolean result = withdrawCommandValidator.validate("withdraw nonExistentID 100.0");
        assertFalse(result, "Withdrawal command should fail for a non-existent account.");
    }

    @Test
    void testCommandWithNonNumericAmount() {
        // Test: Command with a non-numeric amount
        boolean result = withdrawCommandValidator.validate("withdraw 89456185 abc");
        assertFalse(result, "Withdrawal command should be invalid if the amount is not a number.");
    }

    @Test
    void testWithdrawAfterMultipleDeposits() {
        // Test: Perform multiple deposits and then withdraw
        commandProcessor.process("deposit 89456185 1000.0");
        boolean result = withdrawCommandValidator.validate("withdraw 89456185 400.0");
        assertTrue(result, "Withdrawal should be valid after multiple deposits.");
    }

    @Test
    void testMaxWithdrawalLimitPerTransaction() {
        // Test: Withdraw the maximum allowable amount per transaction, assuming a set limit
        double limit = 400;  // Assuming a max withdrawal limit per transaction
        commandProcessor.process("deposit 89456185 1000.0");
        boolean result = withdrawCommandValidator.validate("withdraw 89456185 " + limit);
        assertTrue(result, "Withdrawal command should be valid when the withdrawal is within the transaction limit.");
    }

    @Test
    void testWithdrawWithNoDepositsMade() {
        // Test: Withdraw from an account without any prior deposits

        commandProcessor.process("create banking.Checking 12345678 3.5");
        boolean result = withdrawCommandValidator.validate("withdraw 12345678 100.0");
        assertFalse(result, "Withdrawal should not be allowed if no deposits have been made.");
    }

    @Test
    void testWithdrawFromCDAccount() {
        // Test: Attempt to withdraw from a CD account (if supported)
        commandProcessor.process("create banking.CD 11223344 1000.0");
        boolean result = withdrawCommandValidator.validate("withdraw 11223344 100.0");
        assertFalse(result, "Withdrawal should be invalid for a CD account.");
    }

    @Test
    void testWithdrawWithAccountUnderMaintenance() {
        // Test: Attempt to withdraw from an account that is under maintenance
        commandProcessor.process("create banking.Checking 56789012 1000.0");
        commandProcessor.process("withdraw 56789012 100.0");
        // Assuming we mark account as under maintenance in some way
        boolean result = withdrawCommandValidator.validate("withdraw 56789012 50.0");
        assertFalse(result, "Withdrawal should be invalid if the account is under maintenance.");
    }
}
