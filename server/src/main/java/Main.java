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

        server = new Server();

        server.run(8080);

        System.out.println("♕ 240 Chess Server");
    }
}