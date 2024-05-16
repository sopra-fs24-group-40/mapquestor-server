package ch.uzh.ifi.hase.soprafs24.messages;

import ch.uzh.ifi.hase.soprafs24.constant.MessageType;
import ch.uzh.ifi.hase.soprafs24.messages.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MessageTest {

    private Message<String> message;

    @BeforeEach
    public void setup() {
        message = new Message<>();
    }

    @Test
    @DisplayName("Message fields are set correctly by constructor")
    public void messageConstructorSetsFieldsCorrectly() {
        message = new Message<>("sender", "content", MessageType.CHAT);

        Assertions.assertEquals("sender", message.getFrom());
        Assertions.assertEquals("content", message.getContent());
        Assertions.assertEquals(MessageType.CHAT, message.getType());
    }

    @Test
    @DisplayName("Message fields are updated correctly by setters")
    public void messageSettersUpdateFieldsCorrectly() {
        message.setFrom("sender");
        message.setContent("content");
        message.setType(MessageType.CHAT);

        Assertions.assertEquals("sender", message.getFrom());
        Assertions.assertEquals("content", message.getContent());
        Assertions.assertEquals(MessageType.CHAT, message.getType());
    }

    @Test
    @DisplayName("Message constructor handles null content")
    public void messageConstructorHandlesNullContent() {
        message = new Message<>("sender", null, MessageType.CHAT);

        Assertions.assertEquals("sender", message.getFrom());
        Assertions.assertNull(message.getContent());
        Assertions.assertEquals(MessageType.CHAT, message.getType());
    }
}