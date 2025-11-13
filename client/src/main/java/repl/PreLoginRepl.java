package repl;

import serverAccess.ServerAccessException;
import datamodel.LoginRequest;
import model.AuthData;
import model.UserData;
import serverFacade.ServerFacade;

import static ui.EscapeSequences.*;

import java.util.Arrays;


public class PreLoginRepl extends Repl {

    private static ServerFacade facade;
    private final String serverURL;

    public PreLoginRepl(String serverURL) {
        this.serverURL = serverURL;
        facade = new ServerFacade(serverURL);
    }


    @Override
    protected String getAwaitUserInputText() {
        return SET_TEXT_COLOR_LIGHT_GREY + SET_TEXT_FAINT
                + "Chess Login >>>" + RESET_TEXT_BOLD_FAINT + SET_TEXT_COLOR_WHITE;
    }

    @Override
    protected String getFirstMessageText() {
        return "♕ 240 Chess Client:\n" + getAwaitUserInputText();
    }

    @Override
    protected String getEscapePhrase() {
        return SET_TEXT_BLINKING + "quiting...";
    }


    @Override
    public String eval(String input) throws ServerAccessException {
        String[] tokens = input.toLowerCase().split(" ");
        String command = tokens[0];
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (command) {
            case "quit", "q" -> getEscapePhrase();
            case "help", "h" -> help();
            case "login", "l" -> login(params);
            case "register", "r" -> register(params);
            default -> "Invalid command, try command \"help\"\n" + getAwaitUserInputText();
        };
    }

    private String help() {
        return SET_TEXT_COLOR_WHITE + """
                To quit                -- "quit", "q"
                To login               -- "login", "l" <username> <password>
                To register new user   -- "register", "r" <username> <password> <email>
                To display this menu   -- "help", "h"
                """ + getAwaitUserInputText();
    }

    private String login(String[] params) throws ServerAccessException {
        if (params.length != 2)
            throw new ServerAccessException("Invalid");
        AuthData authData = facade.login(new LoginRequest(params[0], params[1]));
        new PostLoginRepl(serverURL, authData).run();
        return getAwaitUserInputText();
    }

    private String register(String[] params) throws ServerAccessException {
        if (params.length != 3)
            throw new ServerAccessException("Invalid");
        AuthData authData = facade.register(new UserData(params[0], params[1], params[2]));
        new PostLoginRepl(serverURL, authData).run();
        return getAwaitUserInputText();
    }


}
