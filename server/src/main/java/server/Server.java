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

    private final Javalin server;

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
        gameService = new GameService(gameDAO);

        server = Javalin.create(config -> config.staticFiles.add("web"));

        server.delete("db", this::dropDatabase);
        server.post("user", this::register);
        server.post("session", this::login);
        // Register your endpoints and exception handlers here.
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
            ctx.result(e.toString());
        }
    }

    private void dropDatabase(Context ctx)
    {
        try
        {
            adminService.dropDatbase();
            ctx.result("{}");
        } catch (DataAccessException e)
        {
            ctx.result(e.toString());
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
            ctx.result(e.toString());
        }
    }

    public int run(int desiredPort)
    {
        server.start(desiredPort);
        return server.port();
    }

    public void stop()
    {
        server.stop();
    }
}
