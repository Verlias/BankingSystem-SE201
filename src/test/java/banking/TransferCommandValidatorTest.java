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
        commandProcessor.process("create banking.Checking 12345678 3.5"); // Account 1
        commandProcessor.process("deposit 12345678 500.0");  // Deposit some money into account 1

        commandProcessor.process("create banking.Checking 67890789 2.5"); // Account 2
        commandProcessor.process("deposit 67890789 200.0");  // Deposit some money into account 2

        // Create a CertificateOfDeposit (CD) account to test CD checks
        commandProcessor.process("create cd 11223678 5.0 1000"); // CD Account
    }

    // Test valid transfer
    @Test
    public void testValidTransferCommand() {
        String command = "transfer 12345678 67890789 100.0";  // Valid transfer
        boolean result = transferCommandValidator.validate(command);
        assertTrue(result, "Transfer command should be valid with valid account IDs and amount.");
    }

    // Test transfer with invalid source account ID
    @Test
    public void testInvalidSourceAccount() {
        String command = "transfer 99999 67890789 100.0";  // Invalid source account ID
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail with an invalid source account ID.");
    }

    // Test transfer with invalid destination account ID
    @Test
    public void testInvalidDestinationAccount() {
        String command = "transfer 12345678 99999 100.0";  // Invalid destination account ID
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail with an invalid destination account ID.");
    }


    // Test transfer with a negative amount
    @Test
    public void testNegativeTransferAmount() {
        String command = "transfer 12345678 67890789 -100.0";  // Invalid negative transfer amount
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail with a negative transfer amount.");
    }

    // Test transfer with zero amount
    @Test
    public void testZeroTransferAmount() {
        String command = "transfer 12345678 67890789 0.0";  // Zero transfer amount
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail with zero as the transfer amount.");
    }

    // Test transfer from a CertificateOfDeposit account
    @Test
    public void testTransferFromCDAccount() {
        String command = "transfer 11223678 67890789 100.0";  // Transfer from CD account
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail if the source account is a CertificateOfDeposit.");
    }

    // Test transfer to a CertificateOfDeposit account
    @Test
    public void testTransferToCDAccount() {
        String command = "transfer 12345678 11223678 100.0";  // Transfer to CD account
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail if the destination account is a CertificateOfDeposit.");
    }

    // Test transfer with invalid command format (too few arguments)
    @Test
    public void testInvalidCommandFormat() {
        String command = "transfer 12345678 67890789";  // Missing transfer amount
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail with an incorrect format.");
    }

    // Test transfer with invalid command (extra spaces)
    @Test
    public void testTransferWithExtraSpaces() {
        String command = "  transfer  12345678  67890789  100.0  ";  // Command with extra spaces
        boolean result = transferCommandValidator.validate(command);
        assertTrue(result, "Transfer command should be valid even with extra spaces.");
    }
    @Test
    public void testTransferFromCheckingToSavings() {
        String command = "transfer 12345678 67890789 50.0";  // Valid transfer from Checking to Savings
        boolean result = transferCommandValidator.validate(command);
        assertTrue(result, "Transfer from Checking to Savings account should be valid.");
    }

    @Test
    public void testTransferFromSavingsToChecking() {
        String command = "transfer 67890789 12345678 100.0";  // Valid transfer from Savings to Checking
        boolean result = transferCommandValidator.validate(command);
        assertTrue(result, "Transfer from Savings to Checking account should be valid.");
    }

    @Test
    public void testTransferFromCheckingToChecking() {
        String command = "transfer 12345678 67890789 200.0";  // Valid transfer from one Checking account to another
        boolean result = transferCommandValidator.validate(command);
        assertTrue(result, "Transfer between two Checking accounts should be valid.");
    }

    @Test
    public void testTransferFromSavingsToCD() {
        String command = "transfer 67890789 11223678 50.0";  // Valid transfer between two Savings accounts
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "CD accounts cannot be part of a transfer.");
    }

    @Test
    public void testTransferFromCheckingToCD() {
        String command = "transfer 12345678 11223678 50.0";  // Invalid transfer from Checking to CD account
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer from Checking to a CertificateOfDeposit account should not be allowed.");
    }

    @Test
    public void testTransferToCD() {
        String command = "transfer 12345678 11223678 50.0";  // Invalid transfer to a CD account
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer to a CertificateOfDeposit account should not be allowed.");
    }

    @Test
    public void testExcessiveAmountTransfer() {
        String command = "transfer 12345678 67890789 1002.0";  // Amount exceeds balance in source account
        boolean result = transferCommandValidator.validate(command);
        assertTrue(result, "Transfer command should only transfer the available balance, even if the amount exceeds it.");
    }

    @Test
    public void testPartialTransfer() {
        String command = "transfer 12345678 67890789 600.0";  // Transfer more than available balance
        boolean result = transferCommandValidator.validate(command);
        assertTrue(result, "Transfer should succeed but with the available balance only. Remaining balance should stay in source account.");
    }

    @Test
    public void testTransferWithExactAmount() {
        String command = "transfer 12345678 67890789 500.0";  // Transfer exact available balance
        boolean result = transferCommandValidator.validate(command);
        assertTrue(result, "Transfer should succeed when the amount matches the available balance exactly.");
    }

    @Test
    public void testInvalidTransferBetweenCheckingAndCD() {
        String command = "transfer 12345678 11223678 100.0";  // Invalid transfer from Checking to CD
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer between Checking and CertificateOfDeposit account should not be allowed.");
    }

    @Test
    public void testInvalidTransferBetweenSavingsAndCD() {
        String command = "transfer 67890789 11223678 100.0";  // Invalid transfer from Savings to CD
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer between Savings and CertificateOfDeposit account should not be allowed.");
    }

    @Test
    public void testTransferWithNonExistingAccount() {
        String command = "transfer 99999 88888 100.0";  // Both source and destination account IDs are invalid
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail if either source or destination account does not exist.");
    }

    @Test
    public void testTransferWithInvalidAccountType() {
        // Assume `InvalidAccountType` is not a valid account type
        String command = "transfer 12345678 67890789 100.0 InvalidAccountType";
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail if the account type is invalid.");
    }

    @Test
    public void testTransferWithNoSpacesBetweenArguments() {
        String command = "transfer12345678 987654 500.0";  // Missing space between arguments
        boolean result = transferCommandValidator.validate(command);
        assertFalse(result, "Transfer command should fail if there are no spaces between arguments.");
    }
















}
