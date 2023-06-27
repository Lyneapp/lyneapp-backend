package dev.lyneapp.backendapplication.messaging.service;

import dev.lyneapp.backendapplication.messaging.controller.WSChatController;
import dev.lyneapp.backendapplication.messaging.model.MessageDTO;
import dev.lyneapp.backendapplication.messaging.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    private final MessageRepository messageRepository;

    public void sendMessage(MessageDTO message) {
        LOGGER.info("Entering ChatService.sendMessage ,Sending message: {}", message);
        message.setCreatedAt(LocalDateTime.now());
        messageRepository.save(message);
        LOGGER.info("Exiting ChatService.sendMessage ,Message sent: {}", message);
    }

    public List<MessageDTO> getChatHistory(String senderId, String receiverId) {
        LOGGER.info("ChatService.getChatHistory ,Getting messages for sender: {} and receiver: {}", senderId, receiverId);
        return messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAtAsc(senderId, receiverId);
    }
}
