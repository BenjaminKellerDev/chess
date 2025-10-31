package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import datamodel.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

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
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null)
        {
            throw new DataAccessException("bad request");
        }
        if (userDAO.getUser(registerRequest.username()) != null)
        {
            throw new DataAccessException("already taken");
        }
        if (userDAO.getUserByEmail(registerRequest.email()) != null)
        {
            throw new DataAccessException("email already taken");
        }
        storeUserPassword(registerRequest);
        AuthData authData = authorizeUsername(registerRequest.username());
        return authData;
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException
    {
        if (loginRequest.username() == null || loginRequest.password() == null)
        {
            throw new DataAccessException("bad request");
        }
        UserData user = userDAO.getUser(loginRequest.username());
        if (user == null)
        {
            throw new DataAccessException("unauthorized");
        }
        else if (user.username().equals(loginRequest.username()) && verifyHashedPassword(user, loginRequest.password()))
        {
            AuthData authData = authorizeUsername(loginRequest.username());
            return authData;
        }
        else
        {
            throw new DataAccessException("unauthorized");
        }
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException
    {
        AuthData auth = authDAO.getAuth(logoutRequest.authToken());
        if (auth == null)
        {
            throw new DataAccessException("unauthorized");
        }
        authDAO.deleteAuth(auth);
    }

    private void storeUserPassword(UserData registerRequestWithClearTextPassword)
    {
        String hashedPassword = BCrypt.hashpw(registerRequestWithClearTextPassword.password(), BCrypt.gensalt());

        UserData updatedPassword = registerRequestWithClearTextPassword.updatePassword(hashedPassword);

        userDAO.createUser(updatedPassword);
    }

    private boolean verifyHashedPassword(UserData databaseUserInfo, String providedClearTextPassword)
    {
        // read the previously hashed password from the database
        var hashedPassword = databaseUserInfo.password();

        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
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
