package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import datamodel.*;
import model.GameData;

import java.util.List;

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

    public List<GameData> listGames(String authToken) throws DataAccessException
    {
        return null;
    }

    //game id, perhaps replace with gameData
    public int CreateGame(String authToken, String gameName)
    {
        return -1;
    }
}
