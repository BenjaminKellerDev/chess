package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import datamodel.*;
import model.*;

import java.util.List;
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
        if (userDAO.getUser(registerRequest.username()) != null)
        {
            throw new DataAccessException("User Already Registered");
        }
        if (userDAO.getUserByEmail(registerRequest.email()) != null)
        {
            throw new DataAccessException("Email Already Registered");
        }
        userDAO.createUser(registerRequest);
        AuthData authData = authorizeUsername(registerRequest.username());
        return authData;
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException
    {
        UserData user = userDAO.getUser(loginRequest.username());
        if (user == null)
        {
            throw new DataAccessException("Username does not exist");
        }
        else if (user.username().equals(loginRequest.username()) && user.password().equals(loginRequest.password()))
        {
            AuthData authData = authorizeUsername(loginRequest.username());
            return authData;
        }
        else
        {
            throw new DataAccessException("Incorrect login info");
        }
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException
    {
        AuthData auth = authDAO.getAuth(logoutRequest.authToken());
        if (auth == null)
        {
            throw new DataAccessException("bad logout auth");
        }
        List<AuthData> allAuths = authDAO.getAuthByUsername(auth.username());
        for (var auths : allAuths)
        {
            authDAO.deleteAuth(auths);
        }
    }

    private AuthData authorizeUsername(String username)
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
