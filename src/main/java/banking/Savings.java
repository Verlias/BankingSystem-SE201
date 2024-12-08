package banking;

public class Savings extends Accounts {
    private int monthlyWithdrawals;
    private static final int MONTHLY_LIMIT = 3;

    public Savings(double apr, String id) {
        super(0.0, apr, id);
        this.monthlyWithdrawals = 0;
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

    public void resetMonthlyWithdrawalLimit() {
        monthlyWithdrawals = 0;
    }

    @Override
    public void withdraw(double amount) {
        if (hasExceededMonthlyWithdrawalLimit()) {
            System.out.println("Monthly withdrawal limit reached for savings account.");
            return;
        }
        super.withdraw(amount);
        incrementMonthlyWithdrawals();
    }
}
