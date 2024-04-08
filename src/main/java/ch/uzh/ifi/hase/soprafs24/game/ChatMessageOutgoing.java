package ch.uzh.ifi.hase.soprafs24.game;

import ch.uzh.ifi.hase.soprafs24.constant.MessageType;
import ch.uzh.ifi.hase.soprafs24.rest.dto.game.UserGameInfoDTO;

public class ChatMessageOutgoing {

    private UserGameInfoDTO from;
    private String text;

    private MessageType type;

    public ChatMessageOutgoing() {
    }

    public ChatMessageOutgoing(UserGameInfoDTO from, String text, MessageType type) {
        this.from = from;
        this.text = text;
        this.type = type;
    }


    public UserGameInfoDTO getFrom() {
        return from;
    }

    public void setFrom(UserGameInfoDTO from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
