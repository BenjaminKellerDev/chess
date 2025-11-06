package serverFacade;

import datamodel.*;
import model.*;

import java.util.List;

public class ServerFacade {
    public ServerFacade(int port) {
    }

    public void dropDatabase() {

    }

    public AuthData register(String player1, String password, String mail) {
        return null;
    }

    public AuthData login(LoginRequest loginRequest) {
        return null;
    }

    public void logout(LogoutRequest logoutRequest) {

    }

    public List<GameData> listGames(String authorizationToken) {
        return null;
    }

    public int joinGame(JoinGameRequest joinGameRequest) {
        return 0;
    }
}
