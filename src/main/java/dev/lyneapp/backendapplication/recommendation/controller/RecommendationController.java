package dev.lyneapp.backendapplication.recommendation.controller;


import com.amazonaws.services.personalizeruntime.model.GetRecommendationsResult;
import dev.lyneapp.backendapplication.common.model.User;
import dev.lyneapp.backendapplication.recommendation.model.RecommendationRequest;
import dev.lyneapp.backendapplication.recommendation.model.RecommendedUser;
import dev.lyneapp.backendapplication.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendation")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping(path ="vanillaRecommendedUsers")
    public ResponseEntity<List<User>> getVanillaRecommendations(@RequestBody RecommendationRequest recommendationRequest) {
        List<User> recommendations = recommendationService.getVanillaRecommendations(recommendationRequest);
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }

    @GetMapping(path ="recommendedUsers")
    public ResponseEntity<GetRecommendationsResult> getRecommendations(@RequestBody RecommendationRequest recommendationRequest) {
        GetRecommendationsResult recommendations = recommendationService.getRecommendations(recommendationRequest);
        return new ResponseEntity<>(recommendations, HttpStatus.OK);
    }
}
