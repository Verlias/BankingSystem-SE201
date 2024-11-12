public class CommandProcessor {

    private final Bank bank;

    public CommandProcessor(Bank bank) {
        this.bank = bank;
    }

    public void process(String command) {
        String[] parts = command.trim().split("\\s+");

        if (parts.length < 2) {
            System.out.println("Invalid command format.");
            return;
        }

        String action = parts[0].toLowerCase();

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
        String accountType = parts[1].toLowerCase();
        String id = parts[2];
        double apr;

        try {
            apr = Double.parseDouble(parts[3]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid APR value: " + parts[3]);
            return;
        }

        if (bank.accountExists(id)) {
            System.out.println("Account with ID " + id + " already exists - Skipping create command.");
            return;
        }

        Accounts account;
        switch (accountType) {
            case "checking":
                account = new Checking(apr, id);
                break;
            case "savings":
                account = new Savings(apr, id);
                break;
            case "cd":
                double initialCdBalance = 1000.0; // Example initial balance for a CD account
                account = new CertificateOfDeposit(initialCdBalance, apr, id);
                break;
            default:
                System.out.println("Invalid account type: " + accountType);
                return;
        }

        bank.addAccount(id, account);
        System.out.println("Account created successfully: " + id);
    }


    public void processDeposit(String[] parts) {
        String id = parts[1];
        double amount;

        try {
            amount = Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid deposit amount: " + parts[2]);
            return;
        }

        if (!bank.accountExists(id)) {
            System.out.println("Account with ID " + id + " does not exist.");
            return;
        }

        Accounts account = bank.getAccount().get(id);
        String accountType = account.getClass().getSimpleName().toLowerCase();

        switch (accountType) {
            case "checking":
                if (amount > 1000) {
                    System.out.println("Deposit amount exceeds limit for Checking account. Max: $1000");
                    return;
                }
                break;

            case "savings":
                if (amount > 2500) {
                    System.out.println("Deposit amount exceeds limit for Savings account. Max: $2500");
                    return;
                }
                break;

            case "certificateofdeposit":
                System.out.println("Deposits are not allowed for CD accounts.");
                return;
        }

        bank.addDeposit(id, amount);
        System.out.println("Deposited " + amount + " to account " + id);
    }

}
