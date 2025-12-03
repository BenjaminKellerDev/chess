package websocket.messages;

import chess.ChessBoard;

import java.util.Objects;

public class LoadGameMessage extends ServerMessage {
    ChessBoard board;

    public LoadGameMessage(ServerMessageType type, ChessBoard board) {
        super(type);
        this.board = board;
    }

    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoadGameMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType() && getBoard() == that.getBoard();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType(), getBoard());
    }
}
