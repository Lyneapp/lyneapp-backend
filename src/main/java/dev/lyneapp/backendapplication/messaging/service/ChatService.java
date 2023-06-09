package dev.lyneapp.backendapplication.messaging.service;

import dev.lyneapp.backendapplication.messaging.model.MessageDTO;
import dev.lyneapp.backendapplication.messaging.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;

    public void sendMessage(MessageDTO message) {
        message.setCreatedAt(LocalDateTime.now());
        messageRepository.save(message);
    }

    public List<MessageDTO> getChatHistory(String senderId, String receiverId) {
        return messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAtAsc(senderId, receiverId);
    }
}
