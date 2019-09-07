package cashmachine;

import cashmachine.exception.InterruptOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleHelper {
    private static BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));
    private static ResourceBundle res = ResourceBundle.getBundle(CashMachine.class.getPackage().getName() + ".resources.common_en");

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static Operation askOperation() throws InterruptOperationException {
        Operation operation;
        try {
            writeMessage(res.getString("choose.operation") +
                    "\n[1 - " + res.getString("operation.INFO") + ", 2 - " + res.getString("operation.DEPOSIT") +
                    ", 3 - " + res.getString("operation.WITHDRAW") + ", 4 - " + res.getString("operation.EXIT") + "]:");
            operation = Operation.getAllowableOperationByOrdinal(Integer.parseInt(readString()));
        } catch (IllegalArgumentException e) {
            writeMessage("An error occurred, try again.");
            return askOperation();
        }
        return operation;
    }

    public static String askCurrencyCode() throws InterruptOperationException {
        writeMessage(res.getString("choose.currency.code"));
        String code = readString();
        Pattern pattern = Pattern.compile("[a-zA-Z]{3}");
        Matcher matcher = pattern.matcher(code);
        if (matcher.matches())
            return code.toUpperCase();

        writeMessage("Incorrect code, please try again.");
        return askCurrencyCode();
    }

    public static String[] getValidTwoDigits(String currencyCode) throws InterruptOperationException {
        try {
            writeMessage(String.format(res.getString("choose.denomination.and.count.format"), currencyCode));
            String[] twoDigits = readString().split(" ");
            int nominal = Integer.parseInt(twoDigits[0]);
            int amount = Integer.parseInt(twoDigits[1]);
            if (nominal <= 0 || amount <= 0) {
                throw new NumberFormatException();
            }
            return new String[]{String.valueOf(nominal), String.valueOf(amount)};
        } catch (Exception e) {
            writeMessage("An error occurred, try again.");
            return getValidTwoDigits(currencyCode);
        }
    }

    public static String readString() throws InterruptOperationException {
        String string = "";
        try {
            string = bis.readLine();
            if (string.equalsIgnoreCase("EXIT"))
                throw new InterruptOperationException();
        } catch (IOException e) {

        }
        return string;
    }

    public static void printExitMessage() {
        ConsoleHelper.writeMessage(res.getString("the.end"));
    }
}
