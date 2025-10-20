package service;

import chess.ChessGame;
import model.*;
import dataaccess.*;
import datamodel.*;
import org.junit.jupiter.api.*;
import passoff.model.TestAuthResult;
import passoff.model.TestCreateRequest;
import passoff.model.TestUser;
import passoff.server.TestServerFacade;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceTests
{
    private static UserData newUser;
    private static AuthData newAuthData;
    private static GameData newGameData;
    private static AdminService adminService;
    private static UserService userService;
    private static GameService gameService;

    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;

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

    @Test
    public void clearDatabaseTest()
    {
        newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        newUser = new UserData("diffrentUser", "diffrentUserPassword", "dif@mail.com");
        newAuthData = new AuthData("testAuthTokenkejfgbkejr43", "NewUser");
        newGameData = new GameData(1, "NewUser", "diffrentUser", "coolGame", new ChessGame());

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
