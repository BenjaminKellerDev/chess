package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
        switch (type) {
            case KING:
                possibleMoves.addAll(getMoves(board, myPosition, new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}}, false));
                break;
            case QUEEN:
                possibleMoves.addAll(getMoves(board, myPosition, new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}}, true));
                break;
            case BISHOP:
                possibleMoves.addAll(getMoves(board, myPosition, new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}}, true));
                break;
            case KNIGHT:
                possibleMoves.addAll(getMoves(board, myPosition, new int[][]{{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}}, false));
                break;
            case ROOK:
                possibleMoves.addAll(getMoves(board, myPosition, new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}}, true));
                break;
            case PAWN:
                possibleMoves.addAll(getMovesPawn(board, myPosition));
                break;
            default:
                throw new RuntimeException("Unknown piece type");
        }
        return possibleMoves;
    }

    List<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, int[][] directions, boolean repeat) {
        HashSet<ChessPosition> possiblePositions = new HashSet<>();
        for (var pos : directions) {

            //Column / x left and right
            //Row / y / up and down
            ChessPosition checkingPos = new ChessPosition(myPosition.getRow() + pos[1], myPosition.getColumn() + pos[0]);
            //check for off board and reperting
            if (board.isPositionOffBoard(checkingPos)) {
                continue;
            }
            if (board.getPiece(checkingPos) == null || board.getPiece(checkingPos).pieceColor != pieceColor) {
                possiblePositions.add(checkingPos);
                if (repeat) {
                    for (int i = 1; !board.isPositionOffBoard(checkingPos); i++) {
                        possiblePositions.add(checkingPos);
                        checkingPos = new ChessPosition(myPosition.getRow() + (pos[1] * i), myPosition.getColumn() + (pos[0]) * i);
                        ChessPiece checkingPiece = board.getPiece(checkingPos);
                        if (checkingPiece == null) {
                            continue;
                        } else if (checkingPiece.pieceColor != pieceColor)// if enemy
                        {
                            possiblePositions.add(checkingPos);
                        }
                        break;
                    }
                }
            }
        }


        //convert ChessPosition to ChessMoves
        List<ChessMove> possibleMoves = new ArrayList<>();
        for (var pos : possiblePositions) {
            possibleMoves.add(new ChessMove(myPosition, pos, null));
        }
        return new ArrayList<>(possibleMoves);
    }

    List<ChessMove> getMovesPawn(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        int dir = 0;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            dir = 1;
        } else {
            dir = -1;
        }
        ChessPosition checkingPos = new ChessPosition(myPosition.getRow() + dir, myPosition.getColumn());
        //this may be invalid position,
        if (board.isPositionOffBoard(checkingPos)) {
            System.out.printf("pawn move from %s to %s may be invalid", myPosition.toString(), checkingPos.toString());
            return possibleMoves;
        }
        if (board.getPiece(checkingPos) == null || board.getPiece(checkingPos).pieceColor != pieceColor) {
            PieceType promotionPiece = null;
            possibleMoves.add(new ChessMove(myPosition, checkingPos, promotionPiece));
            //if pawn has not moved yet, check the next square too
            if ((myPosition.getRow() == 2 && pieceColor == ChessGame.TeamColor.WHITE)
                    || (myPosition.getRow() == 7 && pieceColor == ChessGame.TeamColor.BLACK)) {

                checkingPos = new ChessPosition(myPosition.getRow() + dir * 2, myPosition.getColumn());
                if (board.getPiece(checkingPos) == null || board.getPiece(checkingPos).pieceColor != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, checkingPos, promotionPiece));
                }

            }
        }
        return possibleMoves;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int hashCode() {
        return (pieceColor.hashCode() * 7) + (type.hashCode() * 13);
    }
}
