import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankTest {

    public static final String ACCOUNT_ID_1 = "89456185";
    private static final String ACCOUNT_ID_2 = "00000001";

    Bank bank;
    Accounts Checking;
    Accounts CertificateOfDeposit;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        Checking = new Checking(7);
        CertificateOfDeposit = new CertificateOfDeposit(2000, 5.5);
    }

    @Test
    void bank_is_initially_created_with_no_accounts() {
        int actual = bank.getNumberOfAccounts();
        assertEquals(0, actual);
    }

    @Test
    void bank_has_one_account_in_the_bank_when_one_account_is_added() {
        bank.addAccount(ACCOUNT_ID_1, Checking);
        int actual = bank.getNumberOfAccounts();
        assertEquals(1, actual);
    }

    @Test
    void bank_has_two_account_in_the_bank_when_two_account_is_added() {
        bank.addAccount(ACCOUNT_ID_1, Checking);
        bank.addAccount(ACCOUNT_ID_2, CertificateOfDeposit);
        int actual = bank.getNumberOfAccounts();
        assertEquals(2, actual);
    }

    @Test
    void retrieve_one_account_from_bank_and_is_correct_account() {
        bank.addAccount(ACCOUNT_ID_1, Checking);
        Accounts actual = bank.getAccount().get(ACCOUNT_ID_1);
        assertEquals(Checking, actual);
    }

    @Test
    void deposit_money_through_id_and_is_correct_account_and_balance() {
        bank.addAccount(ACCOUNT_ID_1, Checking);
        bank.addDeposit(ACCOUNT_ID_1, 100);
        double actual = bank.getAccount().get(ACCOUNT_ID_1).getBalance();
        assertEquals(100, actual);
    }

    @Test
    void withdraw_money_through_id_and_is_correct_account_and_balance() {
        bank.addAccount(ACCOUNT_ID_2, CertificateOfDeposit);
        bank.withdraw(ACCOUNT_ID_2, 100);
        double actual = CertificateOfDeposit.getBalance();
        assertEquals(1900, actual);
    }

    @Test
    void deposit_twice_through_the_bank_works_as_expected() {
        bank.addAccount(ACCOUNT_ID_1, Checking);
        bank.addDeposit(ACCOUNT_ID_1, 100);
        bank.addDeposit(ACCOUNT_ID_1, 100);
        double actual = bank.getAccount().get(ACCOUNT_ID_1).getBalance();
        assertEquals(200, actual);
    }

    @Test
    void withdraw_twice_through_the_bank_works_as_expected() {
        bank.addAccount(ACCOUNT_ID_2, CertificateOfDeposit);
        bank.withdraw(ACCOUNT_ID_2, 100);
        bank.withdraw(ACCOUNT_ID_2, 100);
        double actual = CertificateOfDeposit.getBalance();
        assertEquals(1800, actual);
    }
}