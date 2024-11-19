package banking;

import java.util.Arrays;

public class DepositCommandValidator {
    private final Bank bank;

    public DepositCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean validate(String command) {
        String[] parts = command.trim().split("\\s+");

        if (parts.length != 3) {
            System.out.println("Invalid command length");
            return false;
        }

        String keyword = parts[0];
        String accountId = parts[1];
        String amountString = parts[2];

        System.out.println("Amount String: " + amountString);  // Add this for debugging

        if (!keyword.equalsIgnoreCase("deposit")) {
            return false;
        }

        if (!bank.accountExists(accountId)) {
            return false;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountString);
            if (amount <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }


}
