package server;

import com.google.gson.Gson;
import dataaccess.UserDAO;
import dataaccess.RAMUserDAO;
import io.javalin.*;
import io.javalin.http.Context;
import service.UserService;

public class Server
{

    private final Javalin server;
    private UserService userService;
    private UserDAO dataAccess;

    public Server()
    {

        userService = new UserService();
        dataAccess = new RAMUserDAO();

        server = Javalin.create(config -> config.staticFiles.add("web"));

        server.delete("db", this::dropDatabase);
        server.post("user", this::register);
        // Register your endpoints and exception handlers here.

    }

    private void dropDatabase(Context ctx)
    {
        dataAccess.dropDatabase();
        ctx.result("{}");
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
