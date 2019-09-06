package logparser;

import logparser.query.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery, QLQuery {
    private Path logDir;
    private ArrayList<String> logs = new ArrayList<>();
    private final Pattern pattern = Pattern.compile("(?<ip>[\\d]+.[\\d]+.[\\d]+.[\\d]+)\\s(?<name>[a-zA-Z ]+)\\s(?<date>[\\d]+.[\\d]+.[\\d]+ [\\d]+:[\\d]+:[\\d]+)\\s(?<event>[\\w]+)\\s?((?<taskNumber>[\\d]+)|)\\s(?<status>[\\w]+)");

    public LogParser(Path logDir) {
        this.logDir = logDir.toAbsolutePath();
        writeLogsFromPathToList(logs);
    }

    @Override
    public Set<Object> execute(String query) {
        switch (query) {
            case "get ip":
                return new HashSet<>(getUniqueIPs(null, null));
            case "get user":
                return new HashSet<>(getAllUsers());
            case "get date":
                return new HashSet<>(getUniqueDates());
            case "get event":
                return new HashSet<>(getAllEvents(null, null));
            case "get status":
                return new HashSet<>(getUniqueStatuses());
            default:
                return extendedExecute(query);
        }
    }

    private Set<Object> extendedExecute(String query) {
        String[] queryArray = query.split("\"");
        String[] queries = queryArray[0].split(" ");
        String field1 = queries[1];
        String field2 = queries[3];
        String value = queryArray[1];
        String after = null;
        String before = null;
        if (queryArray.length > 3) {
            after = queryArray[3];
            before = queryArray[5];
        }

        switch (field1) {
            case "ip":
                switch (field2) {
                    case "user":
                        return new HashSet<>(getIPsForUser(value, getDate(after), getDate(before)));
                    case "date":
                        return new HashSet<>(getIPsForDate(value, getDate(after), getDate(before)));
                    case "event":
                        return new HashSet<>(getIPsForEvent(getEvent(value), getDate(after), getDate(before)));
                    case "status":
                        return new HashSet<>(getIPsForStatus(getStatus(value), getDate(after), getDate(before)));
                }
                break;
            case "user":
                switch (field2) {
                    case "ip":
                        return new HashSet<>(getUsersForIP(value, getDate(after), getDate(before)));
                    case "date":
                        return new HashSet<>(getUsersForDate(value, getDate(after), getDate(before)));
                    case "event":
                        return new HashSet<>(getUsersForEvent(value, getDate(after), getDate(before)));
                    case "status":
                        return new HashSet<>(getUsersForStatus(value, getDate(after), getDate(before)));
                }
                break;
            case "date":
                switch (field2) {
                    case "ip":
                        return new HashSet<>(getDatesForIp(value, getDate(after), getDate(before)));
                    case "user":
                        return new HashSet<>(getDatesForUser(value, getDate(after), getDate(before)));
                    case "event":
                        return new HashSet<>(getDatesForEvent(value, getDate(after), getDate(before)));
                    case "status":
                        return new HashSet<>(getDatesForStatus(value, getDate(after), getDate(before)));
                }
                break;
            case "event":
                switch (field2) {
                    case "ip":
                        return new HashSet<>(getEventsForIP(value, getDate(after), getDate(before)));
                    case "user":
                        return new HashSet<>(getEventsForUser(value, getDate(after), getDate(before)));
                    case "date":
                        return new HashSet<>(getEventsForDate(value, getDate(after), getDate(before)));
                    case "status":
                        return new HashSet<>(getEventsForStatus(value, getDate(after), getDate(before)));
                }
                break;
            case "status":
                switch (field2) {
                    case "ip":
                        return new HashSet<>(getStatusesForIp(value, getDate(after), getDate(before)));
                    case "user":
                        return new HashSet<>(getStatusesForUser(value, getDate(after), getDate(before)));
                    case "date":
                        return new HashSet<>(getStatusesForDate(value, getDate(after), getDate(before)));
                    case "event":
                        return new HashSet<>(getStatusesForEvent(value, getDate(after), getDate(before)));
                }
                break;
        }
        return new HashSet<>();
    }

    @Override
    public int getNumberOfAllEvents(Date after, Date before) {
        return getAllEvents(after, before).size();
    }

    @Override
    public Set<Event> getAllEvents(Date after, Date before) {
        Set<Event> events = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                events.add(getEvent(matcher.group("event")));
        }
        return events;
    }

    @Override
    public Set<Event> getEventsForIP(String ip, Date after, Date before) {
        Set<Event> events = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (ip.equals(matcher.group("ip")))
                    events.add(getEvent(matcher.group("event")));
        }
        return events;
    }

    @Override
    public Set<Event> getEventsForUser(String user, Date after, Date before) {
        Set<Event> events = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (user.equals(matcher.group("name")))
                    events.add(getEvent(matcher.group("event")));
        }
        return events;
    }

    @Override
    public Set<Event> getFailedEvents(Date after, Date before) {
        Set<Event> events = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if ("FAILED".equals(matcher.group("status")))
                    events.add(getEvent(matcher.group("event")));
        }
        return events;
    }

    @Override
    public Set<Event> getErrorEvents(Date after, Date before) {
        Set<Event> events = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if ("ERROR".equals(matcher.group("status")))
                    events.add(getEvent(matcher.group("event")));
        }
        return events;
    }

    @Override
    public int getNumberOfAttemptToSolveTask(int task, Date after, Date before) {
        int count = 0;
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if ("SOLVE_TASK".equals(matcher.group("event")) && matcher.group("taskNumber") != null && task == Integer.parseInt(matcher.group("taskNumber")))
                    count++;
        }
        return count;
    }

    @Override
    public int getNumberOfSuccessfulAttemptToSolveTask(int task, Date after, Date before) {
        int count = 0;
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if ("DONE_TASK".equals(matcher.group("event")) && matcher.group("taskNumber") != null && task == Integer.parseInt(matcher.group("taskNumber")))
                    count++;
        }
        return count;
    }

    @Override
    public Map<Integer, Integer> getAllSolvedTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> solvedTasks = new HashMap<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if ("SOLVE_TASK".equals(matcher.group("event")) && matcher.group("taskNumber") != null) {
                    int taskNumber = Integer.parseInt(matcher.group("taskNumber"));
                    if (solvedTasks.containsKey(taskNumber))
                        solvedTasks.put(taskNumber, solvedTasks.get(taskNumber) + 1);
                    else solvedTasks.put(taskNumber, 1);
                }
        }
        return solvedTasks;
    }

    @Override
    public Map<Integer, Integer> getAllDoneTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> solvedTasks = new HashMap<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if ("DONE_TASK".equals(matcher.group("event")) && matcher.group("taskNumber") != null) {
                    int taskNumber = Integer.parseInt(matcher.group("taskNumber"));
                    if (solvedTasks.containsKey(taskNumber))
                        solvedTasks.put(taskNumber, solvedTasks.get(taskNumber) + 1);
                    else solvedTasks.put(taskNumber, 1);
                }
        }
        return solvedTasks;
    }

    @Override
    public Set<Date> getDatesForUserAndEvent(String user, Event event, Date after, Date before) {
        Set<Date> dates = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            Date currentDate;
            if (matcher.find() && isDateInRange(currentDate = getDate(matcher.group("date")), after, before))
                if (user.equals(matcher.group("name")) && event.toString().equals(matcher.group("event")))
                    dates.add(currentDate);
        }
        return dates;
    }

    @Override
    public Set<Date> getDatesWhenSomethingFailed(Date after, Date before) {
        Set<Date> dates = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            Date currentDate;
            if (matcher.find() && isDateInRange(currentDate = getDate(matcher.group("date")), after, before))
                if (matcher.group("status").equals("FAILED"))
                    dates.add(currentDate);
        }
        return dates;
    }

    @Override
    public Set<Date> getDatesWhenErrorHappened(Date after, Date before) {
        Set<Date> dates = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            Date currentDate;
            if (matcher.find() && isDateInRange(currentDate = getDate(matcher.group("date")), after, before))
                if (matcher.group("status").equals("ERROR"))
                    dates.add(currentDate);
        }
        return dates;
    }

    @Override
    public Date getDateWhenUserLoggedFirstTime(String user, Date after, Date before) {
        Set<Date> dates = new TreeSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            Date currentDate;
            if (matcher.find() && isDateInRange(currentDate = getDate(matcher.group("date")), after, before))
                if (user.equals(matcher.group("name")) && matcher.group("event").equals("LOGIN"))
                    dates.add(currentDate);
        }
        return !dates.isEmpty() ? ((TreeSet<Date>) dates).pollFirst() : null;
    }

    @Override
    public Date getDateWhenUserSolvedTask(String user, int task, Date after, Date before) {
        Set<Date> dates = new TreeSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            Date currentDate;
            if (matcher.find() && isDateInRange(currentDate = getDate(matcher.group("date")), after, before)) {
                if (user.equals(matcher.group("name")) && matcher.group("event").equals("SOLVE_TASK") && matcher.group("taskNumber") != null && task == Integer.parseInt(matcher.group("taskNumber")))
                    dates.add(currentDate);
            }
        }
        return !dates.isEmpty() ? ((TreeSet<Date>) dates).pollFirst() : null;
    }

    @Override
    public Date getDateWhenUserDoneTask(String user, int task, Date after, Date before) {
        Set<Date> dates = new TreeSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            Date currentDate;
            if (matcher.find() && isDateInRange(currentDate = getDate(matcher.group("date")), after, before)) {
                if (user.equals(matcher.group("name")) && matcher.group("event").equals("DONE_TASK") && matcher.group("taskNumber") != null && task == Integer.parseInt(matcher.group("taskNumber")))
                    dates.add(currentDate);
            }
        }
        return !dates.isEmpty() ? ((TreeSet<Date>) dates).pollFirst() : null;
    }

    @Override
    public Set<Date> getDatesWhenUserWroteMessage(String user, Date after, Date before) {
        Set<Date> dates = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            Date currentDate;
            if (matcher.find() && isDateInRange(currentDate = getDate(matcher.group("date")), after, before))
                if (user.equals(matcher.group("name")) && matcher.group("event").equals("WRITE_MESSAGE"))
                    dates.add(currentDate);
        }
        return dates;
    }

    @Override
    public Set<Date> getDatesWhenUserDownloadedPlugin(String user, Date after, Date before) {
        Set<Date> dates = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            Date currentDate;
            if (matcher.find() && isDateInRange(currentDate = getDate(matcher.group("date")), after, before))
                if (user.equals(matcher.group("name")) && matcher.group("event").equals("DOWNLOAD_PLUGIN"))
                    dates.add(currentDate);
        }
        return dates;
    }

    @Override
    public Set<String> getAllUsers() {
        return logs.stream()
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> matcher.group("name"))
                .collect(Collectors.toSet());
    }

    @Override
    public int getNumberOfUsers(Date after, Date before) {
        Set<String> users = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                users.add(matcher.group("name"));
        }
        return users.size();
    }

    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {
        Set<String> events = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if (user.equals(matcher.group("name")))
                    events.add(matcher.group("event"));
        }
        return events.size();
    }

    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {
        Set<String> users = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (ip.equals(matcher.group("ip")))
                    users.add(matcher.group("name"));
        }
        return users;
    }

    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {
        Set<String> users = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if ("LOGIN".equals(matcher.group("event")))
                    users.add(matcher.group("name"));
        }
        return users;
    }

    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {
        Set<String> users = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if ("DOWNLOAD_PLUGIN".equals(matcher.group("event")))
                    users.add(matcher.group("name"));
        }
        return users;
    }

    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {
        Set<String> users = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if ("WRITE_MESSAGE".equals(matcher.group("event")))
                    users.add(matcher.group("name"));
        }
        return users;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {
        Set<String> users = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if ("SOLVE_TASK".equals(matcher.group("event")))
                    users.add(matcher.group("name"));
        }
        return users;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {
        Set<String> users = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if ((matcher.group("event") + " " + matcher.group("taskNumber")).equals("SOLVE_TASK " + task))
                    users.add(matcher.group("name"));
        }
        return users;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {
        Set<String> users = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if ("DONE_TASK".equals(matcher.group("event")))
                    users.add(matcher.group("name"));
        }
        return users;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before, int task) {
        Set<String> users = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                if ((matcher.group("event") + " " + matcher.group("taskNumber")).equals("DONE_TASK " + task))
                    users.add(matcher.group("name"));
        }
        return users;
    }

    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {
        return getUniqueIPs(after, before).size();
    }

    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        Set<String> ips = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInRange(getDate(matcher.group("date")), after, before))
                ips.add(matcher.group("ip"));
        }
        return ips;
    }

    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        Set<String> ips = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (user.equals(matcher.group("name")))
                    ips.add(matcher.group("ip"));
        }
        return ips;
    }

    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        Set<String> ips = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (event.toString().equals(matcher.group("event")))
                    ips.add(matcher.group("ip"));
        }
        return ips;
    }

    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        Set<String> ips = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (status.toString().equals(matcher.group("status")))
                    ips.add(matcher.group("ip"));
        }
        return ips;
    }

    private Set<String> getIPsForDate(String date, Date after, Date before) {
        Set<String> ips = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (getDate(matcher.group("date")).equals(getDate(date)))
                    ips.add(matcher.group("ip"));
        }
        return ips;
    }

    private Set<String> getUsersForDate(String date, Date after, Date before) {
        Set<String> users = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (getDate(matcher.group("date")).equals(getDate(date)))
                    users.add(matcher.group("name"));
        }
        return users;
    }

    private Set<String> getUsersForEvent(String event, Date after, Date before) {
        Set<String> users = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (matcher.group("event").equals(event))
                    users.add(matcher.group("name"));
        }
        return users;
    }

    private Set<String> getUsersForStatus(String status, Date after, Date before) {
        Set<String> users = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (matcher.group("status").equals(status))
                    users.add(matcher.group("name"));
        }
        return users;
    }

    private Set<Date> getDatesForIp(String ip, Date after, Date before) {
        Set<Date> dates = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (matcher.group("ip").equals(ip))
                    dates.add(getDate(matcher.group("date")));
        }
        return dates;
    }

    private Set<Date> getDatesForUser(String user, Date after, Date before) {
        Set<Date> dates = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (user.equals(matcher.group("name")))
                    dates.add(getDate(matcher.group("date")));
        }
        return dates;
    }

    private Set<Date> getDatesForEvent(String event, Date after, Date before) {
        Set<Date> dates = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (event.equals(matcher.group("event")))
                    dates.add(getDate(matcher.group("date")));
        }
        return dates;
    }

    private Set<Date> getDatesForStatus(String status, Date after, Date before) {
        Set<Date> dates = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (status.equals(matcher.group("status")))
                    dates.add(getDate(matcher.group("date")));
        }
        return dates;
    }

    private Set<Event> getEventsForDate(String date, Date after, Date before) {
        Set<Event> events = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (date.equals(matcher.group("date")))
                    events.add(getEvent(matcher.group("event")));
        }
        return events;
    }

    private Set<Event> getEventsForStatus(String status, Date after, Date before) {
        Set<Event> events = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (status.equals(matcher.group("status")))
                    events.add(getEvent(matcher.group("event")));
        }
        return events;
    }

    private Set<Status> getStatusesForIp(String ip, Date after, Date before) {
        Set<Status> statuses = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (matcher.group("ip").equals(ip))
                    statuses.add(getStatus(matcher.group("status")));
        }
        return statuses;
    }

    private Set<Status> getStatusesForUser(String user, Date after, Date before) {
        Set<Status> statuses = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (matcher.group("name").equals(user))
                    statuses.add(getStatus(matcher.group("status")));
        }
        return statuses;
    }

    private Set<Status> getStatusesForDate(String date, Date after, Date before) {
        Set<Status> statuses = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (matcher.group("date").equals(date))
                    statuses.add(getStatus(matcher.group("status")));
        }
        return statuses;
    }

    private Set<Status> getStatusesForEvent(String event, Date after, Date before) {
        Set<Status> statuses = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find() && isDateInStrictlyRange(getDate(matcher.group("date")), after, before))
                if (matcher.group("event").equals(event))
                    statuses.add(getStatus(matcher.group("status")));
        }
        return statuses;
    }


    private void writeLogsFromPathToList(List<String> list) {
        File[] files = logDir.toFile().listFiles();
        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                if (file.getName().endsWith(".log")) {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        list.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Date getDate(String time) {
        if (time == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private boolean isDateInRange(Date currentDate, Date after, Date before) {
        boolean isBefore = before == null || currentDate.before(before) || currentDate.equals(before);
        boolean isAfter = after == null || currentDate.after(after) || currentDate.equals(after);
        return isBefore && isAfter;
    }

    private boolean isDateInStrictlyRange(Date currentDate, Date after, Date before) {
        boolean isBefore = before == null || currentDate.before(before);
        boolean isAfter = after == null || currentDate.after(after);
        return isBefore && isAfter;
    }

    private Event getEvent(String enumeration) {
        switch (enumeration) {
            case "LOGIN":
                return Event.LOGIN;
            case "DOWNLOAD_PLUGIN":
                return Event.DOWNLOAD_PLUGIN;
            case "WRITE_MESSAGE":
                return Event.WRITE_MESSAGE;
            case "SOLVE_TASK":
                return Event.SOLVE_TASK;
            case "DONE_TASK":
                return Event.DONE_TASK;
            default:
                return null;
        }
    }

    private Status getStatus(String status) {
        switch (status) {
            case "OK":
                return Status.OK;
            case "FAILED":
                return Status.FAILED;
            case "ERROR":
                return Status.ERROR;
            default:
                return null;
        }
    }

    private Set<Date> getUniqueDates() {
        Set<Date> dates = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                dates.add(getDate(matcher.group("date")));
            }
        }
        return dates;
    }

    private Set<Status> getUniqueStatuses() {
        Set<Status> statuses = new HashSet<>();
        for (String line : logs) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                statuses.add(getStatus(matcher.group("status")));
            }
        }
        return statuses;
    }
}