package banking;

public class Savings extends Accounts {

    public Savings(double apr, String id) {
        super(0, 0, id);
        setApr(apr);
    }

    @Override
    public String getAccountType() {
        return "banking.saving";
    }

}
