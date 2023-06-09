package dev.lyneapp.backendapplication.recommendation.service;


import com.amazonaws.services.personalizeruntime.AmazonPersonalizeRuntime;
import com.amazonaws.services.personalizeruntime.model.GetRecommendationsRequest;
import com.amazonaws.services.personalizeruntime.model.GetRecommendationsResult;
import dev.lyneapp.backendapplication.common.model.User;
import dev.lyneapp.backendapplication.common.model.UserPreference;
import dev.lyneapp.backendapplication.common.repository.UserPreferenceRepository;
import dev.lyneapp.backendapplication.common.repository.UserRepository;
import dev.lyneapp.backendapplication.common.util.exception.UserIdNotFoundException;
import dev.lyneapp.backendapplication.recommendation.model.RecommendationRequest;
import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static dev.lyneapp.backendapplication.common.util.exception.ExceptionMessages.USER_NOT_FOUND_WITH_ID;



/**
 * <br>1. Create datasets in Amazon S3: You need to upload your user dataset, item dataset, and interaction dataset to Amazon S3. Create a separate bucket for each dataset.
 * <br>
 * <br>2. Define your schemas: Personalize requires you to define schemas for your datasets. A schema defines the structure and data types of the dataset fields. Define schemas for your user, item, and interaction datasets. You can use the fields you mentioned in your Java classes to define the schema.
 * <br>
 * <br>3. Create a dataset group: A dataset group is a container for your datasets, event trackers, solutions, and campaigns. Create a dataset group in Personalize to organize your resources.
 * <br>
 * <br>4. Create datasets: Within your dataset group, create three datasets: one for users, one for items, and one for interactions. When creating the datasets, associate them with the corresponding schemas and choose the appropriate dataset type based on your data.
 * <br>
 * <br>5. Import data into datasets: Use the Amazon Personalize API or the AWS Management Console to import data from your Amazon S3 buckets into the datasets you created. Specify the S3 location and choose the appropriate dataset for each data import job.
 * <br>
 * <br>6. Define your event tracker: An event tracker is used to capture user interactions in real-time. You can create an event tracker in your dataset group and associate it with the interaction dataset.
 * <br>
 * <br>7. Train a recommendation model (solution): In Personalize, you need to create a solution, which represents a trained recommendation model. Configure the solution by specifying the recipe (algorithm) to use and the dataset group, user dataset, item dataset, and interaction dataset to include.
 * <br>
 * <br>8. Create a campaign: A campaign hosts the trained solution and provides a real-time endpoint for generating recommendations. Create a campaign and associate it with your solution.
 * <br>
 * <br>9. Generate recommendations: To generate recommendations in your Java Spring Boot application, you can use the AWS SDK for Java. Configure the SDK with your AWS credentials and region.
 * <br>
 * <br>10. Call the Personalize runtime API: Use the AWS SDK for Java to call the Personalize runtime API and get recommendations for a specific user. You will need to provide the campaign ARN and the user ID for which you want to generate recommendations.
 * <br>
 * <br>11. Process the recommendation response: Once you receive the recommendation response, you can process the recommended users and display them in your application based on your desired logic.
 * <br>
 * <br>12. Remember to handle error scenarios, perform appropriate error handling, and implement any additional features or customization specific to your application's needs.
 * <br>
 * <br>It's important to refer to the AWS Personalize documentation for detailed instructions, API references, and best practices while setting up and using the service.
 */

@Service
public class RecommendationService {
    // TODO If disliked, wait 72 hours and show profile again
    // TODO If liked by A and not yet by B, prioritize showing B to A if their preferences align
    // TODO For the recommendation engine, load users to the cache once every 24 hours and then when the user makes a call to get recommendations,
    // TODO Get X recommendations for the user on a schedule (24hrs cycle) and then filter out the users that the user has already seen and are blocked
    // TODO Handle the case where the user has already seen all the recommendations and there are no more recommendations to show gracefully


    @Value("${filter.arn}")
    private String filterArn;

    @Value("${campaign.arn}")
    private String campaignArn;

    private final AmazonPersonalizeRuntime personalizeRuntimeClient;
    private final UserPreferenceRepository userPreferenceRepository;
    private final UserRepository userRepository;

    private final Cache matchedUsersCache;
    private final Cache likedUsersCache;
    private final Cache recommendedUsersCache;

    public RecommendationService(AmazonPersonalizeRuntime personalizeRuntimeClient,
                                 UserPreferenceRepository userPreferenceRepository,
                                 UserRepository userRepository, CacheManager cacheManager) {
        this.personalizeRuntimeClient = personalizeRuntimeClient;
        this.userPreferenceRepository = userPreferenceRepository;
        this.userRepository = userRepository;
        this.matchedUsersCache = cacheManager.getCache("matchedUsers");
        this.likedUsersCache = cacheManager.getCache("likedUsers");
        this.recommendedUsersCache = cacheManager.getCache("recommendedUsers");
    }

    @Cacheable(value = "Users", key = "#recommendationRequest.getUserPhoneNumber()", unless = "#result.size() == 0")
    public List<User> getVanillaRecommendations(RecommendationRequest recommendationRequest) {
        UserPreference userPreference = userPreferenceRepository.findById(recommendationRequest.getUserPhoneNumber()).orElse(null);
        if (userPreference == null) {
            throw new UserIdNotFoundException(USER_NOT_FOUND_WITH_ID);
        }

        User user = userRepository.findById(recommendationRequest.getUserPhoneNumber()).orElse(null);
        if (user == null) {
            throw new UserIdNotFoundException(USER_NOT_FOUND_WITH_ID);
        }

        List<User> recommendations = userRepository.findUsersByGenderAndTribeAndReligionAndDoYouHaveChildrenAndDoYouDrinkAndDoYouSmoke(
                userPreference.getPreferredGender().getPreferredGender(),
                userPreference.getPreferredTribes().getPreferredTribes(),
                userPreference.getPreferredReligion().getPreferredReligion(),
                userPreference.isShouldTheyHaveChildren(),
                userPreference.isShouldTheyDrink(),
                userPreference.isShouldTheySmoke()
        );

        LocalDateTime now = LocalDateTime.now();

        List<User> filteredRecommendations = new ArrayList<>();
        for (User recommendation : recommendations) {
            if (isUserBlocked(recommendation.getId()) || isUserMatched(recommendation.getId())) {
                continue;
            }

            // Check if the user has been previously recommended within the last 5 days
            List<User> previouslyRecommendedUsers = recommendedUsersCache.get(recommendationRequest.getUserPhoneNumber(), List.class);
            LocalDateTime expirationTime = recommendedUsersCache.get(recommendationRequest.getUserPhoneNumber() + "_expiration", LocalDateTime.class);

            if (previouslyRecommendedUsers != null && expirationTime != null && now.isBefore(expirationTime)) {
                boolean alreadyRecommended = previouslyRecommendedUsers.stream()
                        .anyMatch(u -> u.getId().equals(recommendation.getId()));
                if (alreadyRecommended) {
                    continue;
                }
            }

            double score = 0.0;

            // Age range score
            Range<Integer> ageRange = Range.between(userPreference.getPreferredAgeRange().getMinimumAge(),
                    userPreference.getPreferredAgeRange().getMaximumAge());

            if (ageRange.contains(Integer.parseInt(recommendation.getAge()))) {
                score += 0.1;
            } else {
                score += 0.05;
            }

            // Height range filtering
            Range<Integer> heightRange = Range.between(userPreference.getPreferredHeightRange().getMinimumHeight(),
                    userPreference.getPreferredHeightRange().getMaximumHeight());
            if (heightRange.contains(Integer.parseInt(recommendation.getHeight()))) {
                score += 0.05;
            } else {
                score += 0.02;
            }

            // Location filtering
            if (userPreference.getPreferredLocations().contains(recommendation.getCurrentLocation())) {
                score += 0.05;
            }

            // Additional factors
            if (recommendation.isDoYouDrink() == userPreference.isShouldTheyDrink()) {
                score += 0.05;
            }

            if (recommendation.isDoYouSmoke() == userPreference.isShouldTheySmoke()) {
                score += 0.05;
            }

            if (recommendation.isDoYouHaveChildren() == userPreference.isShouldTheyHaveChildren()) {
                score += 0.05;
            }

            // Exclude recommendations with mismatched ShouldTheyHaveChildren
            if (recommendation.isDoYouHaveChildren() != userPreference.isShouldTheyHaveChildren()) {
                continue;
            }

            // Add recommendation with the calculated score
            recommendation.setScore(score);
            filteredRecommendations.add(recommendation);
        }

        filteredRecommendations.sort(Comparator.comparing(User::getScore).reversed());
        List<User> finalRecommendations = filteredRecommendations.stream().limit(5).collect(Collectors.toList());

        // Cache the recommended users for 5 days
        cacheRecommendedUsers(recommendationRequest.getUserPhoneNumber(), finalRecommendations);
        return finalRecommendations;
    }

    public void cacheRecommendedUsers(String userId, List<User> recommendedUsers) {
        LocalDateTime expirationTime = LocalDateTime.now().plusDays(5);

        recommendedUsersCache.put(userId, recommendedUsers);
        recommendedUsersCache.put(userId + "_expiration", expirationTime);
    }

    @Cacheable(value = "addMatchedUsers", key = "#userId")
    public void addToMatchedUsersCache(String userId) {
        matchedUsersCache.put(userId, true);
    }

    @Cacheable(value = "addToLikedUsers", key = "#userId")
    public void addToLikedUsersCache(String userId) {
        likedUsersCache.put(userId, true);
    }

    @Cacheable(value = "blockedUsers", key = "#userId")
    public boolean isUserBlocked(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserIdNotFoundException(USER_NOT_FOUND_WITH_ID);
        }
        return user.getBlockedUsers().contains(userId);
    }

    @Cacheable(value = "matchedUsers", key = "#userId")
    public boolean isUserMatched(String userId) {
        // Code to check if the user is matched
        return matchedUsersCache.get(userId, Boolean.class) != null;
    }

    @Cacheable(value = "likedUsers", key = "#userId")
    public boolean isUserLiked(String userId) {
        return likedUsersCache.get(userId, Boolean.class) != null;
    }

    public GetRecommendationsResult getRecommendations(RecommendationRequest recommendationRequest) {
        StringBuilder filterExpression = new StringBuilder();
        if (recommendationRequest.getMinAge() > 0 && recommendationRequest.getMaxAge() > 0) {
            filterExpression.append("Users.age >= ").append(recommendationRequest.getMinAge()).append(" AND Users.age <= ").append(recommendationRequest.getMaxAge()).append(" ");
        }
        if (recommendationRequest.getMinHeight() > 0 && recommendationRequest.getMaxHeight() > 0) {
            filterExpression.append("Users.height >= ").append(recommendationRequest.getMinHeight()).append(" AND Users.height <= ").append(recommendationRequest.getMaxHeight()).append(" ");
        }
        if (recommendationRequest.getGender() != null && !recommendationRequest.getGender().isEmpty()) {
            filterExpression.append("Users.gender = '").append(recommendationRequest.getGender()).append("' ");
        }
        if (recommendationRequest.getPreferredTribes() != null && !recommendationRequest.getPreferredTribes().isEmpty()) {
            filterExpression.append("Users.tribe IN [");
            for (int i = 0; i < recommendationRequest.getPreferredTribes().size(); i++) {
                filterExpression.append("'").append(recommendationRequest.getPreferredTribes().get(i)).append("'");
                if (i < recommendationRequest.getPreferredTribes().size() - 1) {
                    filterExpression.append(",");
                }
            }
            filterExpression.append("] ");
        }
        if (recommendationRequest.getPreferredReligion() != null && !recommendationRequest.getPreferredReligion().isEmpty()) {
            filterExpression.append("Users.religion = '").append(recommendationRequest.getPreferredReligion()).append("' ");
        }
        if (recommendationRequest.getPreferredLocations() != null && !recommendationRequest.getPreferredLocations().isEmpty()) {
            filterExpression.append("Users.location IN [");
            for (int i = 0; i < recommendationRequest.getPreferredLocations().size(); i++) {
                filterExpression.append("'").append(recommendationRequest.getPreferredLocations().get(i)).append("'");
                if (i < recommendationRequest.getPreferredLocations().size() - 1) {
                    filterExpression.append(",");
                }
            }
            filterExpression.append("] ");
        }
        if (recommendationRequest.isPrefDrinking()) {
            filterExpression.append("Users.drinking = true ");
        }
        if (recommendationRequest.isPrefSmoking()) {
            filterExpression.append("Users.smoking = true ");
        }
        if (recommendationRequest.isPrefChildren()) {
            filterExpression.append("Users.has_children = true ");
        }
        if (recommendationRequest.isVerified()) {
            filterExpression.append("Users.verified = true ");
        }

        GetRecommendationsRequest request = new GetRecommendationsRequest();
        request.setCampaignArn(campaignArn);
        request.setUserId(recommendationRequest.getUserId());
        request.setFilterArn(filterArn);
        request.setFilterValues(createFilterValues(recommendationRequest, filterExpression.toString()));

        // TODO - Throttling: When we get a list of recommendations via user phone number we limit it to a certain number of recommendations per user per day
        // TODO - and store the list in a cache or in DB so that we don't send the same users redundantly the next couple of days (5 - 7) and then clear it
        // TODO - only to recommend users that have never been matched with the user previously
        // TODO - we populate each user's UserProfile model class

        return personalizeRuntimeClient.getRecommendations(request);
    }

    private Map<String, String> createFilterValues(RecommendationRequest recommendationRequest, String filterExpression) {
        Map<String, String> filterValues = new HashMap<>();

        // Set the filter expression
        filterValues.put("$filter_expression", filterExpression);

        if (recommendationRequest.getMinAge() > 0 && recommendationRequest.getMaxAge() > 0) {
            filterValues.put("$min_age", String.valueOf(recommendationRequest.getMinAge()));
            filterValues.put("$max_age", String.valueOf(recommendationRequest.getMaxAge()));
        }

        if (recommendationRequest.getMinHeight() > 0 && recommendationRequest.getMaxHeight() > 0) {
            filterValues.put("$min_height", String.valueOf(recommendationRequest.getMinHeight()));
            filterValues.put("$max_height", String.valueOf(recommendationRequest.getMaxHeight()));
        }

        if (recommendationRequest.getGender() != null && !recommendationRequest.getGender().isEmpty()) {
            filterValues.put("$user_gender", recommendationRequest.getGender());
        }

        if (recommendationRequest.getPreferredTribes() != null && !recommendationRequest.getPreferredTribes().isEmpty()) {
            List<String> preferredTribes = recommendationRequest.getPreferredTribes();
            String preferredTribesValue = String.join(",", preferredTribes);
            filterValues.put("$preferred_tribes", preferredTribesValue);
        }

        if (recommendationRequest.getPreferredReligion() != null && !recommendationRequest.getPreferredReligion().isEmpty()) {
            filterValues.put("$preferred_religion", recommendationRequest.getPreferredReligion());
        }

        if (recommendationRequest.getPreferredLocations() != null && !recommendationRequest.getPreferredLocations().isEmpty()) {
            List<String> preferredLocations = recommendationRequest.getPreferredLocations();
            String preferredLocationsValue = String.join(",", preferredLocations);
            filterValues.put("$preferred_locations", preferredLocationsValue);
        }

        filterValues.put("$pref_drinking", String.valueOf(recommendationRequest.isPrefDrinking()));
        filterValues.put("$pref_smoking", String.valueOf(recommendationRequest.isPrefSmoking()));
        filterValues.put("$pref_children", String.valueOf(recommendationRequest.isPrefChildren()));
        filterValues.put("$is_verified", String.valueOf(recommendationRequest.isVerified()));

        return filterValues;
    }
}


