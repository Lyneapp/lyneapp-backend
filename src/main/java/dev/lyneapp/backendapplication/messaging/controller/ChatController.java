package dev.lyneapp.backendapplication.messaging.controller;

import dev.lyneapp.backendapplication.messaging.model.MessageDTO;
import dev.lyneapp.backendapplication.messaging.service.ChatService;
import dev.lyneapp.backendapplication.recommendation.controller.RecommendationController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO https://www.youtube.com/watch?v=TywlS9iAZCM

/**
 * <br>How to determine if the user is online?<br>
 * <br>1. User login<br>
 * <br>2. User Logout<br>
 * <br>3. LastActiveAt<br>
 * <br>4. User disconnection - Use heartbeats for better user experience (periodically send a heartbeat event to the presence server e.g. every 5 seconds
 * and after sending 3 heartbeat events, the client is disconnected and does not reconnect within x = 30 seconds then the online status is changed to offline)<br>
 *
 * <br>
 *
 * <br>NB<br>
 * <br>- Add push notifications for chat messages and matches<br>
 * <br>- Add a boolean for online presence an implement front end and backend logic for that feature.<br>
 * <br>- Should we start with disappearing messages<br>
 */

@RestController
@RequestMapping("/api/v1/messaging/")
@RequiredArgsConstructor
public class ChatController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    private final ChatService chatService;

    public static final String MESSAGE_SENT_SUCCESSFULLY = "Message sent successfully";

    @PostMapping(path = "send")
    public ResponseEntity<String> sendMessage(@Valid @RequestBody MessageDTO messageDTO) {
        LOGGER.info("Sending message from user with id: {} to user with id: {}", messageDTO.getSenderId(), messageDTO.getReceiverId());
        chatService.sendMessage(messageDTO);
        LOGGER.info("Sent message from user with id: {} to user with id: {}", messageDTO.getSenderId(), messageDTO.getReceiverId());
        return ResponseEntity.ok(MESSAGE_SENT_SUCCESSFULLY);
    }

    @GetMapping(path = "history")
    public ResponseEntity<List<MessageDTO>> getChatHistory(@Valid @RequestBody MessageDTO messageDTO) {
        LOGGER.info("Getting chat history for user with id: {} and user with id: {}", messageDTO.getSenderId(), messageDTO.getReceiverId());
        List<MessageDTO> chatHistory = chatService.getChatHistory(messageDTO.getSenderId(), messageDTO.getReceiverId());
        LOGGER.info("Got chat history for user with id: {} and user with id: {}", messageDTO.getSenderId(), messageDTO.getReceiverId());
        return ResponseEntity.ok(chatHistory);
    }
}