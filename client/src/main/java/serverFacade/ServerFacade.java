package serverFacade;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import datamodel.*;
import model.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.*;
import java.util.List;

public class ServerFacade {
    private HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private static final Gson SERIALIZER = new Gson();

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
    }

    public void dropDatabase() throws DataAccessException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/db"))
                    .DELETE()
                    .build();
            send(client, request, null);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Bad URI syntax: " + e);
        }
    }

    public AuthData register(UserData registerRequest) throws DataAccessException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/user"))
                    .POST(HttpRequest.BodyPublishers.ofString(SERIALIZER.toJson(registerRequest)))
                    .build();
            return send(client, request, AuthData.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Bad URI syntax: " + e);
        }
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(serverUrl + "/session"))
                    .POST(HttpRequest.BodyPublishers.ofString(SERIALIZER.toJson(loginRequest)))
                    .build();
            return send(client, request, AuthData.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Bad URI syntax: " + e);
        }
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {

    }

    public int createGame(CreateGameRequest createGameRequest, String authToken) throws DataAccessException {
        return 0;
    }

    public List<GameData> listGames(String authorizationToken) throws DataAccessException {
        return null;
    }

    public int joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        return 0;
    }

    private static <T> T send(HttpClient client, HttpRequest request, Class<T> returnType) throws DataAccessException {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200)
                throw new DataAccessException("Server facade HttpSned error code: " + response.statusCode() + ": " + response.body());
            else if (returnType != null) {
                T returnObj = SERIALIZER.fromJson(response.body(), returnType);
                return returnObj;
            }
        } catch (Exception e) {
            throw new DataAccessException("Server facade HttpSned: " + e.getMessage());
        }
        return null;
    }
}
