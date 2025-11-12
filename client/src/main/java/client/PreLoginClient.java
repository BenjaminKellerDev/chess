package client;

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
            case "login", "l" -> login();
            case "register", "r" -> register();
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

    private static String login() {
        return EscapeSequences.SET_TEXT_COLOR_BLUE + "";
    }

    private static String register() {
        return EscapeSequences.SET_TEXT_COLOR_BLUE + "";
    }
}
