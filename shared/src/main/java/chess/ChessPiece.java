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
                possibleMoves.addAll(getMoves(board,myPosition,new int[][]{{1,1},{1,-1},{-1,1},{-1,-1},{1,0},{0,1},{-1,0},{0,-1}},false));
                break;
            case QUEEN:
                possibleMoves.addAll(getMoves(board,myPosition,new int[][]{{1,1},{1,-1},{-1,1},{-1,-1},{1,0},{0,1},{-1,0},{0,-1}},true));
                break;
            case BISHOP:
                possibleMoves.addAll(getMoves(board,myPosition,new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}},true));
                break;
            case KNIGHT:
                possibleMoves.addAll(getMoves(board,myPosition,new int[][]{{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{-1,2},{1,-2},{-1,-2}},false));
                break;
            case ROOK:
                possibleMoves.addAll(getMoves(board,myPosition,new int[][]{{1,0},{0,1},{-1,0},{0,-1}},false));
                break;
            case PAWN:
                possibleMoves.addAll(getMovesPawn(board,myPosition));
                break;
            default:
                throw new RuntimeException("Unknown piece type");
        }
        return possibleMoves;
    }

    List<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, int[][] directions, boolean repeat){
        List<ChessMove> possibleMoves = new ArrayList<>();

        for (var pos : directions)
        {
            //Column / x to the right
            //Row / y
            ChessPosition checkingPos = new ChessPosition(myPosition.getRow() + pos[1], myPosition.getColumn() + pos[0]);
            //check for off board and reperting
            if(board.getPiece(checkingPos) == null)
            {
                possibleMoves.add(new ChessMove(myPosition,checkingPos,null));
            }
        }
        return possibleMoves;
    }

    List<ChessMove> getMovesPawn(ChessBoard board, ChessPosition myPosition){
        List<ChessMove> possibleMoves = new ArrayList<>();

        return possibleMoves;
    }
}
