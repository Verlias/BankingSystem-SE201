package banking;

import java.time.LocalDate;

public class CertificateOfDeposit extends Accounts {
    private static final int LOCK_PERIOD_MONTHS = 12;
    private LocalDate startDate;
    private LocalDate currentDate; // To simulate the passage of time

    public CertificateOfDeposit(double balance, double apr, String id) {
        super(balance, apr, id);
        this.startDate = LocalDate.now(); // Set start date as current date
        this.currentDate = LocalDate.now(); // Initialize simulated current date
    }

    @Override
    public String getAccountType() {
        return "certificateofdeposit";
    }

    // Method to simulate passing of time
    public void passTime(int months) {
        currentDate = currentDate.plusMonths(months);
    }

    public boolean canWithdraw() {
        LocalDate lockEndDate = startDate.plusMonths(LOCK_PERIOD_MONTHS);
        return !currentDate.isBefore(lockEndDate); // Withdraw is allowed after lock period ends
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    // Optional: Method to get lock end date for testing purposes
    public LocalDate getLockEndDate() {
        return startDate.plusMonths(LOCK_PERIOD_MONTHS);
    }
}
