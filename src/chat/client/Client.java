package chat.client;

import chat.Connection;
import chat.ConsoleHelper;
import chat.Message;
import chat.MessageType;

import java.io.IOException;
import java.net.Socket;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }


    protected String getServerAddress() {
        System.out.println("Enter server address.");
        return ConsoleHelper.readString();
    }

    protected int getServerPort() {
        System.out.println("Enter server port.");
        return ConsoleHelper.readInt();
    }

    protected String getUserName() {
        return ConsoleHelper.readString();
    }

    protected boolean shouldSendTextFromConsole() {
        return true;
    }

    protected SocketThread getSocketThread() {
        return new SocketThread();
    }

    protected void sendTextMessage(String text) {
        try {
            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException e) {
            e.printStackTrace();
            clientConnected = false;
        }
    }

    public void run() {
        SocketThread thread = getSocketThread();
        thread.setDaemon(true);
        thread.start();

        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                ConsoleHelper.writeMessage("Socket thread is interrupted!");
            }
        }
        while (clientConnected) {
            String str = ConsoleHelper.readString();
            if (str.equals("exit")) {
                break;
            }
            if (shouldSendTextFromConsole()) {
                sendTextMessage(str);
            }
        }
    }


    public class SocketThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                Socket socket = new Socket(getServerAddress(), getServerPort());
                connection = new Connection(socket);
                clientHandshake();
                clientMainLoop();
            } catch (IOException e) {
                e.printStackTrace();
                notifyConnectionStatusChanged(false);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                notifyConnectionStatusChanged(false);
            }
        }

        protected void clientHandshake() throws IOException, ClassNotFoundException {
            while (!clientConnected) {
                Message message = connection.receive();
                if (message.getType() == MessageType.NAME_REQUEST) {
                    System.out.println("Enter your name.");
                    connection.send(new Message(MessageType.USER_NAME, getUserName()));
                } else if (message.getType() == MessageType.NAME_ACCEPTED) {
                    System.out.println("Your name accepted! You can now put some text down.");
                    notifyConnectionStatusChanged(true);
                    break;
                } else {
                    System.out.println("lol");
                    throw new IOException("Unexpected MessageType");
                }
            }
        }

        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT) {
                    processIncomingMessage(message.getData());
                } else if (message.getType() == MessageType.USER_ADDED) {
                    informAboutAddingNewUser(message.getData());
                } else if (message.getType() == MessageType.USER_REMOVED) {
                    informAboutDeletingNewUser(message.getData());
                } else {
                    throw new IOException("Unexpected MessageType");
                }
            }
        }

        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage(new Message(MessageType.USER_ADDED, userName).getData());
            ConsoleHelper.writeMessage("User " + userName + " added to the chat!");
        }

        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage(new Message(MessageType.USER_REMOVED, userName).getData());
            ConsoleHelper.writeMessage("User " + userName + " leaves the chat!");
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }


    }
}



