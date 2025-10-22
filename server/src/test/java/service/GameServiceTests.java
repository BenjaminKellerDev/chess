package service;

import chess.ChessGame.TeamColor;
import model.*;
import dataaccess.*;
import datamodel.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class GameServiceTests extends BaseServiceTests
{
    @Test
    public void createGameAuthorized()
    {
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        UserData differentUser = new UserData("differentUser", "differentUserPassword", "dif@mail.com");
        assertDoesNotThrow(() -> userService.register(newUser));
        assertDoesNotThrow(() -> userService.register(differentUser));

        AuthData newUserAuthData = authDAO.getAuthByUsername("NewUser").getFirst();
        AuthData differentUserAuthData = authDAO.getAuthByUsername("differentUser").getFirst();

        int newGameID = assertDoesNotThrow(() -> gameService.createGame(newUserAuthData.authToken(), "MyCoolGame"));
        int secondGameID = assertDoesNotThrow(() -> gameService.createGame(differentUserAuthData.authToken(), "I lost the game"));

        assertTrue(newGameID != secondGameID);
    }

    @Test
    public void createGameInvalid() throws DataAccessException
    {
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        assertDoesNotThrow(() -> userService.register(newUser));

        AuthData newUserAuthData = authDAO.getAuthByUsername("NewUser").getFirst();

        assertThrows(DataAccessException.class, () -> gameService.createGame("Incorect Auth askdjhfaskjehf", "MyCoolGame"));

        //ask professor what is a better way to test this without throws DataAccessException
        assertTrue(gameService.listGames(newUserAuthData.authToken()).isEmpty());
    }

    @Test
    public void joinGameVaid()
    {
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        UserData differentUser = new UserData("differentUser", "differentUserPassword", "dif@mail.com");
        assertDoesNotThrow(() -> userService.register(newUser));
        assertDoesNotThrow(() -> userService.register(differentUser));

        AuthData newUserAuthData = authDAO.getAuthByUsername("NewUser").getFirst();
        AuthData differentUserAuthData = authDAO.getAuthByUsername("differentUser").getFirst();

        int newGameID = assertDoesNotThrow(() -> gameService.createGame(newUserAuthData.authToken(), "MyCoolGame"));

        JoinGameRequest joinGameRequest = new JoinGameRequest(newUserAuthData.authToken(), TeamColor.WHITE, newGameID);
        JoinGameRequest diffrentJoinGameRequest = new JoinGameRequest(differentUserAuthData.authToken(), TeamColor.BLACK, newGameID);

        assertDoesNotThrow(() -> gameService.joinGame(joinGameRequest));
        assertDoesNotThrow(() -> gameService.joinGame(diffrentJoinGameRequest));
    }

    @Test
    public void joinGameInvalid()
    {
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        UserData differentUser = new UserData("differentUser", "differentUserPassword", "dif@mail.com");
        assertDoesNotThrow(() -> userService.register(newUser));
        assertDoesNotThrow(() -> userService.register(differentUser));

        AuthData newUserAuthData = authDAO.getAuthByUsername("NewUser").getFirst();
        AuthData differentUserAuthData = authDAO.getAuthByUsername("differentUser").getFirst();

        int newGameID = assertDoesNotThrow(() -> gameService.createGame(newUserAuthData.authToken(), "MyCoolGame"));

        JoinGameRequest validJoinGameRequest = new JoinGameRequest(newUserAuthData.authToken(), TeamColor.WHITE, newGameID);
        JoinGameRequest wrongColorDiffrentJoinGameRequest = new JoinGameRequest(differentUserAuthData.authToken(), TeamColor.WHITE, newGameID);
        JoinGameRequest wrongGameIDDiffrentJoinGameRequest = new JoinGameRequest(differentUserAuthData.authToken(), TeamColor.WHITE, newGameID + 1);
        JoinGameRequest noAuthJoinGameRequest = new JoinGameRequest("invalidAuthToken kdjglkjdf", TeamColor.WHITE, newGameID);

        assertDoesNotThrow(() -> gameService.joinGame(validJoinGameRequest));
        assertThrows(DataAccessException.class, () -> gameService.joinGame(wrongColorDiffrentJoinGameRequest));
        assertThrows(DataAccessException.class, () -> gameService.joinGame(wrongGameIDDiffrentJoinGameRequest));
        assertThrows(DataAccessException.class, () -> gameService.joinGame(noAuthJoinGameRequest));
    }

    @Test
    public void listGamesValid() throws DataAccessException
    {
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        UserData differentUser = new UserData("differentUser", "differentUserPassword", "dif@mail.com");
        assertDoesNotThrow(() -> userService.register(newUser));
        assertDoesNotThrow(() -> userService.register(differentUser));

        AuthData newUserAuthData = authDAO.getAuthByUsername("NewUser").getFirst();
        AuthData differentUserAuthData = authDAO.getAuthByUsername("differentUser").getFirst();

        int newGameID = gameService.createGame(newUserAuthData.authToken(), "MyCoolGame");
        int secondGameID = gameService.createGame(differentUserAuthData.authToken(), "i lost the game");

        assertTrue(gameService.listGames(newUserAuthData.authToken()).size() == 2);
        assertTrue(newGameID != secondGameID);

        assertTrue(gameService.listGames(newUserAuthData.authToken()).get(0).whiteUsername() == null);
        assertTrue(gameService.listGames(newUserAuthData.authToken()).get(0).blackUsername() == null);

        JoinGameRequest validJoinGameRequest = new JoinGameRequest(newUserAuthData.authToken(), TeamColor.WHITE, newGameID);
        assertDoesNotThrow(() -> gameService.joinGame(validJoinGameRequest));

        assertFalse(gameService.listGames(newUserAuthData.authToken()).get(0).whiteUsername() == null);
        assertTrue(gameService.listGames(newUserAuthData.authToken()).get(0).blackUsername() == null);

        assertTrue(gameService.listGames(newUserAuthData.authToken()).get(0).gameName() == "MyCoolGame");

        assertNotNull(gameService.listGames(newUserAuthData.authToken()).get(0).game());
    }

    @Test
    public void listGamesNoAuth()
    {
        UserData newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
        UserData differentUser = new UserData("differentUser", "differentUserPassword", "dif@mail.com");
        assertDoesNotThrow(() -> userService.register(newUser));
        assertDoesNotThrow(() -> userService.register(differentUser));

        AuthData newUserAuthData = authDAO.getAuthByUsername("NewUser").getFirst();
        AuthData differentUserAuthData = authDAO.getAuthByUsername("differentUser").getFirst();

        int newGameID = assertDoesNotThrow(() -> gameService.createGame(newUserAuthData.authToken(), "MyCoolGame"));
        int secondGameID = assertDoesNotThrow(() -> gameService.createGame(differentUserAuthData.authToken(), "i lost the game"));

        assertThrows(DataAccessException.class, () -> gameService.listGames("invalid auth token adfgdfgsfdh"));

    }
}
