package banking;

public class Checking extends Accounts {
    public Checking(double apr, String id) {
        super(0, 0, id);
        setApr(apr);
    }
    @Override
    public String getAccountType() {
        return "banking.checking";
    }
}
