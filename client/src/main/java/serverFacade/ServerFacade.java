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
        //ask prof if there is a better way to do bodyJSON
        httpHandler(HttpMethod.DELETE,"/db", null,null);
    }

    public AuthData register(UserData registerRequest) throws DataAccessException {
       return httpHandler(HttpMethod.POST,"/user",SERIALIZER.toJson(registerRequest),AuthData.class);
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
       return httpHandler(HttpMethod.POST,"/session",SERIALIZER.toJson(loginRequest),AuthData.class);
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
       httpHandler(HttpMethod.POST,"/session",SERIALIZER.toJson(logoutRequest),null);
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

    private enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    private <T> T httpHandler(HttpMethod method, String path, String bodyJSON, Class<T> returnType) throws DataAccessException {
        try {
           HttpRequest.Builder builder = HttpRequest.newBuilder().uri(new URI(serverUrl + path));
            switch(method) {
                case GET:
                    builder.GET()
                    break;
                case POST:
                    builder.POST(HttpRequest.BodyPublishers.ofString(bodyJSON));
                    break;
                case PUT:
                    builder.PUT()
                    break;
                case DELETE:
                    builder.DELETE();
                    break;
                default:

            }
            HttpRequest request =  builder.build();
            return send(client, request, returnType);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Bad URI syntax: " + e);
        }
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
