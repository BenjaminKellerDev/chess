package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.AdminService;
import service.GameService;
import service.UserService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocket.messages.*;

import java.io.IOException;

import static websocket.messages.ServerMessage.ServerMessageType.*;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private static final Gson SERIALIZER = new Gson();

    private final ConnectionManager connectionManager = new ConnectionManager();

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) {
        wsConnectContext.enableAutomaticPings();
        System.out.println("Websocket connected!!");
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) {
        UserGameCommand command = SERIALIZER.fromJson(wsMessageContext.message(), UserGameCommand.class);
        if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            command = SERIALIZER.fromJson(wsMessageContext.message(), MakeMoveCommand.class);
        }
        System.out.println(command.getCommandType());
        try {
            switch (command.getCommandType()) {
                case CONNECT -> connect(command, wsMessageContext.session);
                case MAKE_MOVE -> makeMove(command, wsMessageContext.session);
                case LEAVE -> leaveGame(command, wsMessageContext.session);
                case RESIGN -> resign(command, wsMessageContext.session);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void connect(UserGameCommand command, Session session) throws IOException {
        if (gameDAO.getGame(command.getGameID()) == null || authDAO.getAuth(command.getAuthToken()) == null) {
            ErrorMessage em = new ErrorMessage(ERROR, "error: gameID or Auth invalid");
            connectionManager.send(session, em);
            return;
        }

        connectionManager.add(command.getGameID(), session);
        LoadGameMessage lgm = new LoadGameMessage(LOAD_GAME, gameDAO.getGame(command.getGameID()).game());
        connectionManager.send(session, lgm);

        String username = authDAO.getAuth(command.getAuthToken()).username();
        String msg;
        if (gameDAO.getGame(command.getGameID()).whiteUsername().equals(username)) {
            msg = String.format("%s joined as %s", username, "White");
        } else if (gameDAO.getGame(command.getGameID()).blackUsername().equals(username)) {
            msg = String.format("%s joined as %s", username, "Black");
        } else {
            msg = String.format("%s joined as an observer", username);
        }

        NotificationMessage nm = new NotificationMessage(NOTIFICATION, msg);
        connectionManager.broadcast(command.getGameID(), session, nm);

    }

    private void makeMove(UserGameCommand command, Session session) {
    }

    private void leaveGame(UserGameCommand command, Session session) {
    }

    private void resign(UserGameCommand command, Session session) {
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) {
        System.out.println("Websocket closed");
    }
}
