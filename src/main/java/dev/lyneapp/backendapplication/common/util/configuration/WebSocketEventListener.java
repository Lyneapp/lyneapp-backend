package dev.lyneapp.backendapplication.common.util.configuration;

import dev.lyneapp.backendapplication.messaging.model.ChatMessage;
import dev.lyneapp.backendapplication.messaging.model.MessageStatus;
import dev.lyneapp.backendapplication.messaging.model.MessageType;
import dev.lyneapp.backendapplication.messaging.repository.WSMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final WSMessageRepository wsMessageRepository;

    @EventListener
    public void handleWebSocketConnectListener(SessionDisconnectEvent event) {
        log.info("Received a new web socket connection");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");

        if (username != null) {
            log.info("User Disconnected: {}", username);
            ChatMessage chatMessage = ChatMessage.builder()
                    .messageType(MessageType.LEAVE)
                    .sender(username)
                    .build();
            wsMessageRepository.save(chatMessage);
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        log.info("Received a new web socket subscription");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");

        if (username != null) {
            log.info("Sending chat history to: {}", username);
            List<ChatMessage> chatHistory = wsMessageRepository.findAll();
            chatHistory.forEach(chatMessage -> {
                chatMessage.setStatus(MessageStatus.DELIVERED);
                messagingTemplate.convertAndSendToUser(username, "/queue /reply", chatMessage);
            });
        }
    }
}
