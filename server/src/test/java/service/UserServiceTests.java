package service;


import chess.ChessGame;
import model.*;
import dataaccess.*;
import datamodel.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests
{
    private static AuthData newAuthData;
    private static GameData newGameData;
    private static AdminService adminService;
    private static UserService userService;
    private static GameService gameService;

    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;

    @BeforeAll
    static void init()
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

    @AfterEach
    void resetState() throws DataAccessException
    {
        adminService.dropDatbase();
    }

    @Test
    public void registerGood()
    {
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        UserData differentUser = new UserData("diffrentUser", "diffrentUserPassword", "dif@mail.com");

        assertDoesNotThrow(() -> userService.register(newUser));
        assertDoesNotThrow(() -> userService.register(differentUser));

        assertNotNull(userDAO.getUser(newUser.username()));
        assertNotNull(userDAO.getUser(differentUser.username()));

        assertNotNull(authDAO.getAuthByUsername(newUser.username()));
        assertNotNull(authDAO.getAuthByUsername(differentUser.username()));
    }

    @Test
    public void cantRegisterTwice()
    {
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");

        assertDoesNotThrow(() -> userService.register(newUser));
        assertThrows(DataAccessException.class, () -> userService.register(newUser));

        assertNotNull(userDAO.getUser(newUser.username()));
        assertNotNull(authDAO.getAuthByUsername(newUser.username()));
    }

    @Test
    public void duplicateData()
    {
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        UserData sameUsername = new UserData("NewUser", "diffrentUserPassword", "dif@mail.com");
        UserData sameEmailUser = new UserData("diffUser", "moreDiffrentUserPassword", "nu@mail.com");

        assertDoesNotThrow(() -> userService.register(newUser));
        assertThrows(DataAccessException.class, () -> userService.register(sameUsername));
        assertThrows(DataAccessException.class, () -> userService.register(sameEmailUser));

        assertNotNull(userDAO.getUser(newUser.username()));
        assertNotNull(authDAO.getAuthByUsername(newUser.username()));

        assertNull(userDAO.getUser(sameUsername.username()));
        assertNull(authDAO.getAuthByUsername(sameUsername.username()));

        assertNull(userDAO.getUser(sameEmailUser.username()));
        assertNull(authDAO.getAuthByUsername(sameEmailUser.username()));
    }

    @Test
    public void loginGood()
    {

    }

    @Test
    public void loginEvil()
    {

    }

    @Test
    public void logoutGood()
    {

    }

    @Test
    public void logoutEvil()
    {

    }
}
