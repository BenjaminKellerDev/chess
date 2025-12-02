package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

public class LoadGameMessage extends ServerMessage {
    ChessGame game;

    public LoadGameMessage(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoadGameMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType() && getGame() == that.getGame();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType(), getGame());
    }
}
