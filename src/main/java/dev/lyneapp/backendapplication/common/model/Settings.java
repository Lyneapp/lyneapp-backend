package dev.lyneapp.backendapplication.common.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class Settings {
    private String userPhoneNumber;

    private String blockedUsersCount;
    private List<String> blockedUsers = new ArrayList<>();
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
