package dev.lyneapp.backendapplication.onboarding.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tribe {
    // TODO If openToAll is true then the list must be set to empty - implement this logic to set the other field to null when openToAll is true
    private List<String> preferredTribes;
    private boolean openToAll;
}
