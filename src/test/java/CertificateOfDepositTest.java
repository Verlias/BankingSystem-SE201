import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CertificateOfDepositTest {

    public static final String ACCOUNT_ID_1 = "89456185";
    Accounts certificateOfDeposit;

    @BeforeEach
    void setUp() {
        certificateOfDeposit = new CertificateOfDeposit(2000.54, 5.5, ACCOUNT_ID_1);
    }

    @Test
    void certificate_of_deposit_can_be_created_with_supplied_balance() {
        double actual = certificateOfDeposit.getBalance();
        assertEquals(2000.54, actual);
    }

}