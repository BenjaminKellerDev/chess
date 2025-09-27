package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame
{

    private ChessBoard myBoard = new ChessBoard();
    private List<ChessBoard> chessBoardHistory = new ArrayList<>();
    private TeamColor turn = TeamColor.WHITE;

    public ChessGame()
    {
        myBoard.resetBoard();
        chessBoardHistory.add(myBoard);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn()
    {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team)
    {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor
    {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition)
    {
        if (myBoard.getPiece(startPosition) == null)
        {
            return null; //invalid start Position
        }
        Collection<ChessMove> moves = myBoard.getPiece(startPosition).pieceMoves(myBoard, startPosition);
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException
    {
        if (myBoard.getPiece(move.getStartPosition()) == null)
        {
            throw new InvalidMoveException("Invalid move: " + move.toString());
        }
        Collection<ChessPosition> validMoves = extractEndPosFromChessMove(validMoves(move.getStartPosition()));
        if (!validMoves.contains(move.getEndPosition()))
        {
            throw new InvalidMoveException(String.format("Invalid move: %s%n ValidMoves: %s", move.toString(), validMoves(move.getStartPosition()).toString()));
        }
        ChessBoard testBoard =
        if(isInCheck(turn,m))

        myBoard.movePiece(move);
    }

    private Collection<ChessPosition> extractEndPosFromChessMove(Collection<ChessMove> chessMoves)
    {
        Collection<ChessPosition> chessPositions = new HashSet<>();
        for (var move : chessMoves)
        {
            chessPositions.add(move.getEndPosition());
        }
        return chessPositions;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor, ChessBoard board)
    {
        TeamColor oppsiteTeam = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        Collection<ChessPosition> possibleMoves = new HashSet<>();
        for (var piece : board.getAllPositionsOfTeam(oppsiteTeam))
        {
            for (var move : validMoves(piece))
            {
                possibleMoves.add(move.getEndPosition());
            }
        }
        ChessPosition kingPos = board.getKingPos(teamColor);
        for (var pos : possibleMoves)
        {
            if (pos.equals(kingPos))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor)
    {
        return isInCheck(teamColor, myBoard);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor)
    {
        if (!isInCheck(teamColor))
        {
            return false;
        }
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor)
    {
        if (isInCheck(teamColor))
        {
            return false;
        }
        for (var piece : myBoard.getAllPositionsOfTeam(teamColor))
        {
            if (validMoves(piece).size() != 0)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board)
    {
        myBoard = board;
        chessBoardHistory.add(myBoard);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard()
    {
        return myBoard;
    }

    @Override
    public int hashCode()
    {
        return turn.ordinal() * 3 + myBoard.hashCode() * 13;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        ChessGame that = (ChessGame) obj;
        if (that.myBoard.equals(this.myBoard)
                && that.turn == this.turn)
        {
            return true;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return String.format("Turn: %s %n %s", turn.toString(), myBoard.toString());
    }
}
