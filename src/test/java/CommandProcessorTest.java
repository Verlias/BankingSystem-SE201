import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandProcessorTest {

    private Bank bank;
    private CommandProcessor commandProcessor;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        commandProcessor = new CommandProcessor(bank);
    }

    // Testing creation of Checking account
    @Test
    void testCreateCheckingAccountType() {
        commandProcessor.process("create 12345678 Checking 3.5");
        String actual = bank.getAccount().get("12345678").getClass().getSimpleName();
        assertEquals("Checking", actual, "Account should be of type Checking");
    }

    @Test
    void testCreateCheckingAccountAPR() {
        commandProcessor.process("create 12345678 Checking 3.5");
        double actual = bank.getAccount().get("12345678").getApr();
        assertEquals(3.5, actual, 0.01, "APR should be 3.5");
    }

    @Test
    void testCreateCheckingAccountId() {
        commandProcessor.process("create 12345678 Checking 3.5");
        String actual = bank.getAccount().get("12345678").getId();
        assertEquals("12345678", actual, "Account ID should be 12345678");
    }

    @Test
    void testCreateCheckingAccountInitialBalance() {
        commandProcessor.process("create 12345678 Checking 3.5");
        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(0.0, actual, 0.01, "Initial balance should be 0.0");
    }

    // Testing creation of Savings account
    @Test
    void testCreateSavingsAccountType() {
        commandProcessor.process("create 87654321 Savings 2.5");
        String actual = bank.getAccount().get("87654321").getClass().getSimpleName();
        assertEquals("Savings", actual, "Account should be of type Savings");
    }

    @Test
    void testCreateSavingsAccountAPR() {
        commandProcessor.process("create 87654321 Savings 2.5");
        double actual = bank.getAccount().get("87654321").getApr();
        assertEquals(2.5, actual, 0.01, "APR should be 2.5");
    }

    @Test
    void testCreateSavingsAccountId() {
        commandProcessor.process("create 87654321 Savings 2.5");
        String actual = bank.getAccount().get("87654321").getId();
        assertEquals("87654321", actual, "Account ID should be 87654321");
    }

    @Test
    void testCreateSavingsAccountInitialBalance() {
        commandProcessor.process("create 87654321 Savings 2.5");
        double actual = bank.getAccount().get("87654321").getBalance();
        assertEquals(0.0, actual, 0.01, "Initial balance should be 0.0");
    }

    // Testing deposit functionality
    @Test
    void testDepositToAccountBalance() {
        commandProcessor.process("create 12345678 Checking 3.5");
        commandProcessor.process("deposit 12345678 100.0");
        double actual = bank.getAccount().get("12345678").getBalance();
        assertEquals(100.0, actual, 0.01, "Balance after deposit should be 100.0");
    }

    // Testing creation of CD account
    @Test
    void testCreateCDAccountType() {
        commandProcessor.process("create 11223344 CD 1.5");
        String actual = bank.getAccount().get("11223344").getClass().getSimpleName();
        assertEquals("CertificateOfDeposit", actual, "Account should be of type CertificateOfDeposit");
    }

    @Test
    void testCreateCDAccountAPR() {
        commandProcessor.process("create 11223344 CD 1.5");
        CertificateOfDeposit cdAccount = (CertificateOfDeposit) bank.getAccount().get("11223344");
        double actual = cdAccount.getApr();
        assertEquals(1.5, actual, "APR should be set to 1.5");
    }
}
