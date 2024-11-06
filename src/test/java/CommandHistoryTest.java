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
}
