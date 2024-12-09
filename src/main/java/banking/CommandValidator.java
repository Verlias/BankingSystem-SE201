package banking;

public class CommandValidator {
	private final Bank bank;

	public CommandValidator(Bank bank) {
		this.bank = bank;
	}

	public boolean validate(String command) {
		if (command == null) {
			System.out.println("Invalid command: null input");
			return false;
		}

		command = command.trim();

		if (command.startsWith("create")) {
			CreateCommandValidator createValidator = new CreateCommandValidator(bank);
			return createValidator.validate(command);
		} else if (command.startsWith("deposit")) {
			DepositCommandValidator depositValidator = new DepositCommandValidator(bank);
			return depositValidator.validate(command);
		} else if (command.startsWith("withdraw")) {
			WithdrawCommandValidator withdrawValidator = new WithdrawCommandValidator(bank);
			return withdrawValidator.validate(command);
		} else if (command.startsWith("passtime")) {
			PassTimeCommandValidator passTimeValidator = new PassTimeCommandValidator();
			return passTimeValidator.validate(command);
		} else if (command.startsWith("transfer")) {
			TransferCommandValidator transferValidator = new TransferCommandValidator(bank);
			return transferValidator.validate(command);
		}

		// If command doesn't match any known prefix
		System.out.println("Unknown command: " + command);
		return false;
	}

}
