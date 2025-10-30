package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

public class BaseServiceTests
{
    static AuthData newAuthData;
    static GameData newGameData;
    static AdminService adminService;
    static UserService userService;
    static GameService gameService;

    static AuthDAO authDAO;
    static GameDAO gameDAO;
    static UserDAO userDAO;

    @BeforeAll
    static void init()
    {
        authDAO = new MySQLAuthDAO();
        gameDAO = new MySQLGameDAO();
        userDAO = new MySQLUserDAO();

        userService = new UserService(userDAO, authDAO);
        adminService = new AdminService(authDAO, gameDAO, userDAO);
        gameService = new GameService(gameDAO, authDAO);
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
        adminService.dropDatabase();
    }
}
