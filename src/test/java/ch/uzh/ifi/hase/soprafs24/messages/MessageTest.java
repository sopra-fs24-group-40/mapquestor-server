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
    @DisplayName("Message constructor sets fields correctly")
    public void messageConstructorSetsFieldsCorrectly() {
        Message<String> message = new Message<>("sender", "content", MessageType.CHAT);

        Assertions.assertEquals("sender", message.getFrom());
        Assertions.assertEquals("content", message.getContent());
        Assertions.assertEquals(MessageType.CHAT, message.getType());
    }

    @Test
    @DisplayName("Message setters update fields correctly")
    public void messageSettersUpdateFieldsCorrectly() {
        Message<String> message = new Message<>();
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
        Message<String> message = new Message<>("sender", null, MessageType.CHAT);

        Assertions.assertEquals("sender", message.getFrom());
        Assertions.assertNull(message.getContent());
        Assertions.assertEquals(MessageType.CHAT, message.getType());
    }

    @Test
    @DisplayName("Message constructor handles null sender")
    public void messageConstructorHandlesNullSender() {
        Message<String> message = new Message<>(null, "content", MessageType.CHAT);

        Assertions.assertNull(message.getFrom());
        Assertions.assertEquals("content", message.getContent());
        Assertions.assertEquals(MessageType.CHAT, message.getType());
    }

    @Test
    @DisplayName("Message constructor handles null type")
    public void messageConstructorHandlesNullType() {
        Message<String> message = new Message<>("sender", "content", null);

        Assertions.assertEquals("sender", message.getFrom());
        Assertions.assertEquals("content", message.getContent());
        Assertions.assertNull(message.getType());
    }

    @Test
    @DisplayName("Message constructor handles all null fields")
    public void messageConstructorHandlesAllNullFields() {
        Message<String> message = new Message<>(null, null, null);

        Assertions.assertNull(message.getFrom());
        Assertions.assertNull(message.getContent());
        Assertions.assertNull(message.getType());
    }
}