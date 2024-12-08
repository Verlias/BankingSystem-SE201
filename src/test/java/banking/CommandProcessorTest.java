package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        commandProcessor.process("create CD 11223344 1.5");
        String actual = bank.getAccount().get("11223344").getClass().getSimpleName();
        assertEquals("CertificateOfDeposit", actual, "Account should be of type banking.CertificateOfDeposit");
    }

    @Test
    void create_cd_account_apr() {
        commandProcessor.process("create CD 11223344 1.5");
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
        commandProcessor.process("create CD 11223344 1.5");
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
        commandProcessor.process("create CD 12345678 5.0");
        commandProcessor.process("deposit 12345678 1000.0");
        commandProcessor.process("withdraw 12345678 500.0");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(1000.0, actual, 0.01, "Balance should remain the same after attempting to withdraw from a CD account before 12 months.");
    }

    @Test
    void withdraw_full_balance_from_cd_account() {
        commandProcessor.process("create CD 12345678 5.0");
        commandProcessor.process("deposit 12345678 1000.0");
        // Assuming the 12-month condition is fulfilled
        commandProcessor.process("withdraw 12345678 1000.0");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(0.0, actual, 0.01, "Balance should be zero after withdrawing the full balance from a CD account.");
    }

    @Test
    void withdraw_from_empty_account() {
        commandProcessor.process("create banking.Checking 12345678 3.5");
        commandProcessor.process("withdraw 12345678 100.0");

        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(0.0, actual, 0.01, "Balance should remain the same after attempting to withdraw from an empty account.");
    }


















}
