package dev.lyneapp.backendapplication.messaging.repository;

import dev.lyneapp.backendapplication.messaging.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WSMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatId(String chatId);
    ChatMessage findFirstByChatIdOrderByTimestampDesc(String userId);
}
