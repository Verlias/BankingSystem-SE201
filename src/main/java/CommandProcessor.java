public class CommandProcessor {

    private final Bank bank;

    public CommandProcessor(Bank bank) {
        this.bank = bank;
    }

    public void process(String command) {
        String[] parts = command.split("\\s+");
        String action = parts[0];

        if (action.equals("create")) {
            processCreate(parts);
        } else if (action.equals("deposit")) {
            processDeposit(parts);
        }
    }

    public void processCreate(String[] parts) {
        String id = parts[1];
        String accountType = parts[2];
        double apr = Double.parseDouble(parts[3]);

        Accounts account;
        switch (accountType) {
            case "Checking":
                // Fix: Checking constructor only requires apr and id
                account = new Checking(apr, id);
                break;
            case "Savings":
                // Fix: Savings constructor expects apr and id
                account = new Savings(apr, id);
                break;
            case "CD":
                // Fix: CertificateOfDeposit constructor expects apr and id
                account = new CertificateOfDeposit(1,apr, id);
                break;
            default:
                throw new IllegalArgumentException("Invalid account type");
        }

        bank.addAccount(id, account);
    }

    public void processDeposit(String[] parts) {
        String id = parts[1];
        double amount = Double.parseDouble(parts[2]);
        bank.addDeposit(id, amount);
    }
}
