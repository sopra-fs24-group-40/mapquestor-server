package ch.uzh.ifi.hase.soprafs24.controller;

import lombok.*;

@Getter
@Setter
@ToString
public class JoinGameMessage extends GameMessage {
    private String name;

    private JoinGameMessage() {
        super("join");
    }

    public JoinGameMessage(String name) {
        super("join");
        this.name = name;
    }
}
