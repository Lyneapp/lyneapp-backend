package dev.lyneapp.backendapplication.recommendation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * <br>For Area of interest, One point for each matching/similar interests = 10% (this is a list that allows a maximum selection = 10)<br>
 * <br>For Age range: if the user Falls within the preferred age range = 10% and if the user Falls outside age range = 5% (selection range has minAge and maxAge)<br>
 * <br>For location, give the sames core to each country = 5%  (this is a list that allows a maximum selection = 3)<br>
 * <br>For tribe 3 same tribe preferences = 10%, 2 same tribe preferences = 5%, 1 same tribe preferences = 2% (this is a list that allows a maximum selection = 3)<br>
 * <br>For religion, Same religion and same denomination = 10%, Same religion = 5%, Mismatch in religion = 2% (single selection)<br>
 * <br>Level of education, Same preference in level of education = 5%, Mismatch in level of education = 2%<br>
 * <br>For height if the user falls within the preferred height range = 5% and if the user Falls outside height range = 2% (selection range has minHeight and maxHeight)<br>
 * <br>All gender selections (system is basically just identifying the right gender to show here), use the gender provided by the user = 5%<br>
 * <br>For language, Preference in 3 same languages = 5%, Preference in 2 same languages = 2% (this is a list that allows a maximum selection = 3)<br>
 * <br>For ShouldTheyHaveChildren if both have same boolean value (true or false) = 5% if a mismatch do not include the recommended user<br>
 * <br>For ShouldTheyDrink if both have same boolean value (true or false) = 5% if a mismatch do not include the recommended user<br>
 * <br>For ShouldTheySmoke if both have same boolean value (true or false) = 5% if a mismatch do not include the recommended user<br>
 *
 */


@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class InteractionPool {

    private List<String> allUsers; // a list of all users in the system
    private List<String> recommendableUsers; // traverse through the database and put all recommendable users in this list
    private List<String> recommendedUsers; // A list of users currently recommended
    private List<String> matchedRecommendedUsers; //user likes recommended user and recommended user likes user so a match needs to happen
    private List<String> recommendedUsersLikedByUser; //user likes recommended user //user does not like recommended user but recommended user likes user
    private List<String> recommendedUsersNotLikedByUser; //user does not like recommended user and no response yet from recommended user
    private List<String> recommendedUsersNotLikedByUserAndNotMutual; //user does not like recommended user but recommended user likes user
    private List<String> recommendedUsersNotLikedByUserAndMutual; //user does not like recommended user and recommended user does not likes user
    private List<String> recommendedUsersLikedUser; //recommended user likes user and no response yet from user
    private List<String> recommendedUsersNotLikedUser; //recommended user does not like user and no response yet from user
    private List<String> recommendedUserBlockedUser;  //recommended user blocked user
    private List<String> blockedByUser;  //user blocked recommended user
}
