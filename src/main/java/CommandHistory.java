import java.util.ArrayList;
import java.util.List;

public class CommandHistory {
    private final List<String> invalidCommands;

    public CommandHistory() {
        this.invalidCommands = new ArrayList<>();
    }

    public void addInvalidCommand(String command) {
        invalidCommands.add(command);
    }

    public List<String> getInvalidCommands() {
        return new ArrayList<>(invalidCommands);
    }
}
