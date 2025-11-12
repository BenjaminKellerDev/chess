package repl;

import dataaccess.DataAccessException;
import datamodel.LoginRequest;
import model.AuthData;
import model.UserData;
import serverFacade.ServerFacade;
import ui.EscapeSequences;

import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.Scanner;

public class preLoginRepl {

    private static ServerFacade facade;
    private final String serverURL;
    private final String endOfMessageText = EscapeSequences.SET_TEXT_COLOR_WHITE +
            EscapeSequences.SET_TEXT_FAINT + "Chess Login >>>" + EscapeSequences.RESET_TEXT_BOLD_FAINT;

    public preLoginRepl(String serverURL) {
        this.serverURL = serverURL;
        facade = new ServerFacade(serverURL);
    }

    public void run() {
        System.out.println("♕ 240 Chess Client: ");
        System.out.print("Chess Login >>>");

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals("quit")) {
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(result);
            } catch (DataAccessException e) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid\n"
                        + endOfMessageText);
            }
        }
    }

    public String eval(String input) throws DataAccessException {
        String[] tokens = input.toLowerCase().split(" ");
        String command = tokens[0];
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (command) {
            case "quit", "q" -> "quit";
            case "help", "h" -> help();
            case "login", "l" -> login(params);
            case "register", "r" -> register(params);
            default -> "Invalid command, try command \"help\"\n" + endOfMessageText;
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

    private String login(String[] params) throws DataAccessException {
        if (params.length < 2)
            throw new DataAccessException("Invalid");
        AuthData authData = facade.login(new LoginRequest(params[0], params[1]));
        new PostLoginRepl(serverURL, authData).run();
        return endOfMessageText;
    }

    private String register(String[] params) throws DataAccessException {
        if (params.length < 3)
            throw new DataAccessException("Invalid");
        AuthData authData = facade.register(new UserData(params[0], params[1], params[2]));
        new PostLoginRepl(serverURL, authData).run();
        return endOfMessageText;
    }


}
