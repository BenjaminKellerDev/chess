package repl;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import serveraccess.ServerAccessException;
import serverfacade.ServerFacade;

import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.regex.Pattern;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class GameRepl extends Repl {
    public static ServerFacade facade;
    private ChessGame.TeamColor teamColor;
    ChessGame localCG = new ChessGame();

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
        return SET_TEXT_COLOR_GREEN + "Entered Game\n" + buildBoard();
    }

    @Override
    protected String getEscapePhrase() {
        return SET_TEXT_BLINKING + "leaving game...\n";
    }

    @Override
    protected String eval(String input) throws ServerAccessException {
        String[] tokens = input.toLowerCase().split(" ");
        String command = tokens[0];
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (command) {
            case "help", "h" -> help();
            case "redraw", "r" -> buildBoard();
            case "leave" -> getEscapePhrase();
            case "move", "m" -> makeMove(params);
            case "resign" -> resignConfirm();
            case "highlight", "l" -> highlightSquares(params);
            default -> "Invalid command, try command \"help\"\n" + getAwaitUserInputText();
        };
    }

    private String resignConfirm() {
        //mini REPL for confirmation
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you sure you want to resign? [Y/n]");
        String line = scanner.nextLine();
        if (line.equals("Y")) {
            System.out.print("Resigning...\n");
            resign();
        } else {
            System.out.print("You did not resign, continue the game\n");
        }

        return getAwaitUserInputText();
    }

    private void resign() {
        //websocket here
    }

    private String makeMove(String[] params) throws ServerAccessException {
        if (params.length != 1) {
            throw new ServerAccessException("Invalid parameter count, see help command");
        }
        return "";
    }

    private String highlightSquares(String[] params) throws ServerAccessException {
        if (params.length != 1) {
            throw new ServerAccessException("Invalid parameter count, see help command");
        }
        ChessPosition pos = parsePose(params[0]);
        return buildBoard(pos);
    }

    private String help() {
        return SET_TEXT_COLOR_WHITE + """
                To display this menu        -- "help", "h"
                To redraw the board         -- "redraw", "r"
                To leave game               -- "leave"
                To make a move              -- "move", "m" <move (e.g. b2b4)>
                To resign                   -- "resign"
                To highlight legal moves    -- "highlight", "l" <piece (e.g. b2)>
                """ + getAwaitUserInputText();
    }

    public String buildBoard() {
        return buildBoard(null);
    }

    public String buildBoard(ChessPosition posToHighlight) {
        boolean blackSide;
        if (teamColor == ChessGame.TeamColor.BLACK) {
            blackSide = true;
        } else {
            blackSide = false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(SET_TEXT_BOLD + RESET_TEXT_COLOR);
        boolean white = false;
        if (!blackSide) {
            for (int i = 9; i >= 0; i--) {
                for (int j = 0; j <= 9; j++) {
                    ChessPiece chessPiece = localCG.getBoard().getPiece(new ChessPosition(i, j));
                    white = !white;
                    sb.append(buildBoardHelper(i, j, chessPiece, white, posToHighlight));
                }
                sb.append(RESET_BG_COLOR + RESET_TEXT_COLOR + '\n');
                white = !white;
            }
        } else {
            for (int i = 0; i <= 9; i++) {
                for (int j = 9; j >= 0; j--) {
                    ChessPiece chessPiece = localCG.getBoard().getPiece(new ChessPosition(i, j));
                    white = !white;
                    sb.append(buildBoardHelper(i, j, chessPiece, white, posToHighlight));
                }
                sb.append(RESET_BG_COLOR + RESET_TEXT_COLOR + '\n');
                white = !white;
            }
        }

        sb.append(RESET_TEXT_BOLD_FAINT);
        sb.append(getAwaitUserInputText());
        return sb.toString();
    }

    private String buildBoardHelper(int i, int j, ChessPiece chessPiece, boolean white, ChessPosition posToHighlight) {
        StringBuilder sb = new StringBuilder();
        if (i == 0 || i == 9 || j == 0 || j == 9) { //boarder
            sb.append(SET_BG_COLOR_LIGHT_GREY);
            if ((j == 0 || j == 9) && i != 0 && i != 9) {
                sb.append(" " + i + " ");
            } else if ((i == 0 || i == 9) && j != 0 && j != 9) {
                sb.append(" " + (char) ('a' + j - 1) + " ");
            } else {
                sb.append(EMPTY);
            }
        } else {
            if (white) {
                sb.append(SET_BG_COLOR_WHITE);
            } else {
                sb.append(SET_BG_COLOR_BLACK);
            }
            if (posToHighlight != null) {
                ChessPosition pos = new ChessPosition(i, j);
                if (posToHighlight.equals(pos)) {
                    sb.append(SET_BG_COLOR_YELLOW);
                } else {
                    Collection<ChessMove> moves = localCG.validMoves(posToHighlight);
                    for (ChessMove m : moves) {
                        if (m.getEndPosition().equals(pos)) {
                            if (white) {
                                sb.append(SET_BG_COLOR_GREEN);
                            } else {
                                sb.append(SET_BG_COLOR_DARK_GREEN);
                            }
                            break;
                        }
                    }
                }
            }
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
        return sb.toString();
    }

    private ChessPosition parsePose(String inputText) throws ServerAccessException {
        final String regex = "^[a-h]\\d$";
        if (!Pattern.matches(regex, inputText)) {
            throw new ServerAccessException("Chess position format invalid");
        }
        return new ChessPosition(Character.getNumericValue(inputText.charAt(1)), (int) inputText.charAt(0) - 96);
    }

}
