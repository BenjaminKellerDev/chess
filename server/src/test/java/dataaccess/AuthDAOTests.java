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
        String username = "testAuth";
        AuthData authData = new AuthData(testAuthToken, username);
        authDAO.createAuth(authData);
        assertEquals(1, authDAO.getAuthByUsername(username).size());
        authDAO.createAuth(authData);
        assertEquals(1, authDAO.getAuthByUsername(username).size());
    }

    //    AuthData getAuth(String authToken);
    @Test
    public void getAuthTestValid()
    {
        AuthData authData = new AuthData(generateToken(), "testAuth");
        AuthData authData2 = new AuthData(generateToken(), "testAuth2");
        authDAO.createAuth(authData);
        authDAO.createAuth(authData2);
        assertNotNull(authDAO.getAuth(authData.authToken()));
        assertNotNull(authDAO.getAuth(authData2.authToken()));
    }

    @Test
    public void getAuthInvalid()
    {
        AuthData authData = new AuthData(generateToken(), "testAuth");
        AuthData authInvalid = new AuthData("invalid Auth 92384j57", "testAuth2");
        authDAO.createAuth(authData);
        assertNotNull(authDAO.getAuth(authData.authToken()));
        assertNull(authDAO.getAuth(authInvalid.authToken()));
    }

    //    List<AuthData> getAuthByUsername(String username);
    @Test
    public void listAuthsValid()
    {
        AuthData authData = new AuthData(generateToken(), "testAuth");
        AuthData authData2 = new AuthData(generateToken(), "testAuth");
        authDAO.createAuth(authData);
        assertEquals(1, authDAO.getAuthByUsername(authData.username()).size());
        authDAO.createAuth(authData2);
        assertEquals(2, authDAO.getAuthByUsername(authData.username()).size());
        authDAO.deleteAuth(authData);
        assertEquals(1, authDAO.getAuthByUsername(authData.username()).size());
    }

    @Test
    public void listAuthsInvalid()
    {
        AuthData authData = new AuthData(generateToken(), "testAuth");
        AuthData authData2 = new AuthData(generateToken(), "testAuth");
        AuthData invalidData = new AuthData("invalid auth token sdjfsds", "invalid");
        authDAO.createAuth(authData);
        authDAO.createAuth(authData2);
        assertEquals(2, authDAO.getAuthByUsername(authData.username()).size());
        assertTrue(authDAO.getAuthByUsername(invalidData.username()).isEmpty());
    }

    //    void deleteAuth(AuthData authData);
    @Test
    public void deleteAuthValid()
    {
        AuthData authData = new AuthData(generateToken(), "testAuth");
        authDAO.createAuth(authData);
        assertNotNull(authDAO.getAuth(authData.authToken()));
        authDAO.deleteAuth(authData);
        assertNull(authDAO.getAuth(authData.authToken()));
    }

    @Test
    public void deleteAuthInvalid()
    {
        AuthData authData = new AuthData(generateToken(), "testAuth");
        AuthData authInvalid = new AuthData("invalid Auth 92384j57", "testAuth2");
        authDAO.createAuth(authData);
        assertNotNull(authDAO.getAuth(authData.authToken()));
        authDAO.deleteAuth(authInvalid);
        assertNotNull(authDAO.getAuth(authData.authToken()));
    }

    private static String generateToken()
    {
        return UUID.randomUUID().toString();
    }
}
