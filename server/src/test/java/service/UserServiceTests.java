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

        assertFalse(authDAO.getAuthByUsername(newUser.username()).isEmpty());
        assertFalse(authDAO.getAuthByUsername(differentUser.username()).isEmpty());
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
        assertFalse(authDAO.getAuthByUsername(newUser.username()).isEmpty());

        assertNull(userDAO.getUser(sameUsername.username()));
        assertFalse(authDAO.getAuthByUsername(sameUsername.username()).isEmpty());

        assertNull(userDAO.getUser(sameEmailUser.username()));
        assertFalse(authDAO.getAuthByUsername(sameEmailUser.username()).isEmpty());
    }

    @Test
    public void loginGood()
    {
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        assertDoesNotThrow(() -> userService.register(newUser));
        LoginRequest loginRequest = new LoginRequest(newUser.username(), newUser.password());
        assertDoesNotThrow(() -> userService.login(loginRequest));

        assertFalse(authDAO.getAuthByUsername(newUser.username()).isEmpty());
    }

    @Test
    public void loginEvil()
    {
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        assertDoesNotThrow(() -> userService.register(newUser));
        LoginRequest loginRequestBadPassword = new LoginRequest(newUser.username(), "NotThePassword");
        LoginRequest loginRequestBadUsername = new LoginRequest("NotTheUsername", newUser.password());
        LoginRequest loginRequestBadCreds = new LoginRequest("Uncle Bob", "ReadMyBook");

        assertThrows(DataAccessException.class, () -> userService.login(loginRequestBadPassword));
        assertThrows(DataAccessException.class, () -> userService.login(loginRequestBadUsername));
        assertThrows(DataAccessException.class, () -> userService.login(loginRequestBadCreds));

        //only 1 auth due to loginRequest
        assertTrue(authDAO.getAuthByUsername(newUser.username()).size() == 1);
    }

    @Test
    public void logoutGood()
    {
        //register
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        AuthData registerAuthData;
        registerAuthData = assertDoesNotThrow(() -> userService.register(newUser));
        LogoutRequest logoutRequest = new LogoutRequest(registerAuthData.authToken());

        //logout from register
        assertDoesNotThrow(() -> userService.logout(logoutRequest));
        assertTrue(authDAO.getAuthByUsername(newUser.username()).isEmpty());

        //login back in
        LoginRequest loginRequest = new LoginRequest(newUser.username(), newUser.password());
        AuthData loginAuthData = assertDoesNotThrow(() -> userService.login(loginRequest));
        LogoutRequest logoutRequest2 = new LogoutRequest(loginAuthData.authToken());

        //log back out
        assertDoesNotThrow(() -> userService.logout(logoutRequest2));
        assertTrue(authDAO.getAuthByUsername(newUser.username()).isEmpty());
    }

    @Test
    public void logoutEvil()
    {
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        AuthData registerAuthData;
        registerAuthData = assertDoesNotThrow(() -> userService.register(newUser));
        LogoutRequest invalidLogoutRequest = new LogoutRequest("incorrectAuthToken");

        assertThrows(DataAccessException.class, () -> userService.logout(invalidLogoutRequest));
        assertFalse(authDAO.getAuthByUsername(newUser.username()).isEmpty());
    }
}
