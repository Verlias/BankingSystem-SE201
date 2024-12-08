package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PassTimeCommandValidatorTest {

    private PassTimeCommandValidator passTimeCommandValidator;
    private CommandProcessor commandProcessor;
    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        passTimeCommandValidator = new PassTimeCommandValidator();
        commandProcessor = new CommandProcessor(bank);

        // Create a checking account for testing
        commandProcessor.process("create banking.Checking 89456185 3.5");
        commandProcessor.process("deposit 89456185 200.0");  // Deposit some money to the account
    }

    // Test the 'passtime' command directly using PassTimeCommandValidator
    @Test
    public void test_valid_passtime_command() {
        String command = "passtime 12";  // Valid command with no extra spaces
        boolean result = passTimeCommandValidator.validate(command);
        assertTrue(result, "PassTime command should be valid with a valid month value.");
    }

    @Test
    public void test_passtime_with_extra_spaces() {
        String command = "   passtime   12   ";  // Command with extra spaces
        boolean result = passTimeCommandValidator.validate(command);
        assertTrue(result, "PassTime command should be valid even with extra spaces.");
    }

    @Test
    public void test_invalid_passtime_command() {
        String command = "passtime -1";  // Invalid month value (negative)
        boolean result = passTimeCommandValidator.validate(command);
        assertFalse(result, "PassTime command should be invalid with a negative month value.");
    }

    @Test
    public void test_passtime_with_too_many_months() {
        String command = "passtime 61";  // Invalid month value (greater than 60)
        boolean result = passTimeCommandValidator.validate(command);
        assertFalse(result, "PassTime command should be invalid with more than 60 months.");
    }

    @Test
    public void test_passtime_with_non_numeric_value() {
        String command = "passtime abc";  // Invalid month value (non-numeric)
        boolean result = passTimeCommandValidator.validate(command);
        assertFalse(result, "PassTime command should be invalid with a non-numeric month value.");
    }

    @Test
    public void test_invalid_command() {
        String command = "withdraw 12345 500";  // Command that doesn't start with 'passtime'
        boolean result = passTimeCommandValidator.validate(command);
        assertFalse(result, "Command starting with 'withdraw' should not be valid for PassTimeValidator.");
    }

    @Test
    public void test_zero_month_value() {
        String command = "passtime 0";  // Invalid month value (zero)
        boolean result = passTimeCommandValidator.validate(command);
        assertFalse(result, "PassTime command should be invalid with zero as the month value.");
    }

    // Test account creation and processing passtime with account-related data
    @Test
    public void test_passtime_with_valid_account() {
        // Create and deposit some money into the account
        commandProcessor.process("create banking.Checking 11223 5.0");
        commandProcessor.process("deposit 11223 300.0");

        // Now pass time for the account
        String command = "passtime 24";  // Valid command for account after passing time
        boolean result = passTimeCommandValidator.validate(command);
        assertTrue(result, "PassTime command should be valid for an account after passing time.");
    }
}
