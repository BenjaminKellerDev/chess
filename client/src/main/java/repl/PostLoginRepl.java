package repl;

import dataaccess.DataAccessException;
import datamodel.CreateGameRequest;
import datamodel.LogoutRequest;
import model.AuthData;
import model.GameData;
import serverFacade.ServerFacade;

import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.*;

public class PostLoginRepl extends Repl {
    private static ServerFacade facade;
    private AuthData myAuthData;

    public PostLoginRepl(String serverURL, AuthData authData) {
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
                To create a new game        -- "create", "c"
                To join a game              -- "join", "j"
                To spectate a game          -- "observe", "o"
                To display this menu        -- "help", "h"
                """ + getAwaitUserInputText();
    }

    private String logout() throws DataAccessException {
        facade.logout(new LogoutRequest(myAuthData.authToken()));
        return getEscapePhrase();
    }


    private String createGame(String[] params) throws DataAccessException {
        if (params.length < 1)
            throw new DataAccessException("Invalid");
        facade.createGame(new CreateGameRequest(params[0]), myAuthData.authToken());
        return getAwaitUserInputText();
    }

    private String listGames() throws DataAccessException {
        List<GameData> games = facade.listGames(myAuthData.authToken());
        StringBuilder sb = new StringBuilder("Games:\n");
        for (int i = 0; i < games.size(); i++) {
            GameData g = games.get(i);
            String w = "available";
            String b = "available";
            if (g.whiteUsername() != null)
                w = g.whiteUsername();
            if (g.blackUsername() != null)
                b = g.blackUsername();
            sb.append(String.format("%d: %s, White: %s, Black: %s%n", i, g.gameName(), w, b));
        }
        return sb.toString();
    }

    private String joinGame(String[] params) throws DataAccessException {
        if (params.length < 2)
            throw new DataAccessException("Invalid");
        return "";
    }

    private String observeGame(String[] params) throws DataAccessException {
        if (params.length < 2)
            throw new DataAccessException("Invalid");
        return "";
    }


}
