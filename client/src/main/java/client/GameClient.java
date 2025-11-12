package client;

import serverFacade.ServerFacade;

public class GameClient implements Client {
    public static ServerFacade facade;

    public GameClient(String serverURL) {
        facade = new ServerFacade(serverURL);
    }

    @Override
    public String eval(String input) {
        return "";
    }
}
