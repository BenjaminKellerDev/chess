package repl;

import chess.ChessBoard;
import chess.ChessGame;
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

    private String buildBoard(ChessBoard chessBoard) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                sb.append(chessBoard.getPiece(new ChessPosition(i, j)));
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
