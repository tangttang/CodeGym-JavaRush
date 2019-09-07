package cashmachine.command;

import cashmachine.Operation;
import cashmachine.exception.InterruptOperationException;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private static boolean verified;

    private static final Map<Operation, Command> allKnownCommandsMap = new HashMap<>();

    static {
        allKnownCommandsMap.put(Operation.LOGIN, new LoginCommand());
        allKnownCommandsMap.put(Operation.INFO, new InfoCommand());
        allKnownCommandsMap.put(Operation.DEPOSIT, new DepositCommand());
        allKnownCommandsMap.put(Operation.WITHDRAW, new WithdrawCommand());
        allKnownCommandsMap.put(Operation.EXIT, new ExitCommand());
    }

    private CommandExecutor() {
    }

    public static void execute(Operation operation) throws InterruptOperationException {
        verified = verify(operation);
    }

    private static boolean verify(Operation operation) throws InterruptOperationException {
        if (operation.equals(Operation.EXIT)) {
            ExitCommand ec = (ExitCommand) allKnownCommandsMap.get(Operation.EXIT);
            return ec.verify();
        } else {
            allKnownCommandsMap.get(operation).execute();
            return false;
        }
    }

    public static boolean isVerified() {
        return verified;
    }
}
