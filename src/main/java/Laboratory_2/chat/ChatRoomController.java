package Laboratory_2.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class ChatRoomController {

    private final List<String> activeUsers = new CopyOnWriteArrayList<>();

    @GetMapping("/chat/activeUsers")
    public List<String> getActiveUsers() {
        return activeUsers;
    }

}
