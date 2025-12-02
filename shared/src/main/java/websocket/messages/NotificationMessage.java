package websocket.messages;

import java.util.Objects;

public class NotificationMessage extends ServerMessage {
    String message;

    public NotificationMessage(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType() && getMessage() == that.getMessage();
    }


    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType(), getMessage());
    }
}
