package websocket.messages;

import java.util.Objects;

public class ErrorMessage extends ServerMessage {
    String errorMessage;

    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ErrorMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType() && getErrorMessage() == that.getErrorMessage();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType(), getErrorMessage());
    }
}
