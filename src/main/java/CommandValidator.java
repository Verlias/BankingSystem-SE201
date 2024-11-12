public class CommandValidator {
    private final Bank bank;

    public CommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean validate(String command) {
        if (!command.startsWith("create")) {
            return false;
        }

        String[] parts = command.trim().split("\\s+");

        if (parts.length != 4) {
            return false;
        }

        String accountType = parts[1].trim().toLowerCase();
        String accountId = parts[2].trim();
        String aprString = parts[3].trim();

        if (!accountType.equals("cd") && !accountType.equals("checking") && !accountType.equals("saving")) {
            return false;
        }

        if (accountId.length() != 8 || !accountId.matches("\\d+")) {
            return false;
        }

        double apr;
        try {
            apr = Double.parseDouble(aprString);
            if (apr < 0.0 || apr > 10.0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        // Ensure account does not already exist
        if (bank.accountExists(accountId)) {
            return false;
        }

        // All checks passed
        return true;
    }
}
