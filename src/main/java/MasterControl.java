import java.util.List;

public class MasterControl {
    private CommandValidator commandValidator;
    private CommandProcessor commandProcessor;
    private CommandHistory commandHistory;

    public MasterControl(CommandValidator commandValidator,
                         CommandProcessor commandProcessor, CommandHistory commandHistory) {
        this.commandValidator = commandValidator;
        this.commandProcessor = commandProcessor;
        this.commandHistory = commandHistory;
    }

    public List<String> start(List<String> input) {
        commandHistory.addInvalidCommand(input.get(0));
        return commandHistory.getInvalidCommands();
    }
}
