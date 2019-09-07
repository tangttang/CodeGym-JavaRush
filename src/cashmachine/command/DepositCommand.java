package cashmachine.command;

import cashmachine.CashMachine;
import cashmachine.ConsoleHelper;
import cashmachine.CurrencyManipulator;
import cashmachine.CurrencyManipulatorFactory;
import cashmachine.exception.InterruptOperationException;

import java.util.Locale;
import java.util.ResourceBundle;

class DepositCommand implements Command {
    private ResourceBundle res = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "deposit_en", Locale.ENGLISH);

    @Override
    public void execute() throws InterruptOperationException {
        String currencyCode = null;
        try {
            ConsoleHelper.writeMessage(res.getString("before"));
            currencyCode = ConsoleHelper.askCurrencyCode();
            CurrencyManipulator currencyManipulator = CurrencyManipulatorFactory.getManipulatorByCurrencyCode(currencyCode);
            String[] s = ConsoleHelper.getValidTwoDigits(currencyCode);
            int nominal = Integer.parseInt(s[0]);
            int total = Integer.parseInt(s[1]);
            currencyManipulator.addAmount(nominal, total);
            ConsoleHelper.writeMessage(String.format(res.getString("success.format"), nominal * total, currencyCode));
        } catch (NumberFormatException e) {
            ConsoleHelper.writeMessage(res.getString("invalid.data"));
        }

    }
}
