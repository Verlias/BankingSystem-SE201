package banking;

public class TransferCommandValidator {
	private final Bank bank;

	public TransferCommandValidator(Bank bank) {
		this.bank = bank;
	}

	public boolean validate(String command) {
		// Trim any leading or trailing spaces before processing
		command = command.trim();

		String[] parts = command.split("\\s+");

		// Ensure that the command has the correct number of arguments (4)
		if (parts.length != 4) {
			System.out.println("Invalid transfer command. Usage: transfer <fromId> <toId> <amount>");
			return false;
		}

		String fromId = parts[1];
		String toId = parts[2];
		double amount;

		// Parse the amount
		try {
			amount = Double.parseDouble(parts[3]);
		} catch (NumberFormatException e) {
			System.out.println("Invalid transfer amount: " + parts[3]);
			return false;
		}

		// Ensure the transfer amount is positive
		if (amount <= 0) {
			System.out.println("Transfer amount must be greater than 0.");
			return false;
		}

		// Check if the source and destination accounts exist
		if (!bank.accountExists(fromId)) {
			System.out.println("Account with ID " + fromId + " does not exist.");
			return false;
		}

		if (!bank.accountExists(toId)) {
			System.out.println("Account with ID " + toId + " does not exist.");
			return false;
		}

		Accounts fromAccount = bank.getAccount().get(fromId);
		Accounts toAccount = bank.getAccount().get(toId);

		if (fromAccount instanceof CertificateOfDeposit || toAccount instanceof CertificateOfDeposit) {
			System.out.println("Error: CD accounts cannot be part of a transfer.");
			return false;
		}

		double availableBalance = fromAccount.getBalance();
		if (availableBalance < amount) {
			System.out.println("Insufficient balance in account " + fromId + " for the transfer. Only "
					+ availableBalance + " will be transferred.");
			amount = availableBalance; // Transfer the available balance instead of the requested amount
		}

		fromAccount.withdraw(amount);
		toAccount.addDeposit(amount);

		// Inform the user of the successful transfer
		System.out.println("Transferred " + amount + " from account " + fromId + " to account " + toId);
		return true;
	}
}
