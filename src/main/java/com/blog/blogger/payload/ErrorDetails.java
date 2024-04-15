package com.blog.blogger.payload;

import java.util.Date;

public class ErrorDetails {

    private Date TimeStamp;

    private String message;

    private String details;

    public ErrorDetails(Date timeStamp, String message, String details) {
        TimeStamp = timeStamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimeStamp() {
        return TimeStamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
