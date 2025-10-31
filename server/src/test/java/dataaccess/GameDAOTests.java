package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    //GameData getGame(int gameID);

    //List<GameData> listGames();

    //void updateGame(GameData gameData);
}