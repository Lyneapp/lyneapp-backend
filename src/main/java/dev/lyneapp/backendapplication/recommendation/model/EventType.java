package dev.lyneapp.backendapplication.recommendation.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventType {
    @NotBlank
    private String eventType;
    @NotBlank
    private String actionType;

    private Boolean viewedProfile;
    private Boolean sentMessage;
    private Boolean receivedMessage;
    private Integer sentMessageWeight;
    private Integer receivedMessageWeight;
    private Boolean sentBouquet;
    private Boolean receivedBouquet;
    private Integer sentBouquetWeight;
    private Integer receivedBouquetWeight;
    private Boolean nextLevel;
}