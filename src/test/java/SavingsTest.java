import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SavingsTest {

    public static final String ACCOUNT_ID_1 = "89456185";

    Accounts savings;
    @BeforeEach
    void setUp() {
        savings = new Savings(10, ACCOUNT_ID_1);
    }

    @Test
    void savings_starts_with_zero_balance() {
        double actual = savings.getBalance();
        assertEquals(0, actual);
    }
}
