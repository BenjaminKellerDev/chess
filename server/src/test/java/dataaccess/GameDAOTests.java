package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTests extends BaseDAOTests
{
    @Test
    public void clearTest()
    {
        UserData newUser = new UserData("newUsername", "super cool password", "email@example.com");
        UserData diffUser = new UserData("diffUser", "cool password", "diffrent@example.com");
        GameData gameData = new GameData(1, "newUsername", "diffUser", "NEW GAME", new ChessGame());
        userDAO.createUser(newUser);
        userDAO.createUser(diffUser);
        gameDAO.createGame(gameData);
        assertNotNull(gameDAO.getGame(1));
        gameDAO.clear();
        assertNull(gameDAO.getGame(1));
    }

    //void createGame(GameData gameData);
    @Test
    public void createGameTest()
    {
        GameData gameData = new GameData(1, "newUsername", "diffUser", "NEW GAME", new ChessGame());
        assertNull(gameDAO.getGame(1));
        gameDAO.createGame(gameData);
        assertNotNull(gameDAO.getGame(1));
    }

    @Test
    public void createGameInvalid()
    {
        GameData gameData = new GameData(-1, null, null, null, null);
        assertNull(gameDAO.getGame(1));
        gameDAO.createGame(gameData);
        assertNull(gameDAO.getGame(1));
    }

    //GameData getGame(int gameID);
    @Test
    public void getGameValid()
    {
        GameData gameData = new GameData(1, "newUsername", "diffUser", "NEW GAME", new ChessGame());
        gameDAO.createGame(gameData);
        assertEquals("NEW GAME", gameDAO.getGame(1).gameName());
    }

    @Test
    public void getGameInvalid()
    {
        GameData gameData = new GameData(1, "newUsername", "diffUser", "NEW GAME", new ChessGame());
        assertNull(gameDAO.getGame(1));
        gameDAO.createGame(gameData);
        assertNotNull(gameDAO.getGame(1));
        assertNull(gameDAO.getGame(100));
    }

    //List<GameData> listGames();

    //void updateGame(GameData gameData);
    @Test
    public void updateGameTest()
    {
        GameData gameData = new GameData(1, "newUsername", null, "NEW GAME", new ChessGame());
        gameDAO.createGame(gameData);
        assertEquals(gameData.blackUsername(), gameDAO.getGame(1).blackUsername());
        GameData updatedGameData = gameData.updateBlackUsername("diffUser");
        gameDAO.updateGame(updatedGameData);
        assertEquals(updatedGameData.blackUsername(), gameDAO.getGame(1).blackUsername());
    }

    @Test
    public void updateGameInvalid()
    {
        GameData gameData = new GameData(1, "newUsername", null, "NEW GAME", new ChessGame());
        gameDAO.createGame(gameData);
        assertEquals(gameData.blackUsername(), gameDAO.getGame(1).blackUsername());
        StringBuilder nameTooLong = new StringBuilder();
        for (int i = 0; i < 300; i++)
        {
            nameTooLong.append('a');
        }
        GameData updatedGameData = gameData.updateBlackUsername(nameTooLong.toString());
        gameDAO.updateGame(updatedGameData);
        assertEquals(gameData.blackUsername(), gameDAO.getGame(1).blackUsername());
    }
}