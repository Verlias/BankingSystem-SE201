package banking;

public class PassTimeCommandValidator {

    public boolean validate(String command) {
        // Command format validation: "passtime <months>"
        String[] parts = command.trim().split("\\s+"); // Split by one or more spaces

        if (parts.length != 2) {
            System.out.println("Invalid command format. Correct format: passtime <months>");
            return false;
        }

        try {
            int monthsToPass = Integer.parseInt(parts[1]);

            // Ensure monthsToPass is between 1 and 60
            if (monthsToPass <= 0 || monthsToPass > 60) {
                System.out.println("Invalid months value: " + monthsToPass + ". Must be between 1 and 60.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid months value: " + parts[1] + ". Must be a valid integer.");
            return false;
        }

        return true;  // Command is valid
    }
}
