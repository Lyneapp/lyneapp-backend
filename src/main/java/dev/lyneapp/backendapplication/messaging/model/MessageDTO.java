package dev.lyneapp.backendapplication.messaging.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * <br>1. User A sends a chat message to Chat server 1.<br>
 * <br>2. Chat server 1 obtains a message ID from the ID generator.<br>
 * <br>3. Chat server 1 sends the message to the message sync queue.<br>
 * <br>4. The message is stored in a key-value store.<br>
 * <br>5.a. If User B is online, the message is forwarded to Chat server 2 where User B is
 * connected.<br>
 * <br>5.b. If User B is offline, a push notification is sent from push notification (PN) servers.<br>
 * <br>6. Chat server 2 forwards the message to User B. There is a persistent WebSocket
 * connection between User B and Chat server 2.<br>
 */

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class MessageDTO {
    @Id
    private String id;
    private String userPhoneNumber;
    @NotBlank
    private String senderId;
    @NotBlank
    private String receiverId;
    @NotBlank
    private String content;
    private long creationOrder;
    private LocalDateTime createdAt;

    public MessageDTO(String senderId, String receiverId, String content, long creationOrder) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.creationOrder = System.currentTimeMillis();
    }
}
