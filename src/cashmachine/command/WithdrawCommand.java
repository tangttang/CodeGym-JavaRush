package cashmachine.command;

import cashmachine.CashMachine;
import cashmachine.ConsoleHelper;
import cashmachine.CurrencyManipulator;
import cashmachine.CurrencyManipulatorFactory;
import cashmachine.exception.InterruptOperationException;
import cashmachine.exception.NotEnoughMoneyException;

import java.util.Map;
import java.util.ResourceBundle;

class WithdrawCommand implements Command {
    private ResourceBundle res = ResourceBundle.getBundle(CashMachine.class.getPackage().getName() + ".resources.withdraw_en");

    @Override
    public void execute() throws InterruptOperationException {
        ConsoleHelper.writeMessage(res.getString("before"));
        String code = ConsoleHelper.askCurrencyCode();
        CurrencyManipulator manipulator = CurrencyManipulatorFactory.getManipulatorByCurrencyCode(code);
        boolean correct = false;
        int amount;
        while (!correct) {
            try {
                ConsoleHelper.writeMessage(res.getString("specify.amount"));
                amount = Integer.parseInt(ConsoleHelper.readString());

                if (amount <= 0) {
                    ConsoleHelper.writeMessage(res.getString("specify.not.empty.amount"));
                } else if (!manipulator.isAmountAvailable(amount))
                    ConsoleHelper.writeMessage(res.getString("exact.amount.not.available"));
                else
                    correct = true;


                Map<Integer, Integer> withdrawBanknotes = manipulator.withdrawAmount(amount);
                for (Map.Entry<Integer, Integer> pair : withdrawBanknotes.entrySet()) {
                    System.out.println("\t" + pair.getKey() + " - " + pair.getValue());
                }
                ConsoleHelper.writeMessage(String.format(res.getString("success.format"), amount, code));
            } catch (NumberFormatException e) {
                ConsoleHelper.writeMessage(res.getString("exact.amount.not.available"));

            } catch (NotEnoughMoneyException e) {
                ConsoleHelper.writeMessage(res.getString("not.enough.money"));
            }
        }

    }
}
