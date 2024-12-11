package websocket;


import chat.ChatRoomHandler;
import chat.ChatRoomService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket

public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatRoomService chatRoomService;

    public WebSocketConfig(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @Bean
    public ChatRoomHandler chatRoomHandler() {
        return new ChatRoomHandler(chatRoomService);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatRoomHandler(), "/chat").setAllowedOrigins("*");
    }
}