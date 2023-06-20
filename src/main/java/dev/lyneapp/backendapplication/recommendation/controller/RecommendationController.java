package dev.lyneapp.backendapplication.recommendation.controller;


import com.amazonaws.services.personalizeruntime.model.GetRecommendationsResult;
import dev.lyneapp.backendapplication.common.model.User;
import dev.lyneapp.backendapplication.onboarding.service.MediaFilesService;
import dev.lyneapp.backendapplication.recommendation.model.RecommendationRequest;
import dev.lyneapp.backendapplication.recommendation.model.RecommendedUser;
import dev.lyneapp.backendapplication.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendation")
@RequiredArgsConstructor
public class RecommendationController {
    private final static Logger LOGGER = LoggerFactory.getLogger(RecommendationController.class);
    private final RecommendationService recommendationService;

    @GetMapping(path ="vanillaRecommendedUsers")
    public ResponseEntity<List<User>> getVanillaRecommendations(@RequestBody RecommendationRequest recommendationRequest) {
        LOGGER.info("Vanilla recommendation request received for user with id: {}", recommendationRequest.getUserId());
        List<User> recommendations = recommendationService.getVanillaRecommendations(recommendationRequest);
        LOGGER.info("Vanilla recommendation request completed for user with id: {}", recommendationRequest.getUserId());
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }

    @GetMapping(path ="recommendedUsers")
    public ResponseEntity<GetRecommendationsResult> getRecommendations(@RequestBody RecommendationRequest recommendationRequest) {
        LOGGER.info("Recommendation request received for user with id: {}", recommendationRequest.getUserId());
        GetRecommendationsResult recommendations = recommendationService.getRecommendations(recommendationRequest);
        LOGGER.info("Recommendation request completed for user with id: {}", recommendationRequest.getUserId());
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }
}
