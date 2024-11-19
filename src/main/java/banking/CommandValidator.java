package banking;

public class CommandValidator {
    private final Bank bank;

    public CommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean validate(String command) {
        if (command.startsWith("create")) {
            CreateCommandValidator createValidator = new CreateCommandValidator(bank);
            return createValidator.validate(command);
        } else if (command.startsWith("deposit")) {
            DepositCommandValidator depositValidator = new DepositCommandValidator(bank);
            return depositValidator.validate(command);
        }
        return false;
    }
}
