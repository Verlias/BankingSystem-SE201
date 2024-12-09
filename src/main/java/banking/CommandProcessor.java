package banking;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            return;
        }

        String action = parts[0].toLowerCase();

        switch (action) {
            case "create":
                processCreate(parts);
                break;
            case "deposit":
                processDeposit(parts);
                break;
            case "withdraw":
                processWithdraw(parts);
                break;
            case "passtime":
                processPassTime(parts);
                break;
            case "transfer":
                processTransfer(parts);
                break;
            default:
                System.out.println("Unknown action: " + action);
        }
    }

    private void processCreate(String[] parts) {
        if (parts.length < 4) {
            System.out.println("Invalid command format. Not enough arguments.");
            return;
        }

        String accountType = parts[1].toLowerCase();
        String id = parts[2];
        double apr = parseDouble(parts[3], "APR value", 0, 10);

        if (apr == -1 || bank.accountExists(id)) {
            return;
        }

        Accounts account = createAccount(accountType, id, apr, parts);
        if (account != null) {
            bank.addAccount(id, account);
            System.out.println("Account created successfully: " + id);
        }
    }

    private Accounts createAccount(String accountType, String id, double apr, String[] parts) {
        return switch (accountType) {
            case "checking" -> new Checking(apr, id);
            case "savings" -> new Savings(apr, id);
            case "cd" -> createCdAccount(parts, apr, id);
            default -> {
                System.out.println("Invalid account type: " + accountType);
                yield null;
            }
        };
    }

    private Accounts createCdAccount(String[] parts, double apr, String id) {
        if (parts.length < 5) {
            System.out.println("CD creation requires an initial balance.");
            return null;
        }

        double initialBalance = parseDouble(parts[4], "initial balance for CD", 1000, 10000);
        if (initialBalance == -1) {
            return null;
        }

        return new CertificateOfDeposit(initialBalance, apr, id);
    }

    private void processDeposit(String[] parts) {
        String id = parts[1];
        double amount = parseDouble(parts[2], "deposit amount", 0, Double.MAX_VALUE);

        if (amount == -1 || !validateAccount(id)) {
            return;
        }

        Accounts account = bank.getAccount().get(id);
        if (!canDeposit(account, amount)) {
            return;
        }

        bank.addDeposit(id, amount);
        System.out.println("Deposited " + amount + " to account " + id);
    }

    private boolean canDeposit(Accounts account, double amount) {
        switch (account.getClass().getSimpleName().toLowerCase()) {
            case "checking":
                if (amount > 1000) {
                    System.out.println("Deposit amount exceeds limit for Checking account. Max: $1000");
                    return false;
                }
                break;
            case "savings":
                if (amount > 2500) {
                    System.out.println("Deposit amount exceeds limit for Savings account. Max: $2500");
                    return false;
                }
                break;
            case "certificateofdeposit":
                System.out.println("Deposits are not allowed for CD accounts.");
                return false;
            default:
                return false;
        }
        return true;
    }

    private void processWithdraw(String[] parts) {
        String id = parts[1];
        double amount = parseDouble(parts[2], "withdrawal amount", 0, Double.MAX_VALUE);

        if (amount == -1 || !validateAccount(id)) {
            return;
        }

        Accounts account = bank.getAccount().get(id);
        if (!canWithdraw(account, amount)) {
            return;
        }

        account.withdraw(amount);
        System.out.println("Withdrew " + amount + " from account " + id);
    }

    private boolean canWithdraw(Accounts account, double amount) {
        switch (account.getClass().getSimpleName().toLowerCase()) {
            case "checking":
                if (amount > 400) {
                    System.out.println("Withdrawal amount exceeds limit for Checking account. Max: $400");
                    return false;
                }
                break;
            case "savings":
                Savings savingsAccount = (Savings) account;
                if (amount > 1000 || savingsAccount.hasExceededMonthlyWithdrawalLimit()) {
                    System.out.println("Exceeds withdrawal limit for Savings account.");
                    return false;
                }
                savingsAccount.incrementMonthlyWithdrawals();
                break;
            case "certificateofdeposit":
                CertificateOfDeposit cdAccount = (CertificateOfDeposit) account;
                if (!cdAccount.canWithdraw() || amount < cdAccount.getBalance()) {
                    System.out.println("Cannot withdraw from CD account.");
                    return false;
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private void processTransfer(String[] parts) {
        if (parts.length != 4) {
            System.out.println("Invalid transfer command. Usage: transfer <fromId> <toId> <amount>");
            return;
        }

        String fromId = parts[1];
        String toId = parts[2];
        double amount = parseDouble(parts[3], "transfer amount", 0, Double.MAX_VALUE);

        if (amount == -1 || !validateAccount(fromId) || !validateAccount(toId)) {
            return;
        }

        Accounts fromAccount = bank.getAccount().get(fromId);
        Accounts toAccount = bank.getAccount().get(toId);

        if (fromAccount instanceof CertificateOfDeposit || toAccount instanceof CertificateOfDeposit) {
            System.out.println("Error: CD accounts cannot be part of a transfer.");
            return;
        }

        if (fromAccount.getBalance() < amount) {
            System.out.println("Insufficient balance in account " + fromId + " for the transfer.");
            return;
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        System.out.println("Transferred " + amount + " from account " + fromId + " to account " + toId);
    }

    private boolean validateAccount(String id) {
        if (!bank.accountExists(id)) {
            System.out.println("Account with ID " + id + " does not exist.");
            return false;
        }
        return true;
    }

    private void processPassTime(String[] parts) {
        int monthsToPass = parseInt(parts[1]);
        if (monthsToPass == -1) return;

        currentDate = currentDate.plusMonths(monthsToPass);
        System.out.println("Time passed: " + monthsToPass + " months. Current date is now: " + currentDate);

        List<String> accountsToRemove = new ArrayList<>();
        for (Accounts account : bank.getAccount().values()) {
            processAccountTime(account, monthsToPass);
            if (account.getBalance() == 0) {
                System.out.println("Closing account: " + account.getId() + " due to zero balance.");
                accountsToRemove.add(account.getId());
            }
        }

        // Remove accounts after iteration
        for (String accountId : accountsToRemove) {
            bank.getAccount().remove(accountId);
        }
    }



    private void processAccountTime(Accounts account, int monthsToPass) {
        if (account.getBalance() == 0) {
            // Log the account being removed
            System.out.println("Closing account: " + account.getId() + " due to zero balance.");
            bank.getAccount().remove(account.getId());  // Ensure account is removed from the map
            return; // No further processing for this account
        }

        if (account.getBalance() < 100 && !(account instanceof CertificateOfDeposit)) {
            account.setBalance(account.getBalance() - 25);
            System.out.println("Deducted $25 from account: " + account.getId() + " due to low balance.");
        }

        if (account instanceof Savings || account instanceof Checking) {
            applyAPR(account, monthsToPass);
        } else if (account instanceof CertificateOfDeposit cdAccount) {
            cdAccount.passTime(monthsToPass);
            applyAPR(cdAccount, monthsToPass);
        }
    }



    private void applyAPR(Accounts account, int monthsToPass) {
        double aprDecimal = account.getApr() / 100.0;
        double monthlyAPR = aprDecimal / 12;
        double newBalance = account.getBalance();

        for (int i = 0; i < monthsToPass; i++) {
            newBalance *= (1 + monthlyAPR);  // Compound monthly APR
        }

        account.setBalance(newBalance);
        System.out.println("Applied APR to account: " + account.getId() + ". New balance: " + newBalance);
    }


    private double parseDouble(String value, String fieldName, double minValue, double maxValue) {
        try {
            double parsedValue = Double.parseDouble(value);
            if (parsedValue < minValue || parsedValue > maxValue) {
                System.out.println(fieldName + " must be between " + minValue + " and " + maxValue + ".");
                return -1;
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            System.out.println("Invalid " + fieldName + ". Please enter a valid number.");
            return -1;
        }
    }

    private int parseInt(String value) {
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue < 1 || parsedValue > 60) {
                System.out.println("months to pass" + " must be between " + 1 + " and " + 60 + ".");
                return -1;
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            System.out.println("Invalid " + "months to pass" + ". Please enter a valid integer.");
            return -1;
        }
    }
}
