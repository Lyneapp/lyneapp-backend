package dev.lyneapp.backendapplication.recommendation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;


/**
 * <br>Every user is assigned a cache storage and recommendedUsers are added to the cache if they meet the user's requirement by a certain percentage.<br>
 * <br>When a recommendedUser signs up the user is alerted and goes to the recommendedUser's profile to run a weight algorithm<br>
 * <br>This is to determine if that recommendedUser should be added to the user's cache<br>
 *
 * OR
 *
 * Users perform a text search on the entire records in the database with their preference data and puts records with a degree of fitness in the cache
 *
 */

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequest {
    private String userPhoneNumber;
    private String userId;
    private int minAge;
    private int maxAge;
    private int minHeight;
    private int maxHeight;
    private String gender;
    private List<String> preferredTribes;
    private String preferredReligion;
    private List<String> preferredLocations;
    private boolean prefDrinking;
    private boolean prefSmoking;
    private boolean prefChildren;
    private boolean isVerified;
}
