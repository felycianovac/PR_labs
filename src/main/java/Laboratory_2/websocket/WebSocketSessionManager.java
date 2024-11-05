package Laboratory_2.websocket;

import org.springframework.web.socket.WebSocketSession;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessionManager {
    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public static void addSession(String sessionId, WebSocketSession session) {
        sessions.put(sessionId, session);
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public static WebSocketSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }
}
