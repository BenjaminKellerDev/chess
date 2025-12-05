package chess;


import java.util.regex.Pattern;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
        this(startPosition, endPosition, null);
    }

    public ChessMove(ChessMove oldMove, ChessPiece.PieceType promotionPiece) {
        this.startPosition = oldMove.getStartPosition();
        this.endPosition = oldMove.getEndPosition();
        this.promotionPiece = promotionPiece;
    }


    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    public static ChessMove textToMove(String text) throws InvalidMoveException {
        final String regex = "^[a-h]\\d[a-h]\\d$";
        if (!Pattern.matches(regex, text)) {
            throw new InvalidMoveException("Chess position format invalid");
        }
        return new ChessMove(new ChessPosition(Character.getNumericValue(text.charAt(1)), (int) text.charAt(0) - 96),
                new ChessPosition(Character.getNumericValue(text.charAt(3)), (int) text.charAt(2) - 96));
    }

    public static String toUserString(ChessMove chessMove) {
        return (char) ('a' + chessMove.getStartPosition().getColumn() - 1) + String.valueOf(chessMove.getStartPosition().getRow())
                + (char) ('a' + chessMove.getEndPosition().getColumn() - 1) + String.valueOf(chessMove.getEndPosition().getRow());
    }

    @Override
    public String toString() {
        return String.format("ChessMove{%s,%s,%s}", startPosition, endPosition, promotionPiece);
    }

    @Override
    public int hashCode() {
        //todo add promotion peice hash
        if (promotionPiece == null) {
            return (13 * startPosition.hashCode()) + (27 * endPosition.hashCode());
        } else {
            return (13 * startPosition.hashCode()) + (27 * endPosition.hashCode() + (7 * promotionPiece.ordinal()));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChessMove that = (ChessMove) obj;
        if (that.startPosition.equals(this.startPosition)
                && that.endPosition.equals(this.endPosition)
                && that.promotionPiece == this.promotionPiece) {
            return true;
        }

        return false;
    }
}
