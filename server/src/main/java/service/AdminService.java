package service;

import dataaccess.*;

public class AdminService
{
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public AdminService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO)
    {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public void dropDatabase() throws DataAccessException
    {
        try
        {
            authDAO.clear();
            gameDAO.clear();
            userDAO.clear();
        } catch (ResponseException e)
        {
            throw new DataAccessException(e.getMessage());
        }
    }
}
