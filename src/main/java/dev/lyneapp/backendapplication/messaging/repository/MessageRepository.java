package dev.lyneapp.backendapplication.messaging.repository;// MessageRepository.java
import dev.lyneapp.backendapplication.messaging.model.MessageDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<MessageDTO, String> {
    List<MessageDTO> findBySenderIdAndReceiverIdOrderByCreatedAtAsc(String senderId, String receiverId);
}
