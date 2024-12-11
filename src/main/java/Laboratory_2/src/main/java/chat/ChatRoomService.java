package chat;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ChatRoomService {

    private final Map<String, Set<String>> rooms = new HashMap<>();

    public void joinRoom(String roomId, String sessionId) {
        rooms.computeIfAbsent(roomId, k -> new HashSet<>()).add(sessionId);
    }

    public void leaveRoom(String roomId, String sessionId) {
        Set<String> room = rooms.get(roomId);
        if (room != null) {
            room.remove(sessionId);
            if (room.isEmpty()) {
                rooms.remove(roomId);
            }
        }
    }

    public Set<String> getUsersInRoom(String roomId) {
        return rooms.getOrDefault(roomId, new HashSet<>());
    }
}
