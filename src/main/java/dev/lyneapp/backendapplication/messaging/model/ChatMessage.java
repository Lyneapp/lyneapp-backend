package dev.lyneapp.backendapplication.messaging.model;

import dev.lyneapp.backendapplication.common.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chatMessages")
public class ChatMessage {
    @Id
    private String id;
    private String chatId; // A combination of sender and recipient phone numbers + can be gotten from the business logic or form the request body
    private String content; // FIXME: Not null constraint + Gotten in JSON request body
    private String sender; // FIXME: Not null constraint + Gotten in JSON request body
    private String senderPhoneNumber; // Gotten in JSON request body or can be gotten from the database
    private String recipientPhoneNumber; // Gotten in JSON request body or can be gotten from the database
    private String senderName; // Gotten in JSON request body or can be gotten from the database
    private String recipientName; // Gotten in JSON request body or can be gotten from the database

    private long timestamp; // An epoch time

    private boolean isContentBouquet;

    private MessageType messageType; // Values: LEAVE, JOIN, CHAT
    private MessageStatus status; // Values: RECEIVED, DELIVERED, SEEN

    private Bouquet bouquet;
    private BouquetDTO bouquetDTO;

    @Reference
    private User user;
}
