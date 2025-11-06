package client;

import dataaccess.DataAccessException;
import datamodel.LoginRequest;
import datamodel.LogoutRequest;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void dropDatabase() throws Exception {
        facade.dropDatabase();
    }

    @Test
    void dropDatabaseTest() throws Exception {
        AuthData authData = registerTestUser();
        assertDoesNotThrow(() -> facade.login(new LoginRequest(testUser.username(), testUser.password())));
        facade.dropDatabase();
        assertThrows(DataAccessException.class, () -> facade.login(new LoginRequest(testUser.username(), testUser.password())));
    }

    @Test
    void dropDatabaseTestNegative() throws Exception {
        facade.dropDatabase();
        assertThrows(DataAccessException.class, () -> facade.login(new LoginRequest(testUser.username(), testUser.password())));
    }

    @Test
    void register() throws Exception {
        AuthData authData = registerTestUser();
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerTwice() throws Exception {
        registerTestUser();
        assertThrows(DataAccessException.class, () -> registerTestUser());
    }

    @Test
    void loginGood() throws Exception {
        registerTestUser();
        AuthData authData = facade.login(new LoginRequest(testUser.username(), testUser.password()));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void loginBad() throws Exception {
        registerTestUser();
        assertThrows(DataAccessException.class, () -> facade.login(new LoginRequest(testUser2.username(), testUser2.password())));
    }

    @Test
    void logoutGood() throws Exception {
        AuthData authData = registerTestUser();
        assertDoesNotThrow(() -> facade.logout(new LogoutRequest(authData.authToken())));
    }

    @Test
    void logoutBad() throws Exception {
        registerTestUser();
        assertThrows(DataAccessException.class, () -> facade.logout(new LogoutRequest("bad auth token sajkdfhsjhdf")));
    }

    //helper functions and vars
    private static UserData testUser = new UserData("player1", "password", "p1@email.com");
    private static UserData testUser2 = new UserData("player2", "password2", "p2@email.com");

    AuthData registerTestUser() throws Exception {
        return facade.register(testUser.username(), testUser.password(), testUser.email());
    }

}
