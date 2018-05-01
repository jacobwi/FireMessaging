package com.jacob.firemessaging;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    private String username;
    private String message;
    private long timestamp;


    public Message() {
    }

    /**
     * Basic constructor for a message object.
     * @param username Author of the message
     * @param message Message text contents
     * @param timestamp When the message was sent (UNIX time)
     */
    public Message(String username, String message, Long timestamp) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Public getters are required by Firebase for all properties of the object we want to assign
     * when the object is deserialized.
     * @return The author of the message
     */
    public String getUsername() {
        return username;
    }

    /**
     * Public getters are required by Firebase for all properties of the object we want to assign
     * when the object is deserialized.
     * @return The contents of the message
     */
    public String getMessage() {
        return message;
    }

    /**

     * @return timestamp getter from firebase database
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @return it returns readable date and time format
     */
    public String getFormattedTimestamp() {
        String datePattern = "EEE, MMM d, h:mm a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        Date messageCreationDate = new Date(timestamp);
        return dateFormat.format(messageCreationDate);
    }
}

