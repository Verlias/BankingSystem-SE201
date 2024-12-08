package banking;

import banking.Bank;
import banking.CertificateOfDeposit;
import banking.Savings;
import java.time.LocalDate;
import java.util.Iterator;

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
                // Allow both 4 and 5 parts for create commands
                if (parts.length != 4 && parts.length != 5) {
                    System.out.println("Invalid create command. Usage: create <accountType> <id> <apr> <initialBalance>");
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

        // Validate the number of months to pass
        try {
            monthsToPass = Integer.parseInt(parts[1]);
            if (monthsToPass <= 0 || monthsToPass > 60) {
                System.out.println("Invalid months value: " + monthsToPass + ". Must be between 1 and 60.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid months value: " + parts[1]);
            return;
        }

        // Simulate the passing of time by updating the current date
        currentDate = currentDate.plusMonths(monthsToPass);
        System.out.println("Time passed: " + monthsToPass + " months. Current date is now: " + currentDate);

        // Process all accounts for the time passed
        for (Iterator<Accounts> iterator = bank.getAccount().values().iterator(); iterator.hasNext(); ) {
            Accounts account = iterator.next();

            // If the balance is zero, close the account
            if (account.getBalance() == 0) {
                System.out.println("Closing account: " + account.getId() + " due to zero balance.");
                iterator.remove();  // Remove account from the bank
                continue;
            }

            // Deduct $25 if the balance is below $100 (not for CD accounts)
            if (account.getBalance() < 100 && !(account instanceof CertificateOfDeposit)) {
                account.setBalance(account.getBalance() - 25);
                System.out.println("Deducted $25 from account: " + account.getId() + " due to low balance. New balance: " + account.getBalance());
            }

            // Apply APR for Savings and Checking accounts
            if (account instanceof Savings || account instanceof Checking) {
                double aprDecimal = account.getApr() / 100.0;
                double monthlyAPR = aprDecimal / 12; // Monthly APR
                double newBalance = account.getBalance() * (1 + monthlyAPR);
                account.setBalance(newBalance);
                System.out.println("Applied APR to account: " + account.getId() + ". New balance: " + newBalance);
            }

            // For Certificate of Deposit (CD) accounts, pass time and apply APR
            else if (account instanceof CertificateOfDeposit) {
                CertificateOfDeposit cdAccount = (CertificateOfDeposit) account;

                // Call passTime method to simulate the passage of months
                cdAccount.passTime(monthsToPass); // Simulate the passage of time

                // Apply APR after the time has passed
                double aprDecimal = cdAccount.getApr() / 100.0;
                double monthlyAPR = aprDecimal / 12;  // Monthly APR
                double newBalance = cdAccount.getBalance();

                // Apply APR 4 times per month for CD (quarterly application)
                for (int month = 0; month < monthsToPass; month++) {
                    for (int i = 0; i < 4; i++) {
                        newBalance *= (1 + monthlyAPR);
                    }
                }

                cdAccount.setBalance(newBalance);
                System.out.println("Applied APR to CD account: " + account.getId() + ". New balance: " + cdAccount.getBalance());
            }
        }

        // Reset monthly withdrawal limits for all Savings accounts
        for (Accounts account : bank.getAccount().values()) {
            if (account instanceof Savings) {
                Savings savingsAccount = (Savings) account;
                savingsAccount.resetMonthlyWithdrawalLimit();
            }
        }
    }









    public void processCreate(String[] parts) {
        if (parts.length < 4) {
            System.out.println("Invalid command format. Not enough arguments.");
            return;
        }

        String accountType = parts[1].toLowerCase();
        String id = parts[2];
        double apr;

        try {
            apr = Double.parseDouble(parts[3]);
            if (apr < 0 || apr > 10) {
                System.out.println("Invalid APR value. Must be between 0 and 10.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid APR value: " + parts[3]);
            return;
        }

        // Check for duplicate account ID
        if (bank.accountExists(id)) {
            System.out.println("Account with ID " + id + " already exists - Skipping create command.");
            return;
        }

        // Create the account
        Accounts account;
        switch (accountType) {
            case "banking.checking":
                account = new Checking(apr, id);
                break;

            case "banking.savings":
                account = new Savings(apr, id);
                break;

            case "cd":
                if (parts.length < 5) {
                    System.out.println("CD creation requires an initial balance.");
                    return;
                }

                double initialCdBalance;
                try {
                    initialCdBalance = Double.parseDouble(parts[4]);
                    if (initialCdBalance < 1000 || initialCdBalance > 10000) {
                        System.out.println("Invalid initial balance for CD. Must be between $1000 and $10000.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid initial balance value: " + parts[4]);
                    return;
                }

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
                    System.out.println("Withdrawal amount exceeds limit for Checking account. Max: $400");
                    return;
                }
                break;

            case "savings":
                Savings savingsAccount = (Savings) account;
                if (amount > 1000) {
                    System.out.println("Withdrawal amount exceeds limit for Savings account. Max: $1000");
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

                // Ensure withdrawal is allowed only if the lock period is over
                if (!cdAccount.canWithdraw()) {
                    System.out.println("Cannot withdraw from CD account. Lock period has not passed or balance is insufficient.");
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
