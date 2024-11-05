package Laboratory_2.chat;


import lombok.Data;

@Data
public class ChatMessage {
    private String type;
    private String sender;
    private String content;

}
