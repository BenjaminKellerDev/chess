package service;

import chess.ChessGame;
import model.*;
import dataaccess.*;
import datamodel.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceTests
{
    private static AdminService adminService;
    //if there's time, refactor to use services to better simulate the overall backend
    private static UserService userService;
    private static GameService gameService;

    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;

    @BeforeAll
    public static void init()
    {
        authDAO = new RAMAuthDAO();
        gameDAO = new RAMGameDAO();
        userDAO = new RAMUserDAO();

        userService = new UserService(userDAO, authDAO);
        adminService = new AdminService(authDAO, gameDAO, userDAO);
        gameService = new GameService(gameDAO);
    }

    @AfterAll
    static void clearTests()
    {
        authDAO = null;
        gameDAO = null;
        userDAO = null;

        userService = null;
        adminService = null;
        gameService = null;
    }

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
        assertNotNull(gameDAO.getGame());

        assertDoesNotThrow(() -> adminService.dropDatbase());

        assertNull(userDAO.getUser(newUser.username()));
        assertNull(authDAO.getAuth(newAuthData.authToken()));
        assertNull(gameDAO.getGame());
    }
}
