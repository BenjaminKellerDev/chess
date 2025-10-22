package service;

import chess.ChessGame;
import model.*;
import dataaccess.*;
import datamodel.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceTests extends BaseServiceTests
{
    
    @Test
    public void clearDatabaseTest()
    {
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        UserData differentUser = new UserData("differentUser", "differentUserPassword", "dif@mail.com");
        AuthData newAuthData = new AuthData("testAuthTokenkejfgbkejr43", "NewUser");
        GameData newGameData = new GameData(1, "NewUser", "differentUser", "coolGame", new ChessGame());

        userDAO.createUser(newUser);
        authDAO.createAuth(newAuthData);
        gameDAO.createGame(newGameData);

        assertNotNull(userDAO.getUser(newUser.username()));
        assertNotNull(authDAO.getAuth(newAuthData.authToken()));
        assertNotNull(gameDAO.getGame(1));

        assertDoesNotThrow(() -> adminService.dropDatbase());

        assertNull(userDAO.getUser(newUser.username()));
        assertNull(authDAO.getAuth(newAuthData.authToken()));
        assertNull(gameDAO.getGame(1));
    }
}
