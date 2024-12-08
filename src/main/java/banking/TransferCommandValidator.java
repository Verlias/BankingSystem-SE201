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

        // Ensure that CD accounts are not part of the transfer
        if (fromAccount instanceof CertificateOfDeposit || toAccount instanceof CertificateOfDeposit) {
            System.out.println("Error: CD accounts cannot be part of a transfer.");
            return false;
        }

        // Check if the source account has enough balance for the transfer
        if (fromAccount.getBalance() < amount) {
            System.out.println("Insufficient balance in account " + fromId + " for the transfer.");
            return false;
        }

        return true; // All validations passed
    }
}
