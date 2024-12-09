package banking;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CertificateOfDeposit extends Accounts {
	private static final int LOCK_PERIOD_MONTHS = 12;
	private LocalDate startDate;
	private LocalDate currentDate;

	public CertificateOfDeposit(double balance, double apr, String id) {
		super(balance, apr, id);
		this.startDate = LocalDate.now();
		this.currentDate = LocalDate.now();
	}

	@Override
	public String getAccountType() {
		return "certificateofdeposit";
	}

	public void passTime(int months) {
		currentDate = currentDate.plusMonths(months);
	}

	public boolean canWithdraw() {
		LocalDate lockEndDate = startDate.plusMonths(LOCK_PERIOD_MONTHS);
		return !currentDate.isBefore(lockEndDate);
	}

	public Date getCreationDate() {
		return Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

}
