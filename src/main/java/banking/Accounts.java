package banking;

public abstract class Accounts {

	private final String id;
	private double apr;
	private double balance;

	public Accounts(double balance, double apr, String id) {
		this.balance = balance;
		this.apr = apr;
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getApr() {
		return apr;
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

	public abstract String getAccountType();

}
