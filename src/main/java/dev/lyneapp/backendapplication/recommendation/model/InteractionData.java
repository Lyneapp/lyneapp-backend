package dev.lyneapp.backendapplication.recommendation.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;


@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class InteractionData {
    @NotBlank
    private String recommendationId;
    @NotBlank
    private String userId;
    @NotBlank
    private String itemId;
    @NotBlank
    private LocalDateTime timestamp;
    @NotBlank
    private EventType event;
}
