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

        if (balance < 0) {
            balance = 0;
        }
    }

    public String getId() {
        return id;
    }


    public void setApr(double apr) {
        if (apr < 0.0) {
            this.apr = 0.0; // Set to 0.0 if less than 0
        } else if (apr > 10.0) {
            this.apr = 10.0; // Set to 10.0 if greater than 10
        } else {
            this.apr = apr; // Set to valid APR
        }
    }

}
