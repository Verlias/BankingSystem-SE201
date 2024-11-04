import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {

    public static final String ACCOUNT_ID_1 = "89456185";
    private static final String ACCOUNT_ID_2 = "50002001";
    Accounts savings;
    Accounts checking;
    Accounts certificateOfDeposit;

    @BeforeEach
    void setUp() {
        checking = new Checking(0.3, ACCOUNT_ID_1);
        savings = new Savings(10, ACCOUNT_ID_2);
        certificateOfDeposit = new CertificateOfDeposit(2000.54, 5.5, ACCOUNT_ID_2);
    }


    @Test
    void checking_account_sets_valid_apr() {
        checking = new Checking(5.0, ACCOUNT_ID_1);
        double actual = checking.getApr();
        assertEquals(5.0, actual);
    }

    @Test
    void savings_account_sets_valid_apr() {
        savings = new Savings(3.50, ACCOUNT_ID_1);
        double actual = savings.getApr();
        assertEquals(3.5, actual);
    }

    @Test
    void cd_account_sets_valid_apr() {
        certificateOfDeposit = new CertificateOfDeposit(1000.0, 2.5, ACCOUNT_ID_1);
        double actual = certificateOfDeposit.getApr();
        assertEquals(2.5, actual);
    }

    @Test
    void apr_at_zero_is_accepted() {
        checking = new Checking(0.0, ACCOUNT_ID_1);
        double actual = checking.getApr();
        assertEquals(0.0, actual);
    }

    @Test
    void apr_at_upper_limit_is_accepted() {
        checking = new Checking(10.0, ACCOUNT_ID_1);
        double actual = checking.getApr();
        assertEquals(10.0, actual);
    }

    @Test
    void apr_above_ten_defaults_to_max() {
        checking = new Checking(12.0, ACCOUNT_ID_1);
        double actual = checking.getApr();
        assertFalse(actual > 10.0, "APR should not exceed 10.0");
    }

    @Test
    void negative_apr_defaults_to_zero() {
        checking = new Checking(-1.0, ACCOUNT_ID_1);
        double actual = checking.getApr();
        assertFalse(actual < 0.0, "APR should not be negative");
    }

    @Test
    void apr_just_below_zero_defaults_to_zero() {
        checking = new Checking(-0.01, ACCOUNT_ID_1);
        double actual = checking.getApr();
        assertFalse(actual < 0.0, "APR should not be negative");
    }

    @Test
    void apr_just_above_ten_defaults_to_max() {
        checking = new Checking(10.01, ACCOUNT_ID_1);
        double actual = checking.getApr();
        assertFalse(actual > 10.0, "APR should not exceed 10.0");
    }



    // Checking Account Tests
    @Test
    void checking_has_supplied_apr() {
        double actual = checking.getApr();
        assertEquals(0.3, actual);
    }

    @Test
    void checking_balance_increases_by_deposit_amount() {
        checking.addDeposit(10);
        double actual = checking.getBalance();
        assertEquals(10, actual);
    }

    @Test
    void checking_balance_decreases_by_withdrawn_amount() {
        checking.addDeposit(10);
        checking.withdraw(5);
        double actual = checking.getBalance();
        assertEquals(5, actual);
    }

    @Test
    void checking_balance_cannot_go_below_zero_when_withdrawing() {
        checking.withdraw(10);
        double actual = checking.getBalance();
        assertEquals(0, actual);
    }

    @Test
    void checking_allows_multiple_deposits() {
        checking.addDeposit(10);
        checking.addDeposit(15);
        double actual = checking.getBalance();
        assertEquals(25, actual);
    }

    @Test
    void checking_allows_multiple_withdrawals() {
        checking.addDeposit(20);
        checking.withdraw(5);
        checking.withdraw(5);
        double actual = checking.getBalance();
        assertEquals(10, actual);
    }

    // Savings Account Tests
    @Test
    void savings_has_supplied_apr() {
        double actual = savings.getApr();
        assertEquals(10, actual);
    }

    @Test
    void savings_balance_increases_by_deposit_amount() {
        savings.addDeposit(10);
        double actual = savings.getBalance();
        assertEquals(10, actual);
    }

    @Test
    void savings_balance_decreases_by_withdrawn_amount() {
        savings.addDeposit(10);
        savings.withdraw(5);
        double actual = savings.getBalance();
        assertEquals(5, actual);
    }

    @Test
    void savings_balance_cannot_go_below_zero_when_withdrawing() {
        savings.withdraw(20);
        double actual = savings.getBalance();
        assertEquals(0, actual);
    }

    @Test
    void savings_allows_multiple_deposits() {
        savings.addDeposit(10);
        savings.addDeposit(15);
        double actual = savings.getBalance();
        assertEquals(25, actual);
    }

    @Test
    void savings_allows_multiple_withdrawals() {
        savings.addDeposit(15);
        savings.withdraw(5);
        savings.withdraw(5);
        double actual = savings.getBalance();
        assertEquals(5, actual);
    }

    // Certificate of Deposit (CD) Account Tests
    @Test
    void certificate_of_deposit_has_supplied_apr() {
        double actual = certificateOfDeposit.getApr();
        assertEquals(5.5, actual);
    }

    @Test
    void certificate_of_deposit_balance_increases_by_deposit_amount() {
        certificateOfDeposit.addDeposit(10);
        double actual = certificateOfDeposit.getBalance();
        assertEquals(2010.54, actual);
    }

    @Test
    void certificate_of_deposit_balance_decreases_by_withdrawn_amount() {
        certificateOfDeposit.withdraw(10);
        double actual = certificateOfDeposit.getBalance();
        assertEquals(1990.54, actual);
    }

    @Test
    void certificate_of_deposit_balance_cannot_go_below_zero_when_withdrawing() {
        certificateOfDeposit.withdraw(3000);
        double actual = certificateOfDeposit.getBalance();
        assertEquals(0, actual);
    }

    @Test
    void certificate_of_deposit_allows_multiple_deposits() {
        certificateOfDeposit.addDeposit(10);
        certificateOfDeposit.addDeposit(15);
        double actual = certificateOfDeposit.getBalance();
        assertEquals(2025.54, actual);
    }

    @Test
    void certificate_of_deposit_allows_multiple_withdrawals() {
        certificateOfDeposit.withdraw(10);
        certificateOfDeposit.withdraw(15);
        double actual = certificateOfDeposit.getBalance();
        assertEquals(1975.54, actual);
    }
}
