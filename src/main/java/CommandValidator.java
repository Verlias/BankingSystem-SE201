public class CommandValidator {
    private final Bank bank;

    public CommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean validate(String command) {
        if (!command.startsWith("create")) {
            return false;
        }

        String[] parts = command.split("\\s+");

        if (parts.length != 4) {
            return false;
        }

        String accountType = parts[1].trim().toLowerCase();
        String accountId = parts[2].trim();
        String apr = parts[3].trim();

        System.out.println("Account Type: " + accountType);
        System.out.println("Account ID: " + accountId);
        System.out.println("APR: " + apr);

        if (!accountType.equals("cd") && !accountType.equals("checking") && !accountType.equals("saving")) {
            return false;
        }

        if (accountId.length() != 8 || !accountId.matches("\\d+")) {
            return false;
        }

        try {
            Double.parseDouble(apr);
        } catch (NumberFormatException e) {
            return false;
        }

        if (bank.accountExists(accountId)) {
            return false;
        }

        return true;
    }

}
