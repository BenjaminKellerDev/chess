package chess;

import java.util.HashSet;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    final private ChessPiece[][] board = new ChessPiece[8][8];
    final public int size = 8;

    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (!isPositionOffBoard(position)) {
            return board[position.getRow() - 1][position.getColumn() - 1];
        }
        return null;
    }

    public boolean isPositionOffBoard(ChessPosition position) {
        if (position.getRow() - 1 > 7 || position.getColumn() - 1 > 7
                || position.getRow() - 1 < 0 || position.getColumn() - 1 < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = null;
            }
        }

        for (int i = 0; i < board.length; i++) {
            board[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            board[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        board[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        board[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        board[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        board[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        board[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    }

    @Override
    public String toString() {

        StringBuilder ss = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            ss.append('|');
            for (int j = 0; j < board[i].length; j++) {
                boolean isWhite = false;
                if (board[i][j] != null && board[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) {
                    isWhite = true;
                }
                if (board[i][j] == null) {
                    ss.append(" |");
                } else {
                    switch (board[i][j].getPieceType()) {
                        case PAWN:
                            if (isWhite) {
                                ss.append("P|");
                            } else {
                                ss.append("p|");
                            }
                            break;
                        case QUEEN:
                            if (isWhite) {
                                ss.append("Q|");
                            } else {
                                ss.append("q|");
                            }
                            break;
                        case KING:
                            if (isWhite) {
                                ss.append("K|");
                            } else {
                                ss.append("k|");
                            }
                            break;
                        case ROOK:
                            if (isWhite) {
                                ss.append("R|");
                            } else {
                                ss.append("r|");
                            }
                            break;
                        case BISHOP:
                            if (isWhite) {
                                ss.append("B|");
                            } else {
                                ss.append("b|");
                            }
                            break;
                        case KNIGHT:
                            if (isWhite) {
                                ss.append("N|");
                            } else {
                                ss.append("n|");
                            }
                            break;
                        default:
                            ss.append("?|");
                            //throw new RuntimeException("Unknown piece type");
                            break;
                    }
                }
            }
            ss.append(System.lineSeparator());
        }
        return ss.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
