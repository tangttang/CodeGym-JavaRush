package cashmachine.command;

import cashmachine.CashMachine;
import cashmachine.ConsoleHelper;
import cashmachine.CurrencyManipulator;
import cashmachine.CurrencyManipulatorFactory;

import java.util.Locale;
import java.util.ResourceBundle;

class InfoCommand implements Command {
    private ResourceBundle res = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "info_en", Locale.ENGLISH);

    @Override
    public void execute() {
        boolean presentMoney = false;
        ConsoleHelper.writeMessage(res.getString("before"));
        for (CurrencyManipulator currency : CurrencyManipulatorFactory.getAllCurrencyManipulators()) {
            if (currency.hasMoney()) {
                if (currency.getTotalAmount() > 0) {
                    ConsoleHelper.writeMessage(currency.getCurrencyCode() + " - " + currency.getTotalAmount());
                    presentMoney = true;
                }
            }
        }
        if (!presentMoney)
            ConsoleHelper.writeMessage(res.getString("no.money"));
    }
}
