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

        authDAO = new RAMAuthDAO();
        gameDAO = new RAMGameDAO();
        userDAO = new RAMUserDAO();

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
        // Register your endpoints and exception handlers here.
    }

    private void dropDatabase(Context ctx)
    {
        try
        {
            adminService.dropDatbase();
            ctx.result("{}");
        } catch (DataAccessException e)
        {
            ctx.status(500).result(serializer.toJson(e.toString()));
        }
    }

    private void register(Context ctx)
    {
        UserData parsedRequest = serializer.fromJson(ctx.body(), UserData.class);
        try
        {
            AuthData res = userService.register(parsedRequest);
            String JsonRes = serializer.toJson(res);
            ctx.result(JsonRes);
        } catch (DataAccessException e)
        {
            if (e.toString().contains("bad request"))
            {
                ctx.status(400).result(serializer.toJson(new DataAccessExceptionMessage("Error: " + e.getMessage())));
            }
            else if (e.toString().contains("already taken"))
            {
                ctx.status(403).result(serializer.toJson(new DataAccessExceptionMessage("Error: " + e.getMessage())));
            }
            else
            {
                ctx.status(500).result(serializer.toJson(new DataAccessExceptionMessage("Error: " + e.getMessage())));
            }
        }
    }

    private void login(@NotNull Context ctx)
    {
        LoginRequest loginRequest = serializer.fromJson(ctx.body(), LoginRequest.class);
        try
        {
            AuthData authData = userService.login(loginRequest);
            String res = serializer.toJson(authData);
            ctx.result(res);
        } catch (DataAccessException e)
        {

            if (e.toString().contains("bad request"))
            {
                ctx.status(400).result(serializer.toJson(new DataAccessExceptionMessage("Error: " + e.getMessage())));
            }
            else if (e.toString().contains("unauthorized"))
            {
                ctx.status(401).result(serializer.toJson(new DataAccessExceptionMessage("Error: " + e.getMessage())));
            }
            else
            {
                ctx.status(500).result(serializer.toJson(new DataAccessExceptionMessage("Error: " + e.getMessage())));
            }
        }
    }

    private void logout(@NotNull Context context)
    {
    }

    private void listGames(@NotNull Context context)
    {
    }

    private void createGame(@NotNull Context context)
    {
    }

    private void joinGame(@NotNull Context context)
    {
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
