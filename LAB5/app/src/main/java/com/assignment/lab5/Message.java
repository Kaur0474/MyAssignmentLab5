package com.assignment.lab5;

public class Message {
    long db_id;
    String message;
    boolean isSent;

    public Message(long db_id, String message, boolean isSent) {
        this.db_id = db_id;
        this.message = message;
        this.isSent = isSent;
    }

    public long getDb_id() {
        return db_id;
    }

    public void setDb_id(long db_id) {
        this.db_id = db_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }
}
