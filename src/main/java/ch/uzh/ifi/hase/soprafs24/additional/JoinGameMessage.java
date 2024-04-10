package ch.uzh.ifi.hase.soprafs24.additional;

import ch.uzh.ifi.hase.soprafs24.additional.GameMessage;
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
