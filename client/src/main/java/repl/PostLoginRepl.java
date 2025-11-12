package repl;

import dataaccess.DataAccessException;
import datamodel.LogoutRequest;
import model.AuthData;
import serverFacade.ServerFacade;

import java.util.Arrays;

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
            case "logout", "l" -> logout();
            case "help", "h" -> help();
            //case "login", "l" -> login(params);
            //case "register", "r" -> register(params);
            default -> "Invalid command, try command \"help\"\n" + getAwaitUserInputText();
        };
    }

    private String logout() throws DataAccessException {
        facade.logout(new LogoutRequest(myAuthData.authToken()));
        return getEscapePhrase();
    }

    private String help() {
        return SET_TEXT_COLOR_WHITE + """
                To quit                -- "quit", "q"
                To login               -- "login", "l" <username> <password>
                To register new user   -- "register", "r" <username> <password> <email>
                To display this menu   -- "help", "h"
                """ + getAwaitUserInputText();
    }
}
