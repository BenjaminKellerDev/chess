package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTests extends BaseDAOTests
{
    //   void clear();
    @Test
    public void clearAll()
    {
        String testAuthToken = generateToken();
        AuthData authData = new AuthData(testAuthToken, "testAuth");
        authDAO.createAuth(authData);
        assertNotNull(authDAO.getAuth(testAuthToken));
        authDAO.clear();
        assertNull(authDAO.getAuth(testAuthToken));
    }

    //    void createAuth(AuthData authData);
    @Test
    public void createAuthTestValid()
    {
        String testAuthToken = generateToken();
        AuthData authData = new AuthData(testAuthToken, "testAuth");
        authDAO.createAuth(authData);
        assertNotNull(authDAO.getAuth(testAuthToken));
    }

    @Test
    public void duplicateAuthTest()
    {
        String testAuthToken = generateToken();
        AuthData authData = new AuthData(testAuthToken, "testAuth");
        authDAO.createAuth(authData);
        assertNotNull(authDAO.getAuth(testAuthToken));
        authDAO.createAuth(authData);
    }
    //    AuthData getAuth(String authToken);

    //    List<AuthData> getAuthByUsername(String username);

    //    void deleteAuth(AuthData authData);


    private static String generateToken()
    {
        return UUID.randomUUID().toString();
    }
}
