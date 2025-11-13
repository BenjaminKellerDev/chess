package serverFacade;

import com.google.gson.Gson;
import serverAccess.ServerAccessException;
import datamodel.*;
import model.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.*;
import java.util.List;

public class ServerFacade {
    private HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private static final Gson SERIALIZER = new Gson();

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void dropDatabase() throws ServerAccessException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uriBuilder("/db"))
                .DELETE()
                .build();
        send(client, request, null);
    }

    public AuthData register(UserData registerRequest) throws ServerAccessException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uriBuilder("/user"))
                .POST(HttpRequest.BodyPublishers.ofString(SERIALIZER.toJson(registerRequest)))
                .build();
        return send(client, request, AuthData.class);
    }

    public AuthData login(LoginRequest loginRequest) throws ServerAccessException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uriBuilder("/session"))
                .POST(HttpRequest.BodyPublishers.ofString(SERIALIZER.toJson(loginRequest)))
                .build();
        return send(client, request, AuthData.class);
    }

    public void logout(LogoutRequest logoutRequest) throws ServerAccessException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uriBuilder("/session"))
                .DELETE()
                .header("Authorization", logoutRequest.authToken())
                .build();
        send(client, request, null);
    }

    public int createGame(CreateGameRequest createGameRequest, String authToken) throws ServerAccessException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uriBuilder("/game"))
                .POST(HttpRequest.BodyPublishers.ofString(SERIALIZER.toJson(createGameRequest)))
                .header("Authorization", authToken)
                .build();
        return send(client, request, CreateGameResponce.class).gameID();
    }

    public List<GameData> listGames(String authToken) throws ServerAccessException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uriBuilder("/game"))
                .GET()
                .header("Authorization", authToken)
                .build();
        return send(client, request, ListGamesResponse.class).games();
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ServerAccessException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uriBuilder("/game"))
                //authToken is redundant here but won't hurt
                .PUT(HttpRequest.BodyPublishers.ofString(SERIALIZER.toJson(joinGameRequest)))
                .header("Authorization", joinGameRequest.authToken())
                .build();
        send(client, request, null);
    }

    private URI uriBuilder(String path) {
        try {
            return new URI(serverUrl + path);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Bad URI syntax: " + e); //could be a DataAccessException
        }
    }

    private static <T> T send(HttpClient client, HttpRequest request, Class<T> returnType) throws ServerAccessException {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200)
                throw new ServerAccessException("Server facade HttpSned error code: " + response.statusCode() + ": " + response.body());
            else if (returnType != null) {
                T returnObj = SERIALIZER.fromJson(response.body(), returnType);
                return returnObj;
            }
        } catch (Exception e) {
            throw new ServerAccessException("Server facade HttpSned: " + e.getMessage());
        }
        return null;
    }
}
