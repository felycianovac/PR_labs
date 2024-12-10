package Laboratory_3.rabbitMQ;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private String message;

}
