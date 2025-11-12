package client;

import serverFacade.ServerFacade;

public class PostLoginClient implements Client {
    public static ServerFacade facade;

    public PostLoginClient(String serverURL) {
        facade = new ServerFacade(serverURL);
    }

    @Override
    public String eval(String input) {
        return "";
    }
}
