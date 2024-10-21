public abstract class Accounts {

    private final double apr;
    private double balance;

    public Accounts(double balance, double apr) {
        this.balance = balance;
        this.apr = apr;
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

        if (balance < 0) {
            balance = 0;
        }
    }

}
