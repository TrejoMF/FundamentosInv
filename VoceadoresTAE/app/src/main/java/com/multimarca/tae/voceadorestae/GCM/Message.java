package com.multimarca.tae.voceadorestae.GCM;

public class Message {


    private String Title;
    private String Message;

    public Message(String title, String message) {
        Title = title;
        Message = message;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
