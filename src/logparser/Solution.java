package logparser;


import java.nio.file.FileSystems;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Solution {
    public static void main(String[] args) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        LogParser logParser = new LogParser(FileSystems.getDefault().getPath("src", "logparser", "logs"));
        System.out.println(logParser.getNumberOfUniqueIPs(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getUniqueIPs(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getIPsForUser("Eduard Petrovich Morozko", dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getIPsForEvent(Event.DONE_TASK, dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println("all users " + logParser.getAllUsers());
        System.out.println(logParser.getNumberOfUsers(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getNumberOfUserEvents("Eduard Petrovich Morozko", dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getUsersForIP("192.168.100.2", dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getLoggedUsers(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getDownloadedPluginUsers(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getWroteMessageUsers(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getSolvedTaskUsers(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getSolvedTaskUsers(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7"), 1));
        System.out.println(logParser.getDoneTaskUsers(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getDoneTaskUsers(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7"), 48));
        //3
        System.out.println(logParser.getDatesForUserAndEvent("Eduard Petrovich Morozko", Event.DONE_TASK, dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getDatesWhenSomethingFailed(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getDatesWhenErrorHappened(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getDateWhenUserLoggedFirstTime("Eduard Petrovich Morozko", dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getDateWhenUserSolvedTask("Vasya Pupkin", 15, dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getDateWhenUserDoneTask("Vasya Pupkin", 15, dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getDatesWhenUserWroteMessage("Eduard Petrovich Morozko", dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getDatesWhenUserDownloadedPlugin("Eduard Petrovich Morozko", dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        //4
        System.out.println(logParser.getNumberOfAllEvents(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println("all events " + logParser.getAllEvents(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getEventsForIP("120.120.120.122", dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getEventsForUser("Amigo", dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getNumberOfAttemptToSolveTask(18, dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getNumberOfSuccessfulAttemptToSolveTask(48, dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getAllSolvedTasksAndTheirNumber(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.getAllDoneTasksAndTheirNumber(dateFormat.parse("30.08.2012 16:08:13"), dateFormat.parse("29.2.2028 5:4:7")));
        System.out.println(logParser.execute("get date"));
        //6
        //query language
        System.out.println("\nQuery language:");
        System.out.println(logParser.execute("get ip"));
        System.out.println(logParser.execute("get user"));
        System.out.println(logParser.execute("get event"));
        System.out.println(logParser.execute("get ip for user = \"Eduard Petrovich Morozko\""));
        System.out.println(logParser.execute("get ip for user = \"Eduard Petrovich Morozko\" and date between \"11.12.2013 0:00:00\" and \"03.01.2014 23:59:59\"."));
        System.out.println(logParser.execute("get ip for user = \"Eduard Petrovich Morozko\" and date between \"13.09.2013 5:04:49\" and \"13.09.2013 5:04:51\"."));
    }
}