package repl;

import serverFacade.ServerFacade;

public class GameRepl {
    public static ServerFacade facade;

    public GameRepl(String serverURL) {
        facade = new ServerFacade(serverURL);
    }

    public String eval(String input) {
        return "";
    }
}
