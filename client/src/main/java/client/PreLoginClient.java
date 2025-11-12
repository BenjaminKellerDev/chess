package client;

import dataaccess.DataAccessException;
import datamodel.LoginRequest;
import model.AuthData;
import serverFacade.ServerFacade;
import ui.EscapeSequences;

import java.util.Arrays;

public class PreLoginClient implements Client {
    public static ServerFacade facade;

    public PreLoginClient(String serverURL) {
        facade = new ServerFacade(serverURL);
    }

    @Override
    public String eval(String input) {
        String[] tokens = input.toLowerCase().split(" ");
        String command = tokens[0];
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (command) {
            case "quit", "q" -> "quit";
            case "help", "h" -> help();
            case "login", "l" -> login(params);
            case "register", "r" -> register(params);
            default -> "Invalid command, try command \"help\"";
        };
    }

    private static String help() {
        return EscapeSequences.SET_TEXT_COLOR_WHITE + """
                To quit                -- "quit", "q"
                To login               -- "login", "l" <username> <password>
                To register new user   -- "register", "r" <username> <password> <email>
                To display this menu   -- "help", "h"
                Chess Login >>>""";
    }

    private static String login(String[] params) {
        try {
            AuthData authData = facade.login(new LoginRequest(params[0], params[1]));

        } catch (DataAccessException e) {
            return EscapeSequences.SET_TEXT_COLOR_RED + """
                    Invalid
                    Chess Login >>>""";
        }
        return EscapeSequences.SET_TEXT_COLOR_BLUE + "";
    }

    private static String register(String[] params) {
        return EscapeSequences.SET_TEXT_COLOR_BLUE + "";
    }
}
