package server;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> games = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        if (!games.containsKey(gameID)) {
            games.put(gameID, ConcurrentHashMap.newKeySet());
        }
        games.get(gameID).add(session);
    }

    public void remove(int gameID, Session session) {
        if (games.containsKey(gameID)) {
            games.get(gameID).remove(session);
        }
        if (games.get(gameID).size() == 0) {
            games.remove(gameID);
        }
    }

    public void broadcast(int gameID, Session excludeSession, ServerMessage message) throws IOException {
        for (Session s : games.get(gameID)) {
            if (s.isOpen()) {
                if (!s.equals(excludeSession)) {
                    s.getRemote().sendString(message.toString());
                }
            }
        }
    }
}

class connections {
    public final ConcurrentHashMap<Session, Session> connections = new ConcurrentHashMap<>();

}