package banking;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.time.temporal.ChronoUnit;


public class WithdrawCommandValidator {
    private final Bank bank;

    public WithdrawCommandValidator(Bank bank) {
        this.bank = bank;
    }

    public boolean validate(String command) {
        if (command == null) {
            return false; // Null commands are invalid
        }
        String[] parts = command.trim().split("\\s+");

        if (parts.length != 3) {
            return false; // Invalid command format
        }

        String accountId = parts[1];
        double amount;

        try {
            amount = Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            return false; // Invalid amount (not a valid number)
        }

        if (amount <= 0) {
            return false; // Invalid withdrawal amount (should be positive)
        }

        // Check for precision limit (two decimal places)
        if (amount * 100 != Math.floor(amount * 100)) {
            return false; // Reject if the amount has more than two decimal places
        }

        // Retrieve account by ID from the bank
        Accounts account = bank.getAccount().get(accountId);
        if (account == null) {
            return false; // Account does not exist
        }

        // Check withdrawal rules for each account type
        if (account instanceof Checking) {
            return validateCheckingAccount((Checking) account, amount);
        } else if (account instanceof Savings) {
            return validateSavingsAccount((Savings) account, amount);
        } else if (account instanceof CertificateOfDeposit) {
            return validateCDAccount((CertificateOfDeposit) account, amount);
        } else {
            return false; // Unsupported account type for withdrawal
        }
    }


    private boolean validateCheckingAccount(Checking account, double amount) {
        if (amount > 400) {
            return false;  // Checking account withdrawal limit
        }
        if (account.getBalance() < amount) {
            return false;  // Insufficient funds
        }
        account.setBalance(account.getBalance() - amount);  // Deduct the amount
        return true;
    }

    private boolean validateSavingsAccount(Savings account, double amount) {
        if (amount > 1000) {
            return false;  // Savings account withdrawal limit
        }
        if (account.getBalance() < amount) {
            return false;  // Insufficient funds
        }
        if (account.getLastWithdrawalDate() != null
                && isWithinSameMonth(account.getLastWithdrawalDate())) {
            return false;  // Savings account can only have one withdrawal per month
        }
        account.setBalance(account.getBalance() - amount);  // Deduct the amount
        account.setLastWithdrawalDate(LocalDate.now());  // Update the withdrawal date
        return true;
    }

    private boolean validateCDAccount(CertificateOfDeposit account, double amount) {
        Date currentDate = new Date();
        long monthsSinceCreation = monthsBetween(account.getCreationDate(), currentDate);
        if (monthsSinceCreation < 12) {
            return false;  // Cannot withdraw before 12 months
        }
        if (amount > account.getBalance()) {
            amount = account.getBalance();  // Withdraw the full balance if amount exceeds balance
        }
        if (amount == account.getBalance()) {
            account.setBalance(0);  // Deduct full balance for CD withdrawal
            return true;
        }
        return false;  // CD account only allows full withdrawal
    }

    private boolean isWithinSameMonth(LocalDate lastWithdrawalDate) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();  // Use LocalDate to avoid using Date

        // Compare the month and year of lastWithdrawalDate with the current date
        return lastWithdrawalDate.getMonthValue() == currentDate.getMonthValue()
                && lastWithdrawalDate.getYear() == currentDate.getYear();
    }

    private long monthsBetween(Date startDate, Date endDate) {
        LocalDate startLocalDate = toLocalDate(startDate);
        LocalDate endLocalDate = toLocalDate(endDate);
        return ChronoUnit.MONTHS.between(startLocalDate, endLocalDate);
    }

    private LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
