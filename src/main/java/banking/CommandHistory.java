package banking;

import java.util.ArrayList;
import java.util.List;

public class CommandHistory {
    private final List<String> invalidCommands;
    private final List<String> validCommands;

    public CommandHistory() {
        this.invalidCommands = new ArrayList<>();
        this.validCommands = new ArrayList<>();
    }

    public void addInvalidCommand(String command) {
        invalidCommands.add(command);
    }

    public void addValidCommand(String command) {
        validCommands.add(command);
    }

    public List<String> getInvalidCommands() {
        return new ArrayList<>(invalidCommands);
    }

    public List<String> getValidCommands() {
        return new ArrayList<>(validCommands);
    }
}
