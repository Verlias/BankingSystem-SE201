package banking;

public class Checking extends Accounts {
	public Checking(double apr, String id) {
		super(0, apr, id);
	}

	@Override
	public String getAccountType() {
		return "checking";
	}
}
