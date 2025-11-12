package repl;

import model.AuthData;
import serverFacade.ServerFacade;

public class PostLoginRepl {
    private static ServerFacade facade;
    private AuthData myAuthData;

    public PostLoginRepl(String serverURL, AuthData authData) {
        facade = new ServerFacade(serverURL);
        myAuthData = authData;
    }

    public void run() {

    }

    public String eval(String input) {
        return "";
    }
}
