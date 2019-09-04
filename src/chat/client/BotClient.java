package chat.client;

import chat.ConsoleHelper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BotClient extends Client {
    private String currentName;

    public static void main(String[] args) {
        BotClient botClient = new BotClient();
        botClient.run();
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {
        currentName = "date_bot_" + (int) (Math.random() * 100);
        return currentName;
    }

    public class BotSocketThread extends SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Hello chat. I am " + currentName + ". \nI understand the following commands: date, day, month, year, time, hour, minute, second.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
            if (message.contains(": ")) {
                String[] words = message.split(": ");
                DateFormat dateFormat;
                switch (words[1]) {
                    case "date":
                        dateFormat = new SimpleDateFormat("d.MM.YYYY");
                        break;
                    case "day":
                        dateFormat = new SimpleDateFormat("d");
                        break;
                    case "month":
                        dateFormat = new SimpleDateFormat("MMMM");
                        break;
                    case "year":
                        dateFormat = new SimpleDateFormat("YYYY");
                        break;
                    case "time":
                        dateFormat = new SimpleDateFormat("H:mm:ss");
                        break;
                    case "hour":
                        dateFormat = new SimpleDateFormat("H");
                        break;
                    case "minute":
                        dateFormat = new SimpleDateFormat("m");
                        break;
                    case "second":
                        dateFormat = new SimpleDateFormat("s");
                        break;
                    default:
                        dateFormat = null;
                        break;
                }
                if (dateFormat != null) {
                    sendTextMessage("Information for " + message.substring(0, message.indexOf(':')) + ": " + dateFormat.format(Calendar.getInstance().getTime()));
                }
            }
        }
    }
}
