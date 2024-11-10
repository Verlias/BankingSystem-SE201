public class CommandProcessor {

    private final Bank bank;

    public CommandProcessor(Bank bank) {
        this.bank = bank;
    }

    public void process(String command) {
        // Split command by whitespace
        String[] parts = command.trim().split("\\s+");

        // Ensure the command has the correct number of parts
        if (parts.length < 2) {
            System.out.println("Invalid command format.");
            return;
        }

        // The action is the first part
        String action = parts[0].toLowerCase();

        // Handle the action based on its type
        switch (action) {
            case "create":
                if (parts.length != 4) {
                    System.out.println("Invalid create command. Usage: create <accountType> <id> <apr>");
                } else {
                    processCreate(parts);
                }
                break;
            case "deposit":
                if (parts.length != 3) {
                    System.out.println("Invalid deposit command. Usage: deposit <id> <amount>");
                } else {
                    processDeposit(parts);
                }
                break;
            default:
                System.out.println("Unknown action: " + action);
                break;
        }
    }

    public void processCreate(String[] parts) {
        // Correct assignment from command
        String accountType = parts[1].toLowerCase(); // Account type (e.g., "checking")
        String id = parts[2];                        // Account ID (e.g., "12345678")
        double apr;

        try {
            apr = Double.parseDouble(parts[3]);      // APR (e.g., 1.0)
        } catch (NumberFormatException e) {
            System.out.println("Invalid APR value: " + parts[3]);
            return;
        }

        // Check if account already exists using the Bank's method
        if (bank.accountExists(id)) {
            System.out.println("Account with ID " + id + " already exists - Skipping create command.");
            return;
        }

        // Proceed with creating the account if no duplicate is found
        Accounts account;
        switch (accountType) {
            case "checking":
                account = new Checking(apr, id);
                break;
            case "savings":
                account = new Savings(apr, id);
                break;
            case "cd":
                account = new CertificateOfDeposit(1, apr, id);
                break;
            default:
                System.out.println("Invalid account type: " + accountType);
                return;
        }

        // Add the new account to the bank
        bank.addAccount(id, account);
        System.out.println("Account created successfully: " + id);
    }

    public void processDeposit(String[] parts) {
        String id = parts[1];
        double amount;

        try {
            amount = Double.parseDouble(parts[2]); // Deposit amount
        } catch (NumberFormatException e) {
            System.out.println("Invalid deposit amount: " + parts[2]);
            return;
        }

        // Ensure the account exists before depositing
        if (!bank.accountExists(id)) {
            System.out.println("Account with ID " + id + " does not exist.");
            return;
        }

        // Add the deposit if the account exists
        bank.addDeposit(id, amount);
        System.out.println("Deposited " + amount + " to account " + id);
    }
}
