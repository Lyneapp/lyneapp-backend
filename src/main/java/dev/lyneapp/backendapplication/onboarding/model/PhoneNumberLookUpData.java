package dev.lyneapp.backendapplication.onboarding.model;

import com.twilio.rest.lookups.v2.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.Map;


// https://www.twilio.com/docs/lookup/v2-api - find sample code and response here

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PhoneNumberLookUpData {

    private String callingCountryCode;
    private String countryCode;
    private String nationalFormat;
    private Boolean valid;
    private List<PhoneNumber.ValidationError> validationErrors;
    private Map<String, Object> callerName;
    private Map<String, Object> simSwap;
    private Map<String, Object> callForwarding;
    private Map<String, Object> liveActivity;
    private Map<String, Object> lineTypeIntelligence;
    private Map<String, Object> identityMatch;
    private Map<String, Object> smsPumpingRisk;
    private URI url;
}
