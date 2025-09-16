package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        switch (type){
            case KING:
                break;
            case QUEEN:
                break;
            case BISHOP:
                break;
            case KNIGHT:
                break;
            case ROOK:
                //Column / x to the right
                for (int x = myPosition.getColumn(); x < board.size; x++) {
                    ChessPosition positionToCheck =new ChessPosition(myPosition.getRow(),x);
                    if(board.getPiece(positionToCheck) == null)
                    {
                        possibleMoves.add(new ChessMove(myPosition,positionToCheck));
                    }
                    else {
                        break;
                    }
                }
                for (int x = myPosition.getColumn(); x > 0; x--) {
                    ChessPosition positionToCheck =new ChessPosition(x, myPosition.getRow());
                    if(board.getPiece(positionToCheck) == null)
                    {
                        possibleMoves.add(new ChessMove(myPosition,positionToCheck));
                    }
                    else {
                        break;
                    }
                }
                break;
            case PAWN:
                break;
            default:
                throw new RuntimeException("Unknown piece type");
        }
        return possibleMoves;
    }
}

private class Rule
