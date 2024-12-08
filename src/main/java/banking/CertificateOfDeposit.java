package banking;

import java.time.LocalDate;

public class CertificateOfDeposit extends Accounts {
    private static final double MINIMUM_BALANCE = 1000.0;
    private static final int LOCK_PERIOD_MONTHS = 12;
    private LocalDate startDate;

    public CertificateOfDeposit(double balance, double apr, String id) {
        super(balance, apr, id);
        this.startDate = LocalDate.now();
    }

    @Override
    public String getAccountType() {
        return "certificateofdeposit";
    }


    private boolean canWithdraw() {
        return startDate.plusMonths(LOCK_PERIOD_MONTHS).isBefore(LocalDate.now());
    }


    public boolean isWithdrawalAllowed() {
        return getBalance() >= MINIMUM_BALANCE;  // Checks the balance for withdrawal eligibility
    }

}
