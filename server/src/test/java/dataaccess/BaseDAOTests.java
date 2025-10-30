package dataaccess;

import org.junit.jupiter.api.*;

public class BaseDAOTests
{
    static AuthDAO authDAO;
    static GameDAO gameDAO;
    static UserDAO userDAO;

    @BeforeAll
    static void init()
    {
        authDAO = new MySQLAuthDAO();
        gameDAO = new MySQLGameDAO();
        userDAO = new MySQLUserDAO();
    }

    @AfterEach
    void resetState() throws DataAccessException
    {
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
    }
}
