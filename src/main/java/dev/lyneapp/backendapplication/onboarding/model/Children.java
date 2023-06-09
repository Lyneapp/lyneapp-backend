package dev.lyneapp.backendapplication.onboarding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Children {
    private boolean shouldTheyHaveChildren;
    private boolean openToAll;
}
