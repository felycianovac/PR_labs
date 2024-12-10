package Laboratory_3.rabbitMQ;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rabbit")
public class RabbitController {

    private final RabbitMQPublisher rabbitMQPublisher;

    public RabbitController(RabbitMQPublisher rabbitMQPublisher) {
        this.rabbitMQPublisher = rabbitMQPublisher;
    }

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody MessageRequest messageRequest) {
        rabbitMQPublisher.sendMessage(messageRequest.getMessage());
        return ResponseEntity.ok("Message sent to RabbitMQ.");
    }
}
