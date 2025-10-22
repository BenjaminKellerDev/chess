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

    private int nextGameID = 0;

    public GameService(GameDAO gameDAO, AuthDAO authDAO)
    {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }


    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException
    {
        AuthData requestingAuthData = authDAO.getAuth(joinGameRequest.authToken());
        if (requestingAuthData == null)
        {
            throw new DataAccessException("join game unauthorized");
        }
        GameData oldGameData = gameDAO.getGame(joinGameRequest.gameID());
        if (oldGameData == null)
        {
            throw new DataAccessException("game doesn't exist");
        }
        GameData newGameData;
        if (joinGameRequest.playerColor() == ChessGame.TeamColor.WHITE)
        {
            if (oldGameData.whiteUsername() != "")
            {
                throw new DataAccessException("White team already take");
            }
            newGameData = oldGameData.updateWhiteUsername(requestingAuthData.username());
        }
        else
        {
            if (oldGameData.blackUsername() != "")
            {
                throw new DataAccessException("Black team already take");
            }
            newGameData = oldGameData.updateBlackUsername(requestingAuthData.username());
        }
        gameDAO.updateGame(newGameData);
    }

    public List<GameData> listGames(String authToken) throws DataAccessException
    {
        if (authDAO.getAuth(authToken) == null)
        {
            throw new DataAccessException("join game unauthorized");
        }
        return gameDAO.listGames();
    }

    //game id, perhaps replace with gameData
    public int CreateGame(String authToken, String gameName) throws DataAccessException
    {
        if (authDAO.getAuth(authToken) == null)
        {
            throw new DataAccessException("join game unauthorized");
        }
        gameDAO.createGame(new GameData(nextGameID, gameName));
        nextGameID++;
        return nextGameID - 1;
    }
}
