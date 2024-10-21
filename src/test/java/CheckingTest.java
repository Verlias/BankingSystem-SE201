import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckingTest {

    public static final String ACCOUNT_ID_1 = "89456185";
    Accounts checking;
    @BeforeEach
    void setUp() {
        checking = new Checking(0.3, ACCOUNT_ID_1);
    }

    @Test
    void checking_starts_with_zero_balance() {
        double actual = checking.getBalance();
        assertEquals(0, actual);
    }
}
