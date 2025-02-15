package bg.tuvarna.model.dto;

import bg.tuvarna.enums.CouncilRole;
import bg.tuvarna.enums.Faculty;
import bg.tuvarna.model.entities.Council;

import java.util.List;

public record ProfileDTO(
        String username,
        String email,
        String fullName,
        String phoneNumber,
        String grade,
        Faculty faculty,
        List<CouncilRole> roles,
        int fromYear,
        int toYear,
        String image
) {
    public ProfileDTO (Council council, String image){
        this(council.getProfile().getUsername(), council.getProfile().getEmail(), council.getProfile().getFullName(), council.getProfile().getPhoneNumber(), council.getProfile().getGrade(), council.getProfile().getFaculty(), council.getRoles(), council.getFromYear(), council.getToYear(), image);
    }
}
