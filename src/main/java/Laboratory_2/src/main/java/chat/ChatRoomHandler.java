package chat;


import websocket.WebSocketSessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChatRoomHandler extends TextWebSocketHandler {

    private final ChatRoomService chatRoomService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, String> sessionRoomMap = new HashMap<>();

    public ChatRoomHandler(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        WebSocketSessionManager.addSession(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        WebSocketSessionManager.removeSession(session.getId());
        String sessionId = session.getId();
        String roomId = sessionRoomMap.get(sessionId);
        if (roomId != null) {
            chatRoomService.leaveRoom(roomId, sessionId);
            sessionRoomMap.remove(sessionId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        Map<String, String> messagePayload = objectMapper.readValue(message.getPayload(), Map.class);
        String type = messagePayload.get("type");
        String roomId = messagePayload.get("roomId");
        String username = messagePayload.get("username");
        String content = messagePayload.get("message");

        String sessionId = session.getId();

        if ("join".equals(type)) {
            chatRoomService.joinRoom(roomId, sessionId);
            sessionRoomMap.put(sessionId, roomId);
            broadcastToRoom(roomId, username + " joined the room.");
        } else if ("message".equals(type) && roomId != null) {
            broadcastToRoom(roomId, username + ": " + content);
        } else if ("leave".equals(type) && roomId != null) {
            chatRoomService.leaveRoom(roomId, sessionId);
            sessionRoomMap.remove(sessionId);
            broadcastToRoom(roomId, username + " left the room.");
        }
    }

    private void broadcastToRoom(String roomId, String message) throws IOException {
        Set<String> users = chatRoomService.getUsersInRoom(roomId);
        for (String sessionId : users) {
            WebSocketSession session = WebSocketSessionManager.getSession(sessionId);
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }
}
