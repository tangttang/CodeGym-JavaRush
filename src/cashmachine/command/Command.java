package cashmachine.command;

import cashmachine.exception.InterruptOperationException;

interface Command {
    void execute() throws InterruptOperationException;
}
