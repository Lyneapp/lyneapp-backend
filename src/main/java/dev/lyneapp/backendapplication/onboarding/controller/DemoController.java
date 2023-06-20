package dev.lyneapp.backendapplication.onboarding.controller;


import dev.lyneapp.backendapplication.onboarding.service.MediaFilesService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demoController")
@RequiredArgsConstructor
public class DemoController {
    private final static Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

    @GetMapping
    public ResponseEntity<String> sayHello() {
        LOGGER.info("Hello from secured endpoint");
        return ResponseEntity.ok("Hello from secured endpoint");
    }
}
