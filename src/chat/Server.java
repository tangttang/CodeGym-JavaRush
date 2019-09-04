package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Hello! It`s the server program. Enter here any available port.");
        int port = ConsoleHelper.readInt();
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("The server is now working! You can start connecting clients to this 'localhost' server.");
            while (true) {
                Handler handler = new Handler(server.accept());
                handler.start();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void sendBroadcastMessage(Message message) {
        for (Connection connection : connectionMap.values()) {
            try {
                connection.send(message);
            } catch (IOException e) {
                System.out.println("Failed to send message.");
            }
        }
    }

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            ConsoleHelper.writeMessage("Connected with address " + socket.getRemoteSocketAddress());
            String userName = null;
            Connection connection = null;
            try {
                connection = new Connection(socket);
                userName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));
                notifyUsers(connection, userName);
                serverMainLoop(connection, userName);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (userName != null) {
                    connectionMap.remove(userName);
                    sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
                }
            }
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST, "Enter your name: "));
                Message answer = connection.receive();

                if (answer.getType() == MessageType.USER_NAME && !answer.getData().isEmpty() && !connectionMap.containsKey(answer.getData())) {
                    connectionMap.put(answer.getData(), connection);
                    connection.send(new Message(MessageType.NAME_ACCEPTED, "You are added to the chat!"));
                    return answer.getData();
                }
            }
        }

        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (String clientName : connectionMap.keySet()) {
                if (!clientName.equals(userName))
                    connection.send(new Message(MessageType.USER_ADDED, clientName));
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message income = connection.receive();
                if (income.getType() == MessageType.TEXT) {
                    String messageText = userName + ": " + income.getData();
                    sendBroadcastMessage(new Message(MessageType.TEXT, messageText));
                } else {
                    ConsoleHelper.writeMessage("Error");
                }
            }
        }
    }
}

