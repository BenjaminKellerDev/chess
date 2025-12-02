package server;

import com.google.gson.Gson;
import dataaccess.*;
import datamodel.*;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsMessageContext;
import model.*;
import org.jetbrains.annotations.NotNull;
import service.AdminService;
import service.GameService;
import service.UserService;
import websocket.messages.*;
import websocket.commands.*;

import java.util.List;

public class Server {

    private final Javalin javalin;
    private final WebSocketHandler webSocketHandler = new WebSocketHandler();

    private final UserService userService;
    private final AdminService adminService;
    private final GameService gameService;

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    private static final Gson SERIALIZER = new Gson();

    public Server() {

        authDAO = new MySQLAuthDAO();
        gameDAO = new MySQLGameDAO();
        userDAO = new MySQLUserDAO();

        userService = new UserService(userDAO, authDAO);
        adminService = new AdminService(authDAO, gameDAO, userDAO);
        gameService = new GameService(gameDAO, authDAO);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("db", this::dropDatabase);
        javalin.post("user", this::register);
        javalin.post("session", this::login);
        javalin.delete("session", this::logout);
        javalin.get("game", this::listGames);
        javalin.post("game", this::createGame);
        javalin.put("game", this::joinGame);
        // To-Do: Register your endpoints and exception handlers here.
        javalin.exception(DataAccessException.class, this::dataAccessExceptionHandler);
        javalin.exception(RuntimeException.class, this::runtimeInterception);

        javalin.ws("ws", ws -> {
            ws.onConnect(webSocketHandler);
            ws.onMessage(webSocketHandler);
            ws.onClose(webSocketHandler);
        });
    }

    private void WebSocketMessageHandler(WsMessageContext wsMessageContext) {
        UserGameCommand command = SERIALIZER.fromJson(wsMessageContext.message(), UserGameCommand.class);
        if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            command = SERIALIZER.fromJson(wsMessageContext.message(), MakeMoveCommand.class);
        }

    }


    private void dataAccessExceptionHandler(@NotNull DataAccessException e, @NotNull Context context) {
        if (e.toString().contains("bad request")) {
            context.status(400).result(SERIALIZER.toJson(new DataAccessExceptionMessage("Error: " + e.getMessage())));
        } else if (e.toString().contains("unauthorized")) {
            context.status(401).result(SERIALIZER.toJson(new DataAccessExceptionMessage("Error: " + e.getMessage())));
        } else if (e.toString().contains("already taken")) {
            context.status(403).result(SERIALIZER.toJson(new DataAccessExceptionMessage("Error: " + e.getMessage())));
        } else {
            context.status(500).result(SERIALIZER.toJson(new DataAccessExceptionMessage("Error: " + e.getMessage())));
        }
    }

    private void runtimeInterception(@NotNull RuntimeException e, @NotNull Context context) {

        context.status(500).result(SERIALIZER.toJson(new DataAccessExceptionMessage("Error: " + e.getMessage())));

    }

    private void dropDatabase(Context context) throws DataAccessException {

        adminService.dropDatabase();
        context.result("{}");

    }

    private void register(Context context) throws DataAccessException {
        UserData parsedRequest = SERIALIZER.fromJson(context.body(), UserData.class);
        AuthData res = userService.register(parsedRequest);
        String jsonRes = SERIALIZER.toJson(res);
        context.result(jsonRes);
    }

    private void login(@NotNull Context context) throws DataAccessException {
        LoginRequest loginRequest = SERIALIZER.fromJson(context.body(), LoginRequest.class);
        AuthData authData = userService.login(loginRequest);
        String res = SERIALIZER.toJson(authData);
        context.result(res);
    }

    private void logout(@NotNull Context context) throws DataAccessException {
        LogoutRequest logoutRequest = new LogoutRequest(context.header("authorization"));
        userService.logout(logoutRequest);
    }

    private void listGames(@NotNull Context context) throws DataAccessException {
        String authorization = context.header("authorization");
        List<GameData> listOfGames = gameService.listGames(authorization);
        String res = SERIALIZER.toJson(new ListGamesResponse(listOfGames));
        context.result(res);
    }

    private void createGame(@NotNull Context context) throws DataAccessException {
        String authorization = context.header("authorization");
        CreateGameRequest createGameRequest = SERIALIZER.fromJson(context.body(), CreateGameRequest.class);

        int gameID = gameService.createGame(authorization, createGameRequest.gameName());
        String res = SERIALIZER.toJson(new CreateGameResponce(gameID));
        context.result(res);
    }

    private void joinGame(@NotNull Context context) throws DataAccessException {
        String authorization = context.header("authorization");
        JoinGameRequest joinGameRequest = SERIALIZER.fromJson(context.body(), JoinGameRequest.class);
        joinGameRequest = joinGameRequest.addAuth(authorization);
        gameService.joinGame(joinGameRequest);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
