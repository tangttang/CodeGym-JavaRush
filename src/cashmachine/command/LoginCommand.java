package cashmachine.command;

import cashmachine.CashMachine;
import cashmachine.ConsoleHelper;
import cashmachine.exception.InterruptOperationException;

import java.util.Locale;
import java.util.ResourceBundle;

import static cashmachine.ConsoleHelper.writeMessage;

public class LoginCommand implements Command {
    private ResourceBundle validCreditCards = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "verifiedCards", Locale.ENGLISH);
    private ResourceBundle res = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "login_en", Locale.ENGLISH);

    @Override
    public void execute() throws InterruptOperationException {
        try {
            writeMessage(res.getString("before"));
            writeMessage(res.getString("specify.data"));
            writeMessage("Enter your credit card number: ");
            String cardNumber = ConsoleHelper.readString();
            writeMessage("Enter your pin-code: ");
            String pin = ConsoleHelper.readString();
            if (!validCreditCards.containsKey(cardNumber) || !validCreditCards.getString(cardNumber).equals(pin)) {
                writeMessage(String.format(res.getString("not.verified.format"), cardNumber));
                writeMessage(res.getString("try.again.with.details"));
                throw new NumberFormatException();
            } else {
                writeMessage(String.format(res.getString("success.format"), cardNumber));
            }

        } catch (NumberFormatException e) {
            writeMessage(res.getString("try.again.or.exit"));
            execute();
        }
    }
}
