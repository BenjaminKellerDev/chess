package service;

import dataaccess.GameDAO;
import datamodel.*;

public class GameService
{

    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO)
    {
        this.gameDAO = gameDAO;
    }

    public void joinGame(JoinGameRequest joinGameRequest)
    {

    }
}
