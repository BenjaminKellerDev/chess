package server;

import com.google.gson.Gson;
import dataaccess.*;
import datamodel.*;
import io.javalin.*;
import io.javalin.http.Context;
import model.*;
import org.jetbrains.annotations.NotNull;
import service.AdminService;
import service.GameService;
import service.UserService;

import java.util.List;

public class Server
{

    private final Javalin javalin;

    private final UserService userService;
    private final AdminService adminService;
    private final GameService gameService;

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    private static Gson serializer = new Gson();

    public Server()
    {

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
        //javalin.exception(DataAccess.class, this::exceptionHandler)
    }

    private void dropDatabase(Context context)
    {
        try
        {
            adminService.dropDatabase();
            context.result("{}");
        } catch (DataAccessException e)
        {
            context.status(500).result(serializer.toJson(e.toString()));
        }
    }

    private void register(Context context)
    {
        UserData parsedRequest = serializer.fromJson(context.body(), UserData.class);
        try
        {
            AuthData res = userService.register(parsedRequest);
            String jsonRes = serializer.toJson(res);
            context.result(jsonRes);
        } catch (DataAccessException e)
        {
            if (e.toString().contains("bad request"))
            {
                context.status(400).result(serializer.toJson(new DataAccessExceptionMessage("Register Error: " + e.getMessage())));
            }
            else if (e.toString().contains("already taken"))
            {
                context.status(403).result(serializer.toJson(new DataAccessExceptionMessage("Register Error: " + e.getMessage())));
            }
            else
            {
                context.status(500).result(serializer.toJson(new DataAccessExceptionMessage("Register Error: " + e.getMessage())));
            }
        }
    }

    private void login(@NotNull Context context)
    {
        LoginRequest loginRequest = serializer.fromJson(context.body(), LoginRequest.class);
        try
        {
            AuthData authData = userService.login(loginRequest);
            String res = serializer.toJson(authData);
            context.result(res);
        } catch (DataAccessException e)
        {

            if (e.toString().contains("bad request"))
            {
                context.status(400).result(serializer.toJson(new DataAccessExceptionMessage("Login Error: " + e.getMessage())));
            }
            else if (e.toString().contains("unauthorized"))
            {
                context.status(401).result(serializer.toJson(new DataAccessExceptionMessage("Login Error: " + e.getMessage())));
            }
            else
            {
                context.status(500).result(serializer.toJson(new DataAccessExceptionMessage("Login Error: " + e.getMessage())));
            }
        }
    }

    private void logout(@NotNull Context context)
    {
        LogoutRequest logoutRequest = new LogoutRequest(context.header("authorization"));
        try
        {
            userService.logout(logoutRequest);
        } catch (DataAccessException e)
        {
            if (e.toString().contains("unauthorized"))
            {
                context.status(401).result(serializer.toJson(new DataAccessExceptionMessage("Logout Error: " + e.getMessage())));
            }
            else
            {
                context.status(500).result(serializer.toJson(new DataAccessExceptionMessage("Logout Error: " + e.getMessage())));
            }
        }
    }

    private void listGames(@NotNull Context context)
    {
        String authorization = context.header("authorization");
        try
        {
            List<GameData> listOfGames = gameService.listGames(authorization);
            String res = serializer.toJson(new ListGamesResponse(listOfGames));
            context.result(res);
        } catch (DataAccessException e)
        {
            if (e.toString().contains("unauthorized"))
            {
                context.status(401).result(serializer.toJson(new DataAccessExceptionMessage("List Games Error: " + e.getMessage())));
            }
            else
            {
                context.status(500).result(serializer.toJson(new DataAccessExceptionMessage("List Games Error: " + e.getMessage())));
            }
        }
    }

    private void createGame(@NotNull Context context)
    {
        String authorization = context.header("authorization");
        CreateGameRequest createGameRequest = serializer.fromJson(context.body(), CreateGameRequest.class);
        try
        {
            int gameID = gameService.createGame(authorization, createGameRequest.gameName());
            String res = serializer.toJson(new CreateGameResponce(gameID));
            context.result(res);
        } catch (DataAccessException e)
        {
            if (e.toString().contains("bad request"))
            {
                context.status(400).result(serializer.toJson(new DataAccessExceptionMessage("Create Game Error: " + e.getMessage())));
            }
            else if (e.toString().contains("unauthorized"))
            {
                context.status(401).result(serializer.toJson(new DataAccessExceptionMessage("Create Game Error: " + e.getMessage())));
            }
            else
            {
                context.status(500).result(serializer.toJson(new DataAccessExceptionMessage("Create Game Error: " + e.getMessage())));
            }
        }
    }

    private void joinGame(@NotNull Context context)
    {
        String authorization = context.header("authorization");
        JoinGameRequest joinGameRequest = serializer.fromJson(context.body(), JoinGameRequest.class);
        joinGameRequest = joinGameRequest.addAuth(authorization);
        try
        {
            gameService.joinGame(joinGameRequest);
        } catch (DataAccessException e)
        {
            if (e.toString().contains("bad request"))
            {
                context.status(400).result(serializer.toJson(new DataAccessExceptionMessage("Join Game Error: " + e.getMessage())));
            }
            else if (e.toString().contains("unauthorized"))
            {
                context.status(401).result(serializer.toJson(new DataAccessExceptionMessage("Join Game Error: " + e.getMessage())));
            }
            else if (e.toString().contains("already taken"))
            {
                context.status(403).result(serializer.toJson(new DataAccessExceptionMessage("Join Game Error: " + e.getMessage())));
            }
            else
            {
                context.status(500).result(serializer.toJson(new DataAccessExceptionMessage("Join Game Error: " + e.getMessage())));
            }
        }
    }

    public int run(int desiredPort)
    {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop()
    {
        javalin.stop();
    }
}
