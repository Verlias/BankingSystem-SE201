package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandHistoryTest {

    private CommandHistory commandHistory;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
    }

    // Invalid command tests
    @Test
    void add_single_invalid_command() {
        commandHistory.addInvalidCommand("invalid command 1");
        assertEquals(1, commandHistory.getInvalidCommands().size(), "Should contain one invalid command");
    }

    @Test
    void add_multiple_invalid_commands() {
        commandHistory.addInvalidCommand("invalid command 1");
        commandHistory.addInvalidCommand("invalid command 2");
        assertEquals(2, commandHistory.getInvalidCommands().size(), "Should contain two invalid commands");
    }

    @Test
    void contains_specific_invalid_command_1() {
        commandHistory.addInvalidCommand("invalid command 1");
        List<String> commands = commandHistory.getInvalidCommands();
        assertTrue(commands.contains("invalid command 1"), "List should contain 'invalid command 1'");
    }

    @Test
    void contains_specific_invalid_command_2() {
        commandHistory.addInvalidCommand("invalid command 2");
        List<String> commands = commandHistory.getInvalidCommands();
        assertTrue(commands.contains("invalid command 2"), "List should contain 'invalid command 2'");
    }

    @Test
    void get_invalid_commands_returns_copy() {
        commandHistory.addInvalidCommand("invalid command");
        List<String> retrievedCommands = commandHistory.getInvalidCommands();
        retrievedCommands.clear();
        assertEquals(1, commandHistory.getInvalidCommands().size(), "Original list should be unaffected by external modifications");
    }

    // Valid command tests
    @Test
    void add_single_valid_command() {
        commandHistory.addValidCommand("valid command 1");
        assertEquals(1, commandHistory.getValidCommands().size(), "Should contain one valid command");
    }

    @Test
    void add_multiple_valid_commands() {
        commandHistory.addValidCommand("valid command 1");
        commandHistory.addValidCommand("valid command 2");
        assertEquals(2, commandHistory.getValidCommands().size(), "Should contain two valid commands");
    }

    @Test
    void contains_specific_valid_command_1() {
        commandHistory.addValidCommand("valid command 1");
        List<String> commands = commandHistory.getValidCommands();
        assertTrue(commands.contains("valid command 1"), "List should contain 'valid command 1'");
    }

    @Test
    void contains_specific_valid_command_2() {
        commandHistory.addValidCommand("valid command 2");
        List<String> commands = commandHistory.getValidCommands();
        assertTrue(commands.contains("valid command 2"), "List should contain 'valid command 2'");
    }

    @Test
    void get_valid_commands_returns_copy() {
        commandHistory.addValidCommand("valid command");
        List<String> retrievedCommands = commandHistory.getValidCommands();
        retrievedCommands.clear();
        assertEquals(1, commandHistory.getValidCommands().size(), "Original list should be unaffected by external modifications");
    }
}