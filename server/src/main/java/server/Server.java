package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.RAMUserDAO;
import io.javalin.*;
import io.javalin.http.Context;
import service.AdminService;
import service.GameService;
import service.UserService;

public class Server
{

    private final Javalin server;

    private final UserService userService;
    private final AdminService adminService;
    private final GameService gameService;

    public Server(UserService userService, AdminService adminService, GameService gameService)
    {
        this.userService = userService;
        this.adminService = adminService;
        this.gameService = gameService;

        server = Javalin.create(config -> config.staticFiles.add("web"));

        server.delete("db", this::dropDatabase);
        server.post("user", this::register);
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
            ctx.result(e.toString());
        }
    }

    private void register(Context ctx)
    {
        var serializer = new Gson();
        var parsedRequest = serializer.fromJson(ctx.body(), User.class);
        userService.register(parsedRequest);
        var res = serializer.toJson(parsedRequest);
        ctx.result(res);
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
