public class CommandValidator {

    public boolean validate(String command) {
        if (!command.startsWith("create")) {
            return false;
        }

        String[] parts = command.split("\\s+");

        if (parts.length != 4) {
            return false;
        }

        String accountId = parts[1];
        if (accountId.length() != 8 || !accountId.matches("\\d+")) {
            return false;
        }

        String accountType = parts[2];
        if (!accountType.equals("CD") && !accountType.equals("Checking") && !accountType.equals("Saving")) {
            return false;
        }

        String apr = parts[3];
        try {
            Double.parseDouble(apr);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
