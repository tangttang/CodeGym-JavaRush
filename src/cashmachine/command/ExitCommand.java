package cashmachine.command;

import cashmachine.CashMachine;
import cashmachine.ConsoleHelper;
import cashmachine.exception.InterruptOperationException;

import java.util.Locale;
import java.util.ResourceBundle;

class ExitCommand implements Command {
    private ResourceBundle res = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "exit_en", Locale.ENGLISH);

    @Override
    public void execute() throws InterruptOperationException {
        ConsoleHelper.writeMessage(res.getString("thank.message"));
    }

    boolean verify() throws InterruptOperationException {
        ConsoleHelper.writeMessage(res.getString("exit.question.y.n"));
        if (ConsoleHelper.readString().equalsIgnoreCase("Y")) {
            execute();
            return true;
        }
        return false;
    }
}
