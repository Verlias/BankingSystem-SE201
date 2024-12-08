package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//TT
public class CommandProcessorTest {

    private Bank bank;
    private CommandProcessor commandProcessor;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        commandProcessor = new CommandProcessor(bank);
    }

    //Account Creation Testing
    @Test
    void create_checking_account_type() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        String actual = bank.getAccount().get("12345678").getClass().getSimpleName();
        assertEquals("Checking", actual, "Account should be of type banking.Checking");
    }

    @Test
    void create_checking_account_apr() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        double actual = bank.getAccount().get("12345678").getApr();
        assertEquals(3.5, actual, 0.01, "APR should be 3.5");
    }

    @Test
    void create_checking_account_id() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        String actual = bank.getAccount().get("12345678").getId();
        assertEquals("12345678", actual, "Account ID should be 12345678");
    }

    @Test
    void create_checking_account_initial_balance() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(0.0, actual, 0.01, "Initial balance should be 0.0");
    }

    @Test
    void create_savings_account_type() {
        commandProcessor.process("create banking.Savings 87654321 2.5");
        String actual = bank.getAccount().get("87654321").getClass().getSimpleName();
        assertEquals("Savings", actual, "Account should be of type banking.Savings");
    }

    @Test
    void create_savings_account_apr() {
        commandProcessor.process("create banking.Savings 87654321 2.5");
        double actual = bank.getAccount().get("87654321").getApr();
        assertEquals(2.5, actual, 0.01, "APR should be 2.5");
    }

    @Test
    void create_savings_account_id() {
        commandProcessor.process("create banking.Savings 87654321 2.5");
        String actual = bank.getAccount().get("87654321").getId();
        assertEquals("87654321", actual, "Account ID should be 87654321");
    }

    @Test
    void create_savings_account_initial_balance() {
        commandProcessor.process("create banking.Savings 87654321 2.5");
        double actual = bank.getAccount().get("87654321").getBalance();
        assertEquals(0.0, actual, 0.01, "Initial balance should be 0.0");
    }

    @Test
    void create_cd_account_type() {
        commandProcessor.process("create CD 11223344 1.5 1000");
        String actual = bank.getAccount().get("11223344").getClass().getSimpleName();
        assertEquals("CertificateOfDeposit", actual, "Account should be of type banking.CertificateOfDeposit");
    }

    @Test
    void create_cd_account_apr() {
        commandProcessor.process("create CD 11223344 1.5 1000");
        CertificateOfDeposit cdAccount = (CertificateOfDeposit) bank.getAccount().get("11223344");
        double actual = cdAccount.getApr();
        assertEquals(1.5, actual, "APR should be set to 1.5");
    }


    //Deposit Test
    @Test
    void deposit_to_account_balance() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        commandProcessor.process("deposit 12345678 100.0");
        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(100.0, actual, 0.01, "Balance after deposit should be 100.0");
    }

    @Test
    void deposit_to_checking_account_within_limit() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        commandProcessor.process("deposit 12345678 1000.0");
        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(1000.0, actual, 0.01, "Balance after maximum allowed deposit to checking account should be 1000.0");
    }

    @Test
    void deposit_to_checking_account_exceeds_limit() {
        commandProcessor.process("create banking.checking 12345678 3.5");
        commandProcessor.process("deposit 12345678 1500.0");
        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(0.0, actual, 0.01, "Deposit exceeding $1000 should not be allowed for checking account");
    }

    @Test
    void deposit_to_savings_account_within_limit() {
        commandProcessor.process("create banking.savings 87654321 2.5");
        commandProcessor.process("deposit 87654321 2500.0");
        double actual = bank.getAccount().get("87654321").getBalance();
        assertEquals(2500.0, actual, 0.01, "Balance after maximum allowed deposit to savings account should be 2500.0");
    }

    @Test
    void deposit_to_savings_account_exceeds_limit() {
        commandProcessor.process("create banking.savings 87654321 2.5");
        commandProcessor.process("deposit 87654321 3000.0");
        double actual = bank.getAccount().get("87654321").getBalance();
        assertEquals(0.0, actual, 0.01, "Deposit exceeding $2500 should not be allowed for savings account");
    }

    @Test
    void deposit_to_cd_account_not_allowed() {
        commandProcessor.process("create cd 11223344 1.5 1000");
        commandProcessor.process("deposit 11223344 500.0");
        double actual = bank.getAccount().get("11223344").getBalance();
        assertEquals(1000.0, actual, 0.01, "Deposits should not be allowed for CD accounts, so the balance should remain at the initial amount.");
    }



    @Test
    void withdraw_from_checking_account_exceeds_balance() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        commandProcessor.process("deposit 12345678 1000.0");
        commandProcessor.process("withdraw 12345678 1500.0");
        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(1000.0, actual, 0.01, "Balance should remain 1000.0 as withdrawal exceeds balance");
    }

    @Test
    void withdraw_from_savings_account_exceeds_balance() {
        commandProcessor.process("create banking.Savings 87654321 2.5");
        commandProcessor.process("deposit 87654321 2500.0");
        commandProcessor.process("withdraw 87654321 3000.0");
        double actual = bank.getAccount().get("87654321").getBalance();
        assertEquals(2500.0, actual, 0.01, "Balance should remain 2500.0 as withdrawal exceeds balance");
    }

    @Test
    void withdraw_from_checking_account() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        commandProcessor.process("deposit 12345678 500.0");
        commandProcessor.process("withdraw 12345678 200.0");

        double actual = bank.getAccount().get("12345678").getBalance();

        assertEquals(300.0, actual, 0.01, "Balance should be correctly updated after withdrawing $200 from a checking account.");
    }

    @Test
    void withdraw_more_than_balance() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        commandProcessor.process("deposit 12345678 500.0");
        commandProcessor.process("withdraw 12345678 600.0");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(500.0, actual, 0.01, "Balance should remain the same after trying to withdraw more than balance.");
    }

    @Test
    void withdraw_more_than_checking_limit() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        commandProcessor.process("deposit 12345678 500.0");
        commandProcessor.process("withdraw 12345678 500.0");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(500.0, actual, 0.01, "Balance should remain the same after exceeding the withdrawal limit for checking.");
    }

    @Test
    void withdraw_negative_amount() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        commandProcessor.process("deposit 12345678 500.0");
        commandProcessor.process("withdraw 12345678 -100.0");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(500.0, actual, 0.01, "Balance should remain the same after trying to withdraw a negative amount.");
    }

    @Test
    void withdraw_from_non_existent_account() {
        commandProcessor.process("withdraw 99999999 100.0");

        // Check that no accounts are affected
        assertTrue(bank.getAccount().isEmpty(), "No accounts should exist for non-existent account withdrawal.");
    }

    @Test
    void withdraw_from_cd_account_before_12_months() {
        commandProcessor.process("create CD 12345678 5.0 1000");
        commandProcessor.process("withdraw 12345678 500.0");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(1000.0, actual, 0.01, "Balance should remain the same after attempting to withdraw from a CD account before 12 months.");
    }

    @Test
    void withdraw_full_balance_from_cd_account() {
        commandProcessor.process("create cd 12345678 5.0 1000");
        commandProcessor.process("passtime 13");  // Simulate the passing of 12 months.
        commandProcessor.process("withdraw 12345678 2000");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(0.0, actual, 0.01, "Balance should be zero after withdrawing the full balance from a CD account.");
    }
    //Actual   :1016.7711229865932
    @Test
    void withdraw_from_empty_account() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        commandProcessor.process("withdraw 12345678 100.0");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(0.0, actual, 0.01, "Balance should remain the same after attempting to withdraw from an empty account.");
    }

    @Test
    void passtime_no_effect_if_limits_not_reached() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        commandProcessor.process("deposit 12345678 500.0");
        commandProcessor.process("passtime");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(500.0, actual, 0.01);
    }

    @Test
    void passtime_resets_savings_withdrawal_limit() {
        commandProcessor.process("create banking.Savings 12345678 3.5");
        commandProcessor.process("deposit 12345678 1000.0");
        commandProcessor.process("withdraw 12345678 500.0");
        commandProcessor.process("passtime");
        commandProcessor.process("withdraw 12345678 500.0");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(500.0, actual, 0.01);
    }

    @Test
    void passtime_cd_account_withdrawal_allowed_after_12_months() {
        commandProcessor.process("create cd 12345678 5.0 1000");

        // Simulate passing 13 months
        CertificateOfDeposit cdAccount = (CertificateOfDeposit) bank.getAccount().get("12345678");
        cdAccount.passTime(13); // Simulate the passage of 13 months

        // Now perform the withdrawal
        commandProcessor.process("withdraw 12345678 1020");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(0, actual, 0.01); // Expecting balance to be 0 after withdrawal
    }


    @Test
    void passtime_cd_account_withdrawal_not_allowed_before_12_months() {
        commandProcessor.process("create cd 12345678 5.0 1000");
        commandProcessor.process("withdraw 12345678 1030.0");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(1000.0, actual, 0.01);
    }

    @Test
    void passtime_no_change_on_empty_account() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        commandProcessor.process("passtime");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(0.0, actual, 0.01);
    }
    @Test
    void passtime_multiple_accounts() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        commandProcessor.process("create banking.Savings 23456789 1.2");
        commandProcessor.process("deposit 12345678 500.0");
        commandProcessor.process("deposit 23456789 1000.0");
        commandProcessor.process("passtime");

        double checkingBalance = bank.getAccount().get("12345678").getBalance();
        double savingsBalance = bank.getAccount().get("23456789").getBalance();

        assertEquals(500.0, checkingBalance, 0.01);
        assertEquals(1000.0, savingsBalance, 0.01);
    }

    @Test
    void passtime_does_not_process_pending_withdrawals() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        commandProcessor.process("deposit 12345678 500.0");
        commandProcessor.process("withdraw 12345678 600.0");
        commandProcessor.process("passtime");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(500.0, actual, 0.01);
    }

    @Test
    void passtime_checking_account_withdrawal_allowed() {
        commandProcessor.process("create banking.checking 12345678 0.0");
        commandProcessor.process("deposit 12345678 1000.0");

        commandProcessor.process("passtime 1");

        commandProcessor.process("withdraw 12345678 400");

        double actualBalance = bank.getAccount().get("12345678").getBalance();
        assertEquals(600.0, actualBalance, 0.01, "Checking account withdrawal should reduce balance by the specified amount (up to $400).");
    }

    @Test
    void passtime_savings_account_withdrawal_allowed() {
        commandProcessor.process("create banking.savings 12345678 0.0");
        commandProcessor.process("deposit 12345678 2000.0");

        commandProcessor.process("passtime 1");

        commandProcessor.process("withdraw 12345678 1000");

        double actualBalance = bank.getAccount().get("12345678").getBalance();
        assertEquals(1000.0, actualBalance, 0.01, "Savings account withdrawal should reduce balance by the specified amount (up to $1000).");

        commandProcessor.process("withdraw 12345678 500");
        actualBalance = bank.getAccount().get("12345678").getBalance();
        assertEquals(1000.0, actualBalance, 0.01, "Savings account allows only one withdrawal per month.");
    }

    @Test
    void passtime_invalid_negative_withdrawal() {
        commandProcessor.process("create banking.checking 12345678 0.0");
        commandProcessor.process("deposit 12345678 1000.0");

        commandProcessor.process("passtime 1");

        commandProcessor.process("withdraw 12345678 -500");

        double actualBalance = bank.getAccount().get("12345678").getBalance();
        assertEquals(1000.0, actualBalance, 0.01, "Negative withdrawals are not allowed.");
    }

    @Test
    void passtime_zero_withdrawal_amount() {
        commandProcessor.process("create banking.checking 12345678 0.0");
        commandProcessor.process("deposit 12345678 1000.0");

        commandProcessor.process("passtime 1");

        commandProcessor.process("withdraw 12345678 0");

        double actualBalance = bank.getAccount().get("12345678").getBalance();
        assertEquals(1000.0, actualBalance, 0.01, "Zero withdrawals are not allowed.");
    }

    @Test
    void passtime_close_account() {
        commandProcessor.process("create checking 12345678 0.0");
        commandProcessor.process("deposit 12345678 1000.0");

        commandProcessor.process("passtime 1");

        commandProcessor.process("withdraw 12345678 100");

        double actualBalance = bank.getAccount().get("12345678") == null ? 0.0 : bank.getAccount().get("12345678").getBalance();
        assertEquals(0.0, actualBalance, 0.01, "Account should not exist after closing, and balance should be zero.");
    }

    @Test
    void passtime_apr_checking() {
        commandProcessor.process("create banking.checking 12345678 3");
        commandProcessor.process("deposit 12345678 1000.0");

        commandProcessor.process("passtime 1");

        double expectedBalance = 1000.0 * (1 + (3.0 / 100) / 12);
        double actualBalance = bank.getAccount().get("12345678").getBalance();
        assertEquals(expectedBalance, actualBalance, 0.01, "APR should be applied correctly to Checking account.");
    }

    @Test
    void passtime_apr_savings() {
        commandProcessor.process("create banking.savings 12345678 5");
        commandProcessor.process("deposit 12345678 1000.0");

        commandProcessor.process("passtime 1");

        double expectedBalance = 1000.0 * (1 + (5.0 / 100) / 12);
        double actualBalance = bank.getAccount().get("12345678").getBalance();
        assertEquals(expectedBalance, actualBalance, 0.01, "APR should be applied correctly to Savings account.");
    }

    @Test
    void passtime_apr_cd() {
        commandProcessor.process("create cd 12345678 2.1 2000");

        commandProcessor.process("passtime 1");

        double aprDecimal = 2.1 / 100;
        double monthlyAPR = aprDecimal / 12;
        double balance = 2000.0;

        for (int i = 0; i < 4; i++) {
            balance *= (1 + monthlyAPR);
        }

        double actualBalance = bank.getAccount().get("12345678").getBalance();
        assertEquals(balance, actualBalance, 0.01, "APR should be applied 4 times for CD account.");
    }

    @Test
    void passtime_deduction_checking() {
        commandProcessor.process("create banking.checking 12345678 3");
        commandProcessor.process("deposit 12345678 50.0");

        commandProcessor.process("passtime 1");

        double expectedBalance = 25.0 + (25.0 * 0.03 / 12); // Account for APR
        double actualBalance = bank.getAccount().get("12345678").getBalance();
        assertEquals(expectedBalance, actualBalance, 0.01, "Low balance should result in a $25 deduction in Checking account.");
    }

    @Test
    void passtime_deduction_savings() {
        commandProcessor.process("create banking.savings 12345678 4");
        commandProcessor.process("deposit 12345678 80.0");

        commandProcessor.process("passtime 1");

        double expectedBalance = 55.0 + (55.0 * 0.04 / 12);
        double actualBalance = bank.getAccount().get("12345678").getBalance();
        assertEquals(expectedBalance, actualBalance, 0.01, "Low balance should result in a $25 deduction in Savings account and apply APR.");
    }
    @Test
    void passtime_close_zero_balance() {
        commandProcessor.process("create banking.checking 12345678 3");

        commandProcessor.process("passtime 1");

        assertNull(bank.getAccount().get("12345678"), "Account should be closed if the balance is $0.");
    }

    @Test
    void passtime_apr_zero_percent() {
        commandProcessor.process("create banking.checking 12345678 0");
        commandProcessor.process("deposit 12345678 1000.0");

        commandProcessor.process("passtime 1");

        double actualBalance = bank.getAccount().get("12345678").getBalance();
        assertEquals(1000.0, actualBalance, 0.01, "Balance should remain the same when APR is 0%.");
    }

    @Test
    void transfer_valid_checking_to_checking() {
        commandProcessor.process("create banking.checking 12345678 3");  // Create checking account 12345678 with 3% APR
        commandProcessor.process("create banking.checking 98765432 3");  // Create checking account 98765432 with 3% APR
        commandProcessor.process("deposit 12345678 500.0");  // Deposit 500.0 into checking account 12345678
        commandProcessor.process("deposit 98765432 200.0");  // Deposit 200.0 into checking account 98765432

        commandProcessor.process("transfer 12345678 98765432 200.0");  // Transfer 200 from account 12345678 to 98765432

        double expectedBalanceFrom = 500.0 - 200.0;  // 12345678 balance should be 300.0
        double expectedBalanceTo = 200.0 + 200.0;    // 98765432 balance should be 400.0
        double actualBalanceFrom = bank.getAccount().get("12345678").getBalance();
        double actualBalanceTo = bank.getAccount().get("98765432").getBalance();

        assertEquals(expectedBalanceFrom, actualBalanceFrom, 0.01, "Source checking account balance should decrease correctly.");
        assertEquals(expectedBalanceTo, actualBalanceTo, 0.01, "Destination checking account balance should increase correctly.");
    }

    @Test
    void transfer_checking_to_savings() {
        commandProcessor.process("create banking.checking 12345678 3");  // Create checking account 12345678 with 3% APR
        commandProcessor.process("create banking.savings 98765432 4");   // Create savings account 98765432 with 4% APR
        commandProcessor.process("deposit 12345678 500.0");  // Deposit 500.0 into checking account 12345678
        commandProcessor.process("deposit 98765432 100.0");  // Deposit 100.0 into savings account 98765432

        commandProcessor.process("transfer 12345678 98765432 300.0");  // Transfer 300 from checking to savings

        double expectedBalanceFrom = 500.0 - 300.0;  // 12345678 balance should be 200.0
        double expectedBalanceTo = 100.0 + 300.0;    // 98765432 balance should be 400.0
        double actualBalanceFrom = bank.getAccount().get("12345678").getBalance();
        double actualBalanceTo = bank.getAccount().get("98765432").getBalance();

        assertEquals(expectedBalanceFrom, actualBalanceFrom, 0.01, "Source checking account balance should decrease correctly.");
        assertEquals(expectedBalanceTo, actualBalanceTo, 0.01, "Destination savings account balance should increase correctly.");
    }

    @Test
    void transfer_savings_to_checking() {
        commandProcessor.process("create banking.savings 12345678 4");   // Create savings account 12345678 with 4% APR
        commandProcessor.process("create banking.checking 98765432 3");  // Create checking account 98765432 with 3% APR
        commandProcessor.process("deposit 12345678 500.0");  // Deposit 500.0 into savings account 12345678
        commandProcessor.process("deposit 98765432 50.0");   // Deposit 50.0 into checking account 98765432

        commandProcessor.process("transfer 12345678 98765432 200.0");  // Transfer 200 from savings to checking

        double expectedBalanceFrom = 500.0 - 200.0;  // 12345678 balance should be 300.0
        double expectedBalanceTo = 50.0 + 200.0;    // 98765432 balance should be 250.0
        double actualBalanceFrom = bank.getAccount().get("12345678").getBalance();
        double actualBalanceTo = bank.getAccount().get("98765432").getBalance();

        assertEquals(expectedBalanceFrom, actualBalanceFrom, 0.01, "Source savings account balance should decrease correctly.");
        assertEquals(expectedBalanceTo, actualBalanceTo, 0.01, "Destination checking account balance should increase correctly.");
    }

    @Test
    void transfer_insufficient_funds() {
        commandProcessor.process("create banking.checking 12345678 3");
        commandProcessor.process("create banking.savings 98765432 4");

        commandProcessor.process("deposit 12345678 100.0");
        commandProcessor.process("deposit 98765432 50.0");

        commandProcessor.process("transfer 12345678 98765432 200.0");

        double expectedBalanceFrom = 100.0;
        double expectedBalanceTo = 50.0;

        double actualBalanceFrom = bank.getAccount().get("12345678").getBalance();
        double actualBalanceTo = bank.getAccount().get("98765432").getBalance();

        assertEquals(expectedBalanceFrom, actualBalanceFrom, 0.01, "Source account balance should remain the same.");
        assertEquals(expectedBalanceTo, actualBalanceTo, 0.01, "Destination account balance should remain the same.");
    }


    @Test
    void transfer_savings_to_savings() {
        commandProcessor.process("create banking.savings 12345678 3");   // Create savings account 12345678 with 3% APR
        commandProcessor.process("create banking.savings 98765432 4");   // Create savings account 98765432 with 4% APR
        commandProcessor.process("deposit 12345678 1000.0");  // Deposit 1000.0 into savings account 12345678
        commandProcessor.process("deposit 98765432 200.0");  // Deposit 200.0 into savings account 98765432

        commandProcessor.process("transfer 12345678 98765432 500.0");  // Transfer 500 from one savings account to another

        double expectedBalanceFrom = 1000.0 - 500.0;  // 12345678 balance should be 500.0
        double expectedBalanceTo = 200.0 + 500.0;    // 98765432 balance should be 700.0
        double actualBalanceFrom = bank.getAccount().get("12345678").getBalance();
        double actualBalanceTo = bank.getAccount().get("98765432").getBalance();

        assertEquals(expectedBalanceFrom, actualBalanceFrom, 0.01, "Source savings account balance should decrease correctly.");
        assertEquals(expectedBalanceTo, actualBalanceTo, 0.01, "Destination savings account balance should increase correctly.");
    }





































}
