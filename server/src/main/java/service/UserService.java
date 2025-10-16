package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import datamodel.*;
import model.*;

import java.util.UUID;

public class UserService
{
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO)
    {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData registerRequest) throws DataAccessException
    {
        userDAO.createUser(registerRequest);
        AuthData authData = addAuth(registerRequest.username());
        return authData;
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException
    {
        UserData user = userDAO.getUser(loginRequest.username());
        if (user.username().equals(loginRequest.username()) && user.password().equals(loginRequest.password()))
        {
            AuthData authData = addAuth(loginRequest.username());
            authDAO.createAuth(authData);
            return authData;
        }
        else
        {
            throw new DataAccessException("Incorrect login info");
        }
    }

    public void logout(LogoutRequest logoutRequest)
    {
    }

    private AuthData addAuth(String username)
    {
        AuthData authData = new AuthData(generateToken(), username);
        authDAO.createAuth(authData);
        return authData;
    }

    private static String generateToken()
    {
        return UUID.randomUUID().toString();
    }
}
