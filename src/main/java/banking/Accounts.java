package banking;

public abstract class Accounts {

    private double apr;
    private double balance;
    private final String id;

    public Accounts(double balance, double apr, String id) {
        this.balance = balance;
        this.apr = apr;
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public double getApr() {
        return apr;
    }

    public void addDeposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;

        // Ensure balance doesn't go below 0
        if (balance < 0) {
            balance = 0;
        }
    }

    public String getId() {
        return id;
    }

    public void setApr(double apr) {
        if (apr < 0.0) {
            this.apr = 0.0;
        } else if (apr > 10.0) {
            this.apr = 10.0;
        } else {
            this.apr = apr;
        }
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public abstract String getAccountType();

    public void resetMonthlyWithdrawalLimit() {

    }
}
