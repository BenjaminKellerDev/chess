package service;

import dataaccess.UserDAO;
import datamodel.*;
import model.*;

public class UserService
{
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO)
    {
        this.userDAO = userDAO;
    }

    public RegisterResult register(UserData registerRequest)
    {
        return new RegisterResult("notImplemtned", "na");
    }

    public AuthData login(LoginRequest loginRequest)
    {
    }

    public void logout(LogoutRequest logoutRequest)
    {
    }
}
