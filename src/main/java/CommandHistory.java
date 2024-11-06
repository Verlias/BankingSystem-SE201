import java.util.ArrayList;
import java.util.List;

public class CommandHistory {
    private final List<String> invalidCommands;

    public CommandHistory() {
        this.invalidCommands = new ArrayList<>();
    }

    // Method to add an invalid command to the history
    public void addInvalidCommand(String command) {
        invalidCommands.add(command);
    }

    // Method to retrieve all invalid commands
    public List<String> getInvalidCommands() {
        return new ArrayList<>(invalidCommands);
    }
}
