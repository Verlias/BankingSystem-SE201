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
        for (String command : input) {
            if (commandValidator.validate(command)){
                commandProcessor.process(command);
            } else {
                commandHistory.addInvalidCommand(command);
            }
        }
        return commandHistory.getInvalidCommands();
    }
}
