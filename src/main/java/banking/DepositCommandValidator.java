package banking;

public class DepositCommandValidator {
    private final Bank bank;

    public DepositCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean validate(String command) {
        String[] parts = command.trim().split("\\s+");

        if (parts.length != 3) {
            System.out.println("Invalid command length. Expected format: deposit <id> <amount>");
            return false;
        }

        String keyword = parts[0].toLowerCase();
        String accountId = parts[1];
        String amountString = parts[2];

        if (!keyword.equals("deposit")) {
            System.out.println("Invalid command keyword. Expected 'deposit'");
            return false;
        }

        if (!bank.accountExists(accountId)) {
            System.out.println("Account with ID " + accountId + " does not exist.");
            return false;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountString);
            if (amount <= 0) {
                System.out.println("Deposit amount must be greater than zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid deposit amount: " + amountString);
            return false;
        }

        return true;
    }
}
