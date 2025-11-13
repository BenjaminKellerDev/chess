package repl;

import chess.ChessGame;
import dataaccess.DataAccessException;
import datamodel.CreateGameRequest;
import datamodel.JoinGameRequest;
import datamodel.LogoutRequest;
import model.AuthData;
import model.GameData;
import serverFacade.ServerFacade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ui.EscapeSequences.*;

public class PostLoginRepl extends Repl {
    private static ServerFacade facade;
    private final String serverURL;

    private AuthData myAuthData;
    private Map<String, Integer> listIntToGameID = new HashMap<>();

    public PostLoginRepl(String serverURL, AuthData authData) {
        this.serverURL = serverURL;
        facade = new ServerFacade(serverURL);
        myAuthData = authData;
    }

    @Override
    protected String getAwaitUserInputText() {
        return SET_TEXT_COLOR_LIGHT_GREY + SET_TEXT_FAINT
                + "Chess >>>" + RESET_TEXT_BOLD_FAINT + SET_TEXT_COLOR_WHITE;
    }

    @Override
    protected String getFirstMessageText() {
        return SET_TEXT_COLOR_GREEN + "Success!!\n" + getAwaitUserInputText();
    }

    @Override
    protected String getEscapePhrase() {
        return SET_TEXT_BLINKING + "Logging out...\n";
    }

    @Override
    public String eval(String input) throws DataAccessException {
        String[] tokens = input.toLowerCase().split(" ");
        String command = tokens[0];
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (command) {
            case "help", "h" -> help();
            case "logout" -> logout();
            case "create", "c" -> createGame(params);
            case "list", "l" -> listGames();
            case "join", "j" -> joinGame(params);
            case "observe", "o" -> observeGame(params);
            default -> "Invalid command, try command \"help\"\n" + getAwaitUserInputText();
        };
    }

    private String help() {
        return SET_TEXT_COLOR_WHITE + """
                To logout                   -- "logout"
                To list available games     -- "list", "l"
                To create a new game        -- "create", "c" <game name>
                To join a game              -- "join", "j" <game number> <white/black>
                To spectate a game          -- "observe", "o" <game number>
                To display this menu        -- "help", "h"
                """ + getAwaitUserInputText();
    }

    private String logout() throws DataAccessException {
        facade.logout(new LogoutRequest(myAuthData.authToken()));
        return getEscapePhrase();
    }


    private String createGame(String[] params) throws DataAccessException {
        if (params.length != 1)
            throw new DataAccessException("Invalid");
        facade.createGame(new CreateGameRequest(params[0]), myAuthData.authToken());
        return getAwaitUserInputText();
    }

    private String listGames() throws DataAccessException {
        List<GameData> games = facade.listGames(myAuthData.authToken());
        listIntToGameID.clear();
        StringBuilder sb = new StringBuilder("Games:\n");
        for (int i = 0; i < games.size(); i++) {
            GameData g = games.get(i);
            String w = "available";
            String b = "available";
            if (g.whiteUsername() != null)
                w = g.whiteUsername();
            if (g.blackUsername() != null)
                b = g.blackUsername();
            sb.append(String.format("%d: %s, White: %s, Black: %s%n", i + 1, g.gameName(), w, b));
            listIntToGameID.put(Integer.toString(i + 1), g.gameID());
        }
        sb.append(getAwaitUserInputText());
        return sb.toString();
    }

    private String joinGame(String[] params) throws DataAccessException {
        if (params.length != 2 || !listIntToGameID.containsKey(params[0]))
            throw new DataAccessException("Invalid");
        int gameID = listIntToGameID.get(params[0]);
        if (params[1].toLowerCase().equals("white")) {
            facade.joinGame(new JoinGameRequest(myAuthData.authToken(), ChessGame.TeamColor.WHITE, gameID));
            new GameRepl(serverURL, ChessGame.TeamColor.WHITE).run();
        } else if (params[1].toLowerCase().equals("black")) {
            facade.joinGame(new JoinGameRequest(myAuthData.authToken(), ChessGame.TeamColor.BLACK, gameID));
            new GameRepl(serverURL, ChessGame.TeamColor.BLACK).run();
        } else
            throw new DataAccessException("Invalid");
        return getAwaitUserInputText();
    }

    private String observeGame(String[] params) throws DataAccessException {
        if (params.length != 1)
            throw new DataAccessException("Invalid");

        new GameRepl(serverURL, ChessGame.TeamColor.WHITE).run();
        return getAwaitUserInputText();
    }


}
