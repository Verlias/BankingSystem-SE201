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
    void setUp() {
        bank = new Bank();
        commandValidator = new CommandValidator(bank);
        commandProcessor = new CommandProcessor(bank);

        // Create a checking account for testing
        commandProcessor.process("create banking.Checking 89456185 3.5");
        commandProcessor.process("deposit 89456185 200.0"); // Deposit some money to the account
    }

    @Test
    void testValidWithdraw() {
        boolean result = commandValidator.validate("withdraw 89456185 100.0");
        assertTrue(result, "Withdrawal command should be valid when account has sufficient funds.");
    }

    @Test
    void testInsufficientFunds() {
        boolean result = commandValidator.validate("withdraw 89456185 300.0");
        assertFalse(result, "Withdrawal command should be invalid when there are insufficient funds.");
    }

    @Test
    void testInvalidAccount() {
        boolean result = commandValidator.validate("withdraw 12345678 50.0");
        assertFalse(result, "Withdrawal command should be invalid when account does not exist.");
    }

    @Test
    void testNegativeAmount() {
        boolean result = commandValidator.validate("withdraw 89456185 -50.0");
        assertFalse(result, "Withdrawal command should be invalid when amount is negative.");
    }

    @Test
    void testZeroAmount() {
        boolean result = commandValidator.validate("withdraw 89456185 0.0");
        assertFalse(result, "Withdrawal command should be invalid when amount is zero.");
    }

    @Test
    void testInvalidCommandFormat() {
        boolean result = commandValidator.validate("withdraw 89456185");
        assertFalse(result, "Withdrawal command should be invalid when the command format is incorrect.");
    }

    @Test
    void testAccountWithNoFunds() {
        commandProcessor.process("withdraw 89456185 200.0"); // Withdraw all funds
        boolean result = commandValidator.validate("withdraw 89456185 100.0");
        assertFalse(result, "Withdrawal command should be invalid when the account has no funds.");
    }

    @Test
    void testAccountBalanceExactlyEqualToAmount() {
        boolean result = commandValidator.validate("withdraw 89456185 200.0");
        assertTrue(result, "Withdrawal should be valid when the account balance is exactly equal to the withdrawal amount.");
    }

    @Test
    void testMaxAmountWithdrawable() {
        commandProcessor.process("deposit 89456185 1000.0"); // Deposit large amount
        boolean result = commandValidator.validate("withdraw 89456185 400.0");
        assertTrue(result, "Withdrawal should be valid when the withdrawal amount is within the allowed limit.");
    }

    @Test
    void testExceedingMaximumWithdrawalLimit() {
        boolean result = commandValidator.validate("withdraw 89456185 10000.0");
        assertFalse(result, "Withdrawal should be invalid when the amount exceeds the maximum allowed limit.");
    }

    @Test
    void testWithdrawWithSpacesInCommand() {
        boolean result = commandValidator.validate("withdraw   89456185   100.0");
        assertTrue(result, "Withdrawal command with extra spaces should still be valid.");
    }

    @Test
    void testWithdrawFromSavingsAccount() {
        commandProcessor.process("create banking.Savings 12345678 1.0");
        commandProcessor.process("deposit 12345678 300.0");
        boolean result = commandValidator.validate("withdraw 12345678 150.0");
        assertTrue(result, "Withdrawal from a savings account should be valid.");
    }

    @Test
    void testWithdrawToZeroBalance() {
        commandProcessor.process("withdraw 89456185 200.0"); // Withdraw all funds
        boolean result = commandValidator.validate("withdraw 89456185 0.0");
        assertFalse(result, "Withdrawal command should be invalid when the account balance is already zero.");
    }

    @Test
    void testWithdrawNegativeBalanceAfterWithdrawal() {
        commandProcessor.process("withdraw 89456185 250.0"); // Attempt to withdraw more than balance
        boolean result = commandValidator.validate("withdraw 89456185 50.0");
        assertFalse(result, "Withdrawal should not be allowed if the balance goes negative.");
    }

    @Test
    void testNonExistentAccountID() {
        boolean result = commandValidator.validate("withdraw nonExistentID 100.0");
        assertFalse(result, "Withdrawal command should fail for a non-existent account.");
    }

    @Test
    void testCommandWithNonNumericAmount() {
        boolean result = commandValidator.validate("withdraw 89456185 abc");
        assertFalse(result, "Withdrawal command should be invalid if the amount is not a number.");
    }

    @Test
    void testWithdrawAfterMultipleDeposits() {
        commandProcessor.process("deposit 89456185 1000.0");
        boolean result = commandValidator.validate("withdraw 89456185 400.0");
        assertTrue(result, "Withdrawal should be valid after multiple deposits.");
    }

    @Test
    void testMaxWithdrawalLimitPerTransaction() {
        double limit = 400;
        commandProcessor.process("deposit 89456185 1000.0");
        boolean result = commandValidator.validate("withdraw 89456185 " + limit);
        assertTrue(result, "Withdrawal command should be valid when the withdrawal is within the transaction limit.");
    }

    @Test
    void testWithdrawWithNoDepositsMade() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        boolean result = commandValidator.validate("withdraw 12345678 100.0");
        assertFalse(result, "Withdrawal should not be allowed if no deposits have been made.");
    }

    @Test
    void testWithdrawFromCDAccount() {
        commandProcessor.process("create banking.CD 11223344 1000.0");
        boolean result = commandValidator.validate("withdraw 11223344 100.0");
        assertFalse(result, "Withdrawal should be invalid for a CD account.");
    }

    @Test
    void testWithdrawWithAccountUnderMaintenance() {
        commandProcessor.process("create banking.Checking 56789012 1000.0");
        commandProcessor.process("withdraw 56789012 100.0");
        boolean result = commandValidator.validate("withdraw 56789012 50.0");
        assertFalse(result, "Withdrawal should be invalid if the account is under maintenance.");
    }

    @Test
    void testMinimalValidWithdrawal() {
        boolean result = commandValidator.validate("withdraw 89456185 0.01");
        assertTrue(result, "Minimal valid withdrawal amount should be accepted.");
    }

    @Test
    void testLargeAccountNumber() {
        boolean result = commandValidator.validate("withdraw 9999999999 100.0");
        assertFalse(result, "Large account numbers should be invalid if not supported.");
    }

    @Test
    void testNullCommandString() {
        boolean result = commandValidator.validate(null);
        assertFalse(result, "Withdrawal command should be invalid if the input is null.");
    }

    @Test
    void testEmptyCommandString() {
        boolean result = commandValidator.validate("");
        assertFalse(result, "Withdrawal command should be invalid if the input is empty.");
    }

    @Test
    void testSpecialCharactersInAmount() {
        boolean result = commandValidator.validate("withdraw 89456185 @#$%^");
        assertFalse(result, "Withdrawal command should be invalid if the amount contains special characters.");
    }

    @Test
    void testSpecialCharactersInAccountNumber() {
        boolean result = commandValidator.validate("withdraw abc@# 100.0");
        assertFalse(result, "Withdrawal command should be invalid if the account number contains special characters.");
    }

    @Test
    void testWithdrawalCommandWithTypo() {
        boolean result = commandValidator.validate("withraw 89456185 100.0");
        assertFalse(result, "Withdrawal command should be invalid if there is a typo in the keyword.");
    }

    @Test
    void testWithdrawFromClosedAccount() {
        commandProcessor.process("create banking.cd 12345679 1 0"); // Assuming there is a close command
        commandProcessor.process("passtime 1");
        boolean result = commandValidator.validate("withdraw 12345679 100.0");
        assertFalse(result, "Withdrawal should be invalid if the account is closed.");
    }


    @Test
    void testPrematureCDWithdrawalPenalty() {
        commandProcessor.process("create banking.CD 11223344 1000.0");
        boolean result = commandValidator.validate("withdraw 11223344 100.0");
        assertFalse(result, "Premature withdrawal from a CD should be invalid.");
    }

    @Test
    void testCheckingAccountMaximumLimitViolation() {
        commandProcessor.process("deposit 89456185 5000.0");
        boolean result = commandValidator.validate("withdraw 89456185 500.0");
        assertFalse(result, "Withdrawal should be invalid if it exceeds the maximum limit for a checking account.");
    }

    @Test
    void testNegativeAccountNumber() {
        boolean result = commandValidator.validate("withdraw -89456185 100.0");
        assertFalse(result, "Negative account numbers should be invalid.");
    }


}
