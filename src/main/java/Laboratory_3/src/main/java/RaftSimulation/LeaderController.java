package RaftSimulation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LeaderController {

    private volatile String currentLeaderAddress;

    @PostMapping("/update-leader")
    public ResponseEntity<String> updateLeader(@RequestBody Map<String, String> payload) {
        currentLeaderAddress = payload.get("leaderAddress");
        return ResponseEntity.ok("Leader updated to: " + currentLeaderAddress);
    }

    @GetMapping("/current-leader")
    public ResponseEntity<String> getCurrentLeader() {
        return ResponseEntity.ok(currentLeaderAddress);
    }
}
