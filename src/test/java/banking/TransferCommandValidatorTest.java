package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransferCommandValidatorTest {

    private TransferCommandValidator transferCommandValidator;
    private CommandProcessor commandProcessor;
    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        transferCommandValidator = new TransferCommandValidator(bank);
        commandProcessor = new CommandProcessor(bank);

        // Create some test accounts
        commandProcessor.process("create banking.Checking 12345 3.5"); // Account 1
        commandProcessor.process("deposit 12345 500.0");  // Deposit some money into account 1

        commandProcessor.process("create banking.Checking 67890 2.5"); // Account 2
        commandProcessor.process("deposit 67890 200.0");  // Deposit some money into account 2

        // Create a CertificateOfDeposit (CD) account to test CD checks
        commandProcessor.process("create banking.CertificateOfDeposit 11223 5.0"); // CD Account
    }

    // Test valid transfer
    @Test
    public void testValidTransferCommand() {
        String command = "transfer 12345 67890 100.0";  // Valid transfer
        boolean result = transferCommandValidator.validate(command);
        assertTrue(result, "Transfer command should be valid with valid account IDs and amount.");
    }

    // Test transfer with invalid source account ID
    @Test
    public void testInvalidSourceAccount() {
        String command = "transfer 99999 67890 100.0";  // Invalid source account ID
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail with an invalid source account ID.");
    }

    // Test transfer with invalid destination account ID
    @Test
    public void testInvalidDestinationAccount() {
        String command = "transfer 12345 99999 100.0";  // Invalid destination account ID
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail with an invalid destination account ID.");
    }

    // Test transfer with insufficient balance in source account
    @Test
    public void testInsufficientBalance() {
        String command = "transfer 12345 67890 600.0";  // Amount exceeds balance in source account
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail if the source account has insufficient balance.");
    }

    // Test transfer with a negative amount
    @Test
    public void testNegativeTransferAmount() {
        String command = "transfer 12345 67890 -100.0";  // Invalid negative transfer amount
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail with a negative transfer amount.");
    }

    // Test transfer with zero amount
    @Test
    public void testZeroTransferAmount() {
        String command = "transfer 12345 67890 0.0";  // Zero transfer amount
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail with zero as the transfer amount.");
    }

    // Test transfer from a CertificateOfDeposit account
    @Test
    public void testTransferFromCDAccount() {
        String command = "transfer 11223 67890 100.0";  // Transfer from CD account
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail if the source account is a CertificateOfDeposit.");
    }

    // Test transfer to a CertificateOfDeposit account
    @Test
    public void testTransferToCDAccount() {
        String command = "transfer 12345 11223 100.0";  // Transfer to CD account
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail if the destination account is a CertificateOfDeposit.");
    }

    // Test transfer with invalid command format (too few arguments)
    @Test
    public void testInvalidCommandFormat() {
        String command = "transfer 12345 67890";  // Missing transfer amount
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail with an incorrect format.");
    }

    // Test transfer with invalid command (extra spaces)
    @Test
    public void testTransferWithExtraSpaces() {
        String command = "  transfer  12345  67890  100.0  ";  // Command with extra spaces
        boolean result = transferCommandValidator.validate(command);
        assertTrue(result, "Transfer command should be valid even with extra spaces.");
    }
}
