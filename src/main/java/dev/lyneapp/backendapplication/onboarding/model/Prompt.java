package dev.lyneapp.backendapplication.onboarding.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// TODO See prompt-bank.txt. We will give each prompt and ID

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Prompt {
    private int promptID;
    private String promptQuestion;
    private String promptResponse;
}
