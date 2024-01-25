package com.example.api_server.model;
import java.util.ArrayList;
import java.util.Date;

public class ValidationErrorMessage {
    private int statusCode;
    private Date timestamp;
    private ArrayList<String> messages;

    public ValidationErrorMessage(int statusCode, Date timestamp, ArrayList<String> messages){
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.messages = messages;
    }
    public int getStatusCode(){ return this.statusCode; }
    public Date getTimeStamp(){ return this.timestamp; }
    public ArrayList<String> getMessages(){ return this.messages; }
}
