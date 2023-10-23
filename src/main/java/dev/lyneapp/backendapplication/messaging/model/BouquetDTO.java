package dev.lyneapp.backendapplication.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BouquetDTO {
    private String senderPhoneNumber; // Gotten in JSON request body or can be gotten from the database
    private String recipientPhoneNumber; // Gotten in JSON request body or can be gotten from the database
    private int numberOfBouquetReceived;
    private int remainingBouquets; // The number of bouquets a user has left
    private int numberOfBouquetsBought;
    private int numberOfBouquetsSent;
    private long bouquetCreatedAt; // An epoch time
    private long bouquetUpdatedAt; // An epoch time
    private boolean bouquetSubscriptionEnabled;
}