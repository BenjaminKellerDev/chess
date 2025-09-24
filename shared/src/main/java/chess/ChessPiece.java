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
public class ChessPiece
{
    private final ChessGame.TeamColor pieceColor;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type)
    {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType
    {
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
    public ChessGame.TeamColor getTeamColor()
    {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType()
    {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        List<ChessMove> possibleMoves = new ArrayList<>();
        switch (type)
        {
            case KING:
                int[][] kingDirections = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}};
                possibleMoves.addAll(getMoves(board, myPosition, kingDirections, false));
                break;
            case QUEEN:
                int[][] queenDrections = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}};
                possibleMoves.addAll(getMoves(board, myPosition, queenDrections, true));
                break;
            case BISHOP:
                int[][] bishopDrections = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
                possibleMoves.addAll(getMoves(board, myPosition, bishopDrections, true));
                break;
            case KNIGHT:
                int[][] knightDrections = new int[][]{{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}};
                possibleMoves.addAll(getMoves(board, myPosition, knightDrections, false));
                break;
            case ROOK:
                int[][] rookDrections = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
                possibleMoves.addAll(getMoves(board, myPosition, rookDrections, true));
                break;
            case PAWN:
                possibleMoves.addAll(getMovesPawn(board, myPosition));
                break;
            default:
                throw new RuntimeException("Unknown piece type");
        }
        return possibleMoves;
    }

    List<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, int[][] directions, boolean repeat)
    {
        HashSet<ChessPosition> possiblePositions = new HashSet<>();
        for (var pos : directions)
        {

            //Column / x left and right
            //Row / y / up and down
            ChessPosition checkingPos = new ChessPosition(myPosition.getRow() + pos[1], myPosition.getColumn() + pos[0]);
            //check for off board and reperting
            if (board.isPositionOffBoard(checkingPos))
            {
                continue;
            }
            if (board.getPiece(checkingPos) == null || board.getPiece(checkingPos).pieceColor != pieceColor)
            {
                possiblePositions.add(checkingPos);
                for (int i = 1; !board.isPositionOffBoard(checkingPos) && repeat; i++)
                {
                    possiblePositions.add(checkingPos);
                    checkingPos = new ChessPosition(myPosition.getRow() + (pos[1] * i), myPosition.getColumn() + (pos[0]) * i);
                    ChessPiece checkingPiece = board.getPiece(checkingPos);
                    if (checkingPiece == null)
                    {
                        continue;
                    }
                    else if (checkingPiece.pieceColor != pieceColor)// if enemy
                    {
                        possiblePositions.add(checkingPos);
                    }
                    break;
                }

            }
        }


        //convert ChessPosition to ChessMoves
        List<ChessMove> possibleMoves = new ArrayList<>();
        for (var pos : possiblePositions)
        {
            possibleMoves.add(new ChessMove(myPosition, pos, null));
        }
        return new ArrayList<>(possibleMoves);
    }

    List<ChessMove> getMovesPawn(ChessBoard board, ChessPosition myPosition)
    {
        List<ChessMove> possibleMoves = new ArrayList<>();
        int dir = 0;
        if (pieceColor == ChessGame.TeamColor.WHITE)
        {
            dir = 1;
        }
        else
        {
            dir = -1;
        }

        //move forward
        ChessPosition checkingPos = new ChessPosition(myPosition.getRow() + dir, myPosition.getColumn());
        //this may be invalid position,
        if (board.isPositionOffBoard(checkingPos))
        {
            System.out.printf("pawn move from %s to %s may be invalid", myPosition, checkingPos);
            return possibleMoves;
        }
        // set promotion piece if close
        boolean canPromote = false;
        if (checkingPos.getRow() == 8 && pieceColor == ChessGame.TeamColor.WHITE
                || checkingPos.getRow() == 1 && pieceColor == ChessGame.TeamColor.BLACK)
        {
            canPromote = true;
        }

        //check for validity of potential moves
        if (board.getPiece(checkingPos) == null)
        {
            if (!canPromote)
            {
                possibleMoves.add(new ChessMove(myPosition, checkingPos, null));
            }
            else
            {
                possibleMoves.add(new ChessMove(myPosition, checkingPos, PieceType.QUEEN));
                possibleMoves.add(new ChessMove(myPosition, checkingPos, PieceType.ROOK));
                possibleMoves.add(new ChessMove(myPosition, checkingPos, PieceType.BISHOP));
                possibleMoves.add(new ChessMove(myPosition, checkingPos, PieceType.KNIGHT));
            }

            //if pawn has not moved yet, check the next square too
            if ((myPosition.getRow() == 2 && pieceColor == ChessGame.TeamColor.WHITE)
                    || (myPosition.getRow() == 7 && pieceColor == ChessGame.TeamColor.BLACK))
            {
                checkingPos = new ChessPosition(myPosition.getRow() + dir * 2, myPosition.getColumn());
                if (board.getPiece(checkingPos) == null)
                {
                    possibleMoves.add(new ChessMove(myPosition, checkingPos, null));
                }

            }
        }

        //capture
        canPromote = false; //re-initialize
        if (checkingPos.getRow() == 8 && pieceColor == ChessGame.TeamColor.WHITE
                || checkingPos.getRow() == 1 && pieceColor == ChessGame.TeamColor.BLACK)
        {
            canPromote = true;
        }

        ChessPosition checkingCaptureA = new ChessPosition(myPosition.getRow() + dir, myPosition.getColumn() - 1);
        if (!board.isPositionOffBoard(checkingCaptureA))
        {
            if (board.getPiece(checkingCaptureA) != null && board.getPiece(checkingCaptureA).pieceColor != pieceColor)
            {
                if (!canPromote)
                {
                    possibleMoves.add(new ChessMove(myPosition, checkingCaptureA, null));
                }
                else
                {
                    possibleMoves.add(new ChessMove(myPosition, checkingCaptureA, PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, checkingCaptureA, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, checkingCaptureA, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, checkingCaptureA, PieceType.KNIGHT));
                }
            }
        }
        ChessPosition checkingCaptureB = new ChessPosition(myPosition.getRow() + dir, myPosition.getColumn() + 1);
        if (!board.isPositionOffBoard(checkingCaptureB))
        {
            if (board.getPiece(checkingCaptureB) != null && board.getPiece(checkingCaptureB).pieceColor != pieceColor)
            {
                if (!canPromote)
                {
                    possibleMoves.add(new ChessMove(myPosition, checkingCaptureB, null));
                }
                else
                {
                    possibleMoves.add(new ChessMove(myPosition, checkingCaptureB, PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, checkingCaptureB, PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, checkingCaptureB, PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, checkingCaptureB, PieceType.KNIGHT));
                }
            }
        }
        return possibleMoves;
    }


    @Override
    public String toString()
    {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        //if memory address is the same
        if (this == obj)
        {
            return true;
        }
        //if it's a different class
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        //check for reals
        ChessPiece that = (ChessPiece) obj;
        if (that.type == this.type && that.pieceColor == this.pieceColor)
        {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return (pieceColor.ordinal() * 7) + (type.ordinal() * 13);
    }
}
