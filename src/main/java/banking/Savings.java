package banking;

import java.time.LocalDate;

public class Savings extends Accounts {
	private static final int MONTHLY_LIMIT = 3;
	private int monthlyWithdrawals;
	private LocalDate lastWithdrawalDate; // To store the date of the last withdrawal

	public Savings(double apr, String id) {
		super(0.0, apr, id);
		this.monthlyWithdrawals = 0;
		this.lastWithdrawalDate = null; // No withdrawal made yet
	}

	@Override
	public String getAccountType() {
		return "savings";
	}

	public boolean hasExceededMonthlyWithdrawalLimit() {
		return monthlyWithdrawals >= MONTHLY_LIMIT;
	}

	public void incrementMonthlyWithdrawals() {
		monthlyWithdrawals++;
	}

	@Override
	public void withdraw(double amount) {
		if (hasExceededMonthlyWithdrawalLimit()) {
			System.out.println("Monthly withdrawal limit reached for savings account.");
			return;
		}
		super.withdraw(amount);
		incrementMonthlyWithdrawals();
		lastWithdrawalDate = LocalDate.now(); // Set the last withdrawal date
	}

	public LocalDate getLastWithdrawalDate() {
		return lastWithdrawalDate;
	}

	public void setLastWithdrawalDate(LocalDate lastWithdrawalDate) {
		this.lastWithdrawalDate = lastWithdrawalDate;
	}
}
