package ch.uzh.ifi.hase.soprafs24.messages;

import ch.uzh.ifi.hase.soprafs24.constant.MessageType;


public class Message<T> {

    private String from;
    private T content;

    private MessageType type;

    public Message() {
    }

    public Message(String from, T content, MessageType type) {
        this.from = from;
        this.content = content;
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
