package dev.lyneapp.backendapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableCaching
@RestController
@RequestMapping("/api/auth/v1/")
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@GetMapping(path ="lyneapp")
    public ResponseEntity<String> sayHello() {
		String lyneApp = "Building LyneApp, one line of code at a time!";
		return ResponseEntity.status(200).body(lyneApp);
	}
}
