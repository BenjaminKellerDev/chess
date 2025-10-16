import dataaccess.*;
import org.eclipse.jetty.server.Authentication;
import server.Server;
import service.AdminService;
import service.GameService;
import service.UserService;

public class Main
{


    public static void main(String[] args)
    {
        final Server server;

        final UserService userService;
        final AdminService adminService;
        final GameService gameService;

        final AuthDAO authDAO;
        final GameDAO gameDAO;
        final UserDAO userDAO;

        authDAO = new RAMAuthDAO();
        gameDAO = new RAMGameDAO();
        userDAO = new RAMUserDAO();

        userService = new UserService(userDAO);
        adminService = new AdminService(authDAO, gameDAO, userDAO);
        gameService = new GameService(gameDAO);


        server = new Server(userService, adminService, gameService);

        server.run(8080);

        System.out.println("♕ 240 Chess Server");
    }
}