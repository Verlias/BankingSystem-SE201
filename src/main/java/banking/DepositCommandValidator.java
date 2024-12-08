package banking;
// t
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
            if (amount < 0) {
                System.out.println("Deposit amount cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid deposit amount: " + amountString);
            return false;
        }

        String accountType = bank.getAccountType(accountId);

        switch (accountType.toLowerCase()) {
            case "savings":
                if (amount > 2500 || amount == 0) {
                    System.out.println("Deposit amount exceeds the $2500 limit for savings accounts.");
                    return false;
                }
                break;

            case "checking":
                if (amount > 1000 || amount == 0) {
                    System.out.println("Deposit amount exceeds the $1000 limit for checking accounts.");
                    return false;
                }
                break;

            case "certificateofdeposit":
                System.out.println("Deposits are not allowed for Certificate of Deposit (CD) accounts.");
                return false;

            default:
                System.out.println("Unknown account type for account ID " + accountId);
                return false;
        }

        return true;
    }
}
