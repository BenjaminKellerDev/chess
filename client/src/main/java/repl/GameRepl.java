package repl;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import dataaccess.DataAccessException;
import serverFacade.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class GameRepl extends Repl {
    public static ServerFacade facade;
    private ChessGame.TeamColor teamColor;

    public GameRepl(String serverURL, ChessGame.TeamColor teamColor) {
        facade = new ServerFacade(serverURL);
        this.teamColor = teamColor;
    }

    @Override
    protected String getAwaitUserInputText() {
        return SET_TEXT_COLOR_LIGHT_GREY + SET_TEXT_FAINT
                + "Game >>>" + RESET_TEXT_BOLD_FAINT + SET_TEXT_COLOR_WHITE;
    }

    @Override
    protected String getFirstMessageText() {
        ChessBoard cb = new ChessBoard();
        cb.resetBoard();
        return SET_TEXT_COLOR_GREEN + "Entered Game\n" + buildBoard(cb) + getAwaitUserInputText();
    }

    @Override
    protected String getEscapePhrase() {
        return SET_TEXT_BLINKING + "quiting game...\n";
    }

    @Override
    protected String eval(String input) throws DataAccessException {
        String[] tokens = input.toLowerCase().split(" ");
        String command = tokens[0];
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (command) {
            case "help", "h" -> help();
            case "quit" -> getEscapePhrase();
            default -> "Invalid command, try command \"help\"\n" + getAwaitUserInputText();
        };
    }

    private String help() {
        return SET_TEXT_COLOR_WHITE + """
                To quit                     -- "quit"
                To display this menu        -- "help", "h"
                """ + getAwaitUserInputText();
    }

    public static String buildBoard(ChessBoard chessBoard) {
        StringBuilder sb = new StringBuilder();
        sb.append(SET_TEXT_BOLD);
        boolean white = false;
        for (int i = 9; i >= 0; i--) {
            for (int j = 0; j <= 9; j++) {
                if (i == 0 || i == 9 || j == 0 || j == 9) { //boarder
                    sb.append(SET_BG_COLOR_LIGHT_GREY);
                    if ((j == 0 || j == 9) && i != 0 && i != 9)
                        sb.append(" " + i + " ");
                    else if ((i == 0 || i == 9) && j != 0 && j != 9)
                        sb.append(" " + (char) ('a' + j - 1) + " ");
                    else
                        sb.append(EMPTY);
                } else {
                    if (white) {
                        sb.append(SET_BG_COLOR_WHITE);
                    } else {
                        sb.append(SET_BG_COLOR_BLACK);
                    }
                    white = !white;
                    ChessPiece chessPiece = chessBoard.getPiece(new ChessPosition(i, j));
                    if (chessPiece != null) {
                        if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            sb.append(SET_TEXT_COLOR_RED);
                        } else {
                            sb.append(SET_TEXT_COLOR_BLUE);
                        }
                        switch (chessPiece.getPieceType()) {
                            case PAWN -> sb.append(" P ");
                            case ROOK -> sb.append(" R ");
                            case QUEEN -> sb.append(" Q ");
                            case KNIGHT -> sb.append(" N ");
                            case BISHOP -> sb.append(" B ");
                            case KING -> sb.append(" K ");
                        }
                    } else {
                        sb.append(EMPTY);
                    }
                    sb.append(RESET_TEXT_COLOR);
                }
            }
            sb.append(RESET_BG_COLOR + RESET_TEXT_COLOR + '\n');
            white = !white;
        }
        sb.append(RESET_TEXT_BOLD_FAINT);
        return sb.toString();
    }
}
