import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {
    Accounts savings;
    Accounts checking;
    Accounts certificateOfDeposit;

    @BeforeEach
    void setUp() {
        checking = new Checking(0.3);
        savings = new Savings(10);
        certificateOfDeposit = new CertificateOfDeposit(2000.54, 5.5);
    }

    // Checking Account Tests
    @Test
    void checking_starts_with_zero_balance() {
        double actual = checking.getBalance();
        assertEquals(0, actual);
    }

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
    void savings_starts_with_zero_balance() {
        double actual = savings.getBalance();
        assertEquals(0, actual);
    }

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
    void certificate_of_deposit_can_be_created_with_supplied_balance() {
        double actual = certificateOfDeposit.getBalance();
        assertEquals(2000.54, actual);
    }

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
