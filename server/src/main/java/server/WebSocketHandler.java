package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.io.IOException;
import java.util.Objects;

import static websocket.messages.ServerMessage.ServerMessageType.*;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private static final Gson SERIALIZER = new Gson();

    private final ConnectionManager connectionManager = new ConnectionManager();

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    //to-do refactor to switch from DAO to Services
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
            if (gameDAO.getGame(command.getGameID()) == null || authDAO.getAuth(command.getAuthToken()) == null) {
                ErrorMessage em = new ErrorMessage(ERROR, "error: gameID or Auth invalid");
                connectionManager.send(wsMessageContext.session, em);
                return;
            }
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
        connectionManager.add(command.getGameID(), session);
        LoadGameMessage lgm = new LoadGameMessage(LOAD_GAME, gameDAO.getGame(command.getGameID()).game());
        connectionManager.send(session, lgm);

        String username = authDAO.getAuth(command.getAuthToken()).username();
        String whiteUsername = gameDAO.getGame(command.getGameID()).whiteUsername();
        String blackUsername = gameDAO.getGame(command.getGameID()).blackUsername();
        String msg;
        if (whiteUsername != null && username.equals(whiteUsername)) {
            msg = String.format("%s joined as %s", username, "White");
        } else if (whiteUsername != null && username.equals(blackUsername)) {
            msg = String.format("%s joined as %s", username, "Black");
        } else {
            msg = String.format("%s joined as an observer", username);
        }

        NotificationMessage nm = new NotificationMessage(NOTIFICATION, msg);
        connectionManager.broadcast(command.getGameID(), session, nm);

    }

    private void makeMove(UserGameCommand command, Session session) throws IOException {
        MakeMoveCommand moveCommand;
        if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            moveCommand = (MakeMoveCommand) command;
        } else {
            System.out.println("Internal Mismatch");
            return;
        }

        GameData gameData = gameDAO.getGame(command.getGameID());
        String username = authDAO.getAuth(command.getAuthToken()).username();
        if (gameData.game().getGameState() != ChessGame.GameState.PLAYING) {
            ErrorMessage em = new ErrorMessage(ERROR, "error: the game is over!");
            connectionManager.send(session, em);
            return;
        }
        if (!Objects.equals(username, gameData.whiteUsername()) && !Objects.equals(username, gameData.blackUsername())) {
            ErrorMessage em = new ErrorMessage(ERROR, "error: observers can't make moves!");
            connectionManager.send(session, em);
            return;
        }
        if ((gameData.game().getTeamTurn() == ChessGame.TeamColor.WHITE && !Objects.equals(username, gameData.whiteUsername()))
                || (gameData.game().getTeamTurn() == ChessGame.TeamColor.BLACK && !Objects.equals(username, gameData.blackUsername()))) {
            ErrorMessage em = new ErrorMessage(ERROR, "error: it's not your turn!");
            connectionManager.send(session, em);
            return;
        }
        try {
            gameData.game().makeMove(moveCommand.getMove());
        } catch (InvalidMoveException e) {
            ErrorMessage em = new ErrorMessage(ERROR, "error: invalid move");
            connectionManager.send(session, em);
            return;
        }

        gameDAO.updateGame(gameData);

        LoadGameMessage lgm = new LoadGameMessage(LOAD_GAME, gameDAO.getGame(command.getGameID()).game());
        connectionManager.broadcast(command.getGameID(), lgm);

        String moveString = String.format("move: %s", ChessMove.toUserString(moveCommand.getMove()));
        NotificationMessage nm = new NotificationMessage(NOTIFICATION, moveString);
        connectionManager.broadcast(command.getGameID(), session, nm);

        if (gameData.game().isInStalemate(gameData.game().getTeamTurn())) {
            connectionManager.broadcast(command.getGameID(), new NotificationMessage(NOTIFICATION, "In stalemate"));
        } else if (gameData.game().isInCheckmate(gameData.game().getTeamTurn())) {
            connectionManager.broadcast(command.getGameID(), new NotificationMessage(NOTIFICATION, "Checkmate"));
        } else if (gameData.game().isInCheck(gameData.game().getTeamTurn())) {
            connectionManager.broadcast(command.getGameID(), new NotificationMessage(NOTIFICATION, "Check"));
        }
    }

    private void leaveGame(UserGameCommand command, Session session) throws IOException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        String username = authDAO.getAuth(command.getAuthToken()).username();
        NotificationMessage nm;
        if (username.equals(gameData.whiteUsername())) {
            gameDAO.updateGame(gameData.updateWhiteUsername(null));
            nm = new NotificationMessage(NOTIFICATION, String.format("user %s left", username));
        } else if (username.equals(gameData.blackUsername())) {
            gameDAO.updateGame(gameData.updateBlackUsername(null));
            nm = new NotificationMessage(NOTIFICATION, String.format("user %s left", username));
        } else {
            nm = new NotificationMessage(NOTIFICATION, String.format("observer %s left", username));
        }
        connectionManager.broadcast(command.getGameID(), session, nm);
        connectionManager.remove(command.getGameID(), session);
    }

    private void resign(UserGameCommand command, Session session) throws IOException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        String username = authDAO.getAuth(command.getAuthToken()).username();
        if (gameData.game().getGameState() != ChessGame.GameState.PLAYING) {
            ErrorMessage em = new ErrorMessage(ERROR, "game already over!");
            connectionManager.send(session, em);
            return;
        }
        if (!Objects.equals(username, gameData.whiteUsername()) && !Objects.equals(username, gameData.blackUsername())) {
            ErrorMessage em = new ErrorMessage(ERROR, "observers cannot resign!");
            connectionManager.send(session, em);
            return;
        }

        if (gameData.whiteUsername().equals(username)) {
            gameData.game().setGameState(ChessGame.GameState.BLACK_WON);
        } else if (gameData.blackUsername().equals(username)) {
            gameData.game().setGameState(ChessGame.GameState.WHITE_WON);
        }


        gameDAO.updateGame(gameData);

        connectionManager.broadcast(command.getGameID(), new NotificationMessage(NOTIFICATION, String.format("%s has resigned", username)));
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) {
        System.out.println("Websocket closed");
    }
}
