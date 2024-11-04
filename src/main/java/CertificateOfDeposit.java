public class CertificateOfDeposit extends Accounts {

    public CertificateOfDeposit(double balance, double apr, String id) {
        super(balance, 0, id);
        setApr(apr);
    }

}
