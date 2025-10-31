package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTests extends BaseDAOTests
{
    @Test
    public void clearTest()
    {
        UserData newUser = new UserData("newUsername", "super cool password", "email@example.com");
        userDAO.createUser(newUser);
        assertNotNull(userDAO.getUser(newUser.username()));
        userDAO.clear();
        assertNull(userDAO.getUser(newUser.username()));
    }

    //void createUser(UserData userData);
    @Test
    public void createUserTest()
    {
        UserData newUser = new UserData("newUsername", "super cool password", "email@example.com");
        assertNull(userDAO.getUser(newUser.username()));
        userDAO.createUser(newUser);
        assertNotNull(userDAO.getUser(newUser.username()));
    }

    @Test
    public void duplicateUsername()
    {
        UserData newUser = new UserData("newUsername", "super cool password", "email@example.com");
        UserData sameUsername = new UserData("newUsername", "different password", "diffrent@example.com");
        userDAO.createUser(newUser);
        assertEquals(newUser.email(), userDAO.getUser(newUser.username()).email());
        userDAO.createUser(sameUsername);
        assertEquals(newUser.email(), userDAO.getUser(newUser.username()).email());
    }

    //UserData getUser(String username);
    @Test
    public void getUserTest()
    {
        UserData newUser = new UserData("newUsername", "super cool password", "email@example.com");
        UserData diffUser = new UserData("different", "different password", "diffrent@example.com");
        assertNull(userDAO.getUser(newUser.username()));
        userDAO.createUser(newUser);
        assertNotNull(userDAO.getUser(newUser.username()));
        assertNull(userDAO.getUser(diffUser.username()));
    }

    @Test
    public void getUserInvalid()
    {
        UserData newUser = new UserData("newUsername", "super cool password", "email@example.com");
        UserData invalid = new UserData("different", "different password", "diffrent@example.com");
        assertNull(userDAO.getUser(newUser.username()));
        userDAO.createUser(newUser);
        assertNull(userDAO.getUser(invalid.username()));
    }

    //UserData getUserByEmail(String email);
    @Test
    public void getUserByEmailTest()
    {
        UserData newUser = new UserData("newUsername", "super cool password", "email@example.com");
        assertNull(userDAO.getUser(newUser.username()));
        userDAO.createUser(newUser);
        assertNotNull(userDAO.getUserByEmail(newUser.email()));
    }

    @Test
    public void getUserByEmailInvalid()
    {
        UserData newUser = new UserData("newUsername", "super cool password", "email@example.com");
        UserData invalid = new UserData("different", "different password", "diffrent@example.com");
        assertNull(userDAO.getUser(newUser.username()));
        userDAO.createUser(newUser);
        assertNull(userDAO.getUserByEmail(invalid.email()));
    }
}