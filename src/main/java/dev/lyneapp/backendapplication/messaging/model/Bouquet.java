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
@Document(collection = "bouquet")
public class Bouquet {
    @Id
    private String id;
    private String bouquetId; // A combination of sender and recipient phone numbers + can be gotten from the business logic or form the request body
    private String senderPhoneNumber; // Gotten in JSON request body or can be gotten from the database
    private String recipientPhoneNumber; // Gotten in JSON request body or can be gotten from the database
    private String numberOfBouquetReceived;
    private long bouquetCreatedAt; // An epoch time
    private long bouquetUpdatedAt; // An epoch time
    private int remainingBouquets; // The number of bouquets a user has left
    private boolean bouquetSubscriptionEnabled;


    @Reference
    private User user;
}
