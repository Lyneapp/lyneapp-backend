package dev.lyneapp.backendapplication.settings.model;


import dev.lyneapp.backendapplication.onboarding.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDTO {
    private String userPhoneNumber;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String blockedUsersCount;

    private List<String> blockedUsers;

    private Location currentLocation;

    private boolean visibilityIcon;
    private boolean incognitoMode;
    private boolean showMeOnLyne;
    private boolean appleConnected;
    private boolean instagramConnected;
    private boolean facebookConnected;
    private boolean newMatchNotification;
    private boolean newMessageNotification;
    private boolean newMessageLikeNotification;
    private boolean inAppVibration;
    private boolean bouquetReceived;
    private boolean inAppSound;
}
