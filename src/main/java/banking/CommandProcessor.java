package banking;

import banking.Bank;
import banking.CertificateOfDeposit;
import banking.Savings;
import java.time.LocalDate;

public class CommandProcessor {

    private final Bank bank;
    private LocalDate currentDate;

    public CommandProcessor(Bank bank) {
        this.bank = bank;
        this.currentDate = LocalDate.now(); // Initializing currentDate to today's date
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
            case "withdraw":
                if (parts.length != 3) {
                    System.out.println("Invalid withdraw command. Usage: withdraw <id> <amount>");
                } else {
                    processWithdraw(parts);
                }
                break;
            case "passtime":
                if (parts.length != 2) {
                    System.out.println("Invalid passtime command. Usage: passtime <months>");
                } else {
                    processPassTime(parts);
                }
                break;
            default:
                System.out.println("Unknown action: " + action);
                break;
        }
    }

    public void processPassTime(String[] parts) {
        int monthsToPass;

        try {
            monthsToPass = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid months value: " + parts[1]);
            return;
        }

        // Pass the specified number of months
        currentDate = currentDate.plusMonths(monthsToPass);
        System.out.println("Time passed: " + monthsToPass + " months. Current date is now: " + currentDate);

        // Reset monthly limits for savings accounts
        for (Accounts account : bank.getAccount().values()) {
            if (account instanceof Savings) {
                Savings savingsAccount = (Savings) account;
                savingsAccount.resetMonthlyWithdrawalLimit(); // Reset monthly withdrawals
            }
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
            case "banking.checking":
                account = new Checking(apr, id);
                break;
            case "banking.savings":
                account = new Savings(apr, id);
                break;
            case "cd":
                double initialCdBalance = 1000.0;
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
                    System.out.println("Deposit amount exceeds limit for banking.Checking account. Max: $1000");
                    return;
                }
                break;

            case "savings":
                if (amount > 2500) {
                    System.out.println("Deposit amount exceeds limit for banking.Savings account. Max: $2500");
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

    public void processWithdraw(String[] parts) {
        String id = parts[1];
        double amount;

        try {
            amount = Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid withdrawal amount: " + parts[2]);
            return;
        }

        if (amount < 0) {
            System.out.println("Cannot withdraw a negative amount.");
            return;
        }

        if (!bank.accountExists(id)) {
            System.out.println("Account with ID " + id + " does not exist.");
            return;
        }

        Accounts account = bank.getAccount().get(id);
        String accountType = account.getAccountType().toLowerCase();

        switch (accountType) {
            case "checking":
                if (amount > 400) {
                    System.out.println("Withdrawal amount exceeds limit for banking.Checking account. Max: $400");
                    return;
                }
                break;

            case "savings":
                Savings savingsAccount = (Savings) account;
                if (amount > 1000) {
                    System.out.println("Withdrawal amount exceeds limit for banking.Savings account. Max: $1000");
                    return;
                }
                if (savingsAccount.hasExceededMonthlyWithdrawalLimit()) {
                    System.out.println("Savings account withdrawal limit reached for this month.");
                    return;
                }
                savingsAccount.incrementMonthlyWithdrawals();
                break;

            case "certificateofdeposit":
                CertificateOfDeposit cdAccount = (CertificateOfDeposit) account;
                if (!cdAccount.isWithdrawalAllowed()) {
                    System.out.println("Cannot withdraw from CD account before 12 months have passed.");
                    return;
                }
                if (amount < cdAccount.getBalance()) {
                    System.out.println("Cannot partially withdraw from CD account. Must withdraw full balance.");
                    return;
                }
                amount = cdAccount.getBalance();
                break;

            default:
                System.out.println("Invalid account type for withdrawal.");
                return;
        }

        account.withdraw(amount);

        System.out.println("Withdrew " + amount + " from account " + id);
    }


}
