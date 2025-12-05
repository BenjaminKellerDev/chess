package serverfacade;

import com.google.gson.Gson;
import jakarta.websocket.*;
import repl.GameRepl;
import repl.NotificationHandler;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session serverSession;

    private static final Gson SERIALIZER = new Gson();
    public final GameRepl gameRepl;

    public WebSocketFacade(String url, GameRepl gameRepl) {
        this.gameRepl = gameRepl;
        try {
            String uri = url.replace("http", "ws");
            URI socketURI = new URI(uri + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            serverSession = container.connectToServer(this, socketURI);

            serverSession.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage sm = SERIALIZER.fromJson(message, ServerMessage.class);
                    if (sm.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                        NotificationHandler.notify(SERIALIZER.fromJson(message, NotificationMessage.class));
                    } else if (sm.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                        NotificationHandler.notify(SERIALIZER.fromJson(message, ErrorMessage.class));
                    } else if (sm.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        gameRepl.receiveLoadBoard(SERIALIZER.fromJson(message, LoadGameMessage.class));
                    }

                }
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendUserGameCommand(UserGameCommand userGameCommand) {
        try {
            String str = SERIALIZER.toJson(userGameCommand);
            serverSession.getBasicRemote().sendText(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
