package com.massky.greenlandvland.model.entity;

/**
 * Created by masskywcy on 2017-12-21.
 */

public class Message {
    private String type;
    private String value;

    public Message(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
