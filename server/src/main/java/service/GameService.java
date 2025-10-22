package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import datamodel.*;
import model.*;

import java.util.List;

public class GameService
{

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    private int nextGameID = 1;

    public GameService(GameDAO gameDAO, AuthDAO authDAO)
    {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }


    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException
    {
        if (joinGameRequest.playerColor() == null)
        {
            throw new DataAccessException("bad request");
        }
        AuthData requestingAuthData = authDAO.getAuth(joinGameRequest.authToken());
        if (requestingAuthData == null)
        {
            throw new DataAccessException("unauthorized");
        }
        GameData oldGameData = gameDAO.getGame(joinGameRequest.gameID());
        if (oldGameData == null)
        {
            throw new DataAccessException("bad request");
        }
        GameData newGameData;
        if (joinGameRequest.playerColor() == ChessGame.TeamColor.WHITE)
        {
            if (oldGameData.whiteUsername() != null)
            {
                throw new DataAccessException("White team already taken");
            }
            newGameData = oldGameData.updateWhiteUsername(requestingAuthData.username());
        }
        else
        {
            if (oldGameData.blackUsername() != null)
            {
                throw new DataAccessException("Black team already taken");
            }
            newGameData = oldGameData.updateBlackUsername(requestingAuthData.username());
        }
        gameDAO.updateGame(newGameData);
    }

    public List<GameData> listGames(String authToken) throws DataAccessException
    {
        if (authDAO.getAuth(authToken) == null)
        {
            throw new DataAccessException("unauthorized");
        }
        return gameDAO.listGames();
    }

    //game id, perhaps replace with gameData
    public int createGame(String authToken, String gameName) throws DataAccessException
    {
        if (authToken == null || gameName == null)
        {
            throw new DataAccessException("bad request");
        }
        if (authDAO.getAuth(authToken) == null)
        {
            throw new DataAccessException("unauthorized");
        }
        gameDAO.createGame(new GameData(nextGameID, gameName));
        nextGameID++;
        return nextGameID - 1;
    }
}
