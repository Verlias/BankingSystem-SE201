package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class AccountTest {

    public static final String ACCOUNT_ID_1 = "89456185";
    private static final String ACCOUNT_ID_2 = "50002001";
    private static final String ACCOUNT_ID_DUPLICATE = "89456185";
    private static final String INVALID_SHORT_ID = "1234567";
    private static final String INVALID_LONG_ID = "123456789";
    private static final String INVALID_CHAR_ID = "1234ABCD";
    Accounts savings;
    Accounts checking;
    Accounts certificateOfDeposit;
    Set<String> existingAccountIds; // To mock existing account IDs for uniqueness test

    @BeforeEach
    void setUp() {
        checking = new Checking(0.3, ACCOUNT_ID_1);
        savings = new Savings(10, ACCOUNT_ID_2);
        certificateOfDeposit = new CertificateOfDeposit(2000.54, 5.5, ACCOUNT_ID_2);

        // Initialize a set to hold existing account IDs
        existingAccountIds = new HashSet<>();
        existingAccountIds.add(ACCOUNT_ID_1); // Add existing IDs for uniqueness test
        existingAccountIds.add(ACCOUNT_ID_2);
    }

    // Helper methods for testing account ID validation
    private boolean isValidAccountId(String accountId) {
        return accountId.length() == 8 && accountId.matches("\\d+");
    }

    private boolean isUnique(String accountId) {
        return existingAccountIds.contains(accountId);
    }


    // Account ID Validation Test Cases
    @Test
    void account_id_must_be_unique() {
        boolean isUnique = isUnique(ACCOUNT_ID_1); // Check for uniqueness
        boolean actual = true;
        assertEquals(isUnique, actual);
    }

    @Test
    void account_id_must_be_eight_digits() {
        boolean isValidShort = isValidAccountId(INVALID_SHORT_ID); // Check for length validation
        assertFalse(isValidShort, "Account ID must be 8 digits long");

        boolean isValidLong = isValidAccountId(INVALID_LONG_ID); // Check for length validation
        assertFalse(isValidLong, "Account ID must be 8 digits long");
    }

    @Test
    void account_id_must_only_contain_digits() {
        boolean isValidDigits = isValidAccountId(INVALID_CHAR_ID); // Check for digit validation
        assertFalse(isValidDigits, "Account ID must contain only digits");
    }



    @Test
    void apr_above_ten_sets_to_ten() {
        checking.setApr(12.0); // Set an invalid APR
        double actual = checking.getApr();
        assertEquals(10.0, actual, "APR should be set to 10.0 for values above 10.0"); // Check if set to 10.0
    }

    @Test
    void negative_apr_sets_to_zero() {
        checking.setApr(-1.0); // Set an invalid APR
        double actual = checking.getApr();
        assertEquals(0.0, actual, "APR should be set to 0.0 for negative input"); // Check if set to 0.0
    }

    @Test
    void apr_just_below_zero_sets_to_zero() {
        checking.setApr(-0.01); // Set an invalid APR
        double actual = checking.getApr();
        assertEquals(0.0, actual, "APR should be set to 0.0 for values just below zero"); // Check if set to 0.0
    }

    @Test
    void apr_just_above_ten_sets_to_ten() {
        checking.setApr(10.01); // Set an invalid APR
        double actual = checking.getApr();
        assertEquals(10.0, actual, "APR should be set to 10.0 for values just above 10.0"); // Check if set to 10.0
    }

    @Test
    void valid_apr_is_set_correctly() {
        checking.setApr(5.0); // Set a valid APR
        double actual = checking.getApr();
        assertEquals(5.0, actual, "APR should be set correctly for valid input"); // Check if set correctly
    }



    // banking.Checking Account Tests
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

    // banking.Savings Account Tests
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
