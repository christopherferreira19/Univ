package common;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final String content;
    private final LocalTime timestamp;
    private final User userFrom;
    private final User userTo;

    public Message(String content, User user) {
        this.content = content;
        this.timestamp = LocalTime.now();
        this.userFrom = user;
        this.userTo = null;
    }


    public Message(String content, User userFrom, User userTo) {
        this.content = content;
        this.timestamp = LocalTime.now();
        this.userFrom = userFrom;
        this.userTo = userTo;
    }

    public String getContent() {
        return content;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public User getUserFrom() {
        return userFrom;
    }

    @Override
    public String toString() {
        String timestampStr = DATE_TIME_FORMATTER.format(timestamp);
        if(userTo == null)
            return "[" + timestampStr + "] " + userFrom + ": " + content;
        return "Private [" + timestampStr + "] " + userFrom + ": " + content;
    }
}
