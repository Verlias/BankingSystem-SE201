package banking;

public class CreateCommandValidator {
    private final Bank bank;

    public CreateCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean validate(String command) {
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

        if (bank.accountExists(accountId)) {
            return false;
        }

        return true;
    }
}
