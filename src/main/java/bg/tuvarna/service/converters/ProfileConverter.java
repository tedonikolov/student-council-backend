package bg.tuvarna.service.converters;

import bg.tuvarna.model.dto.ProfileDTO;
import bg.tuvarna.model.dto.UserDto;
import bg.tuvarna.model.entities.Profile;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProfileConverter {
    public static Profile toEntity(UserDto dto) {
        Profile profile = new Profile();
        updateEntity(profile, dto);
        return profile;
    }

    public static ProfileDTO toDto(Profile profile, String image) {
        return new ProfileDTO(profile.id, profile.getUsername(), profile.getEmail(), profile.getRole(), profile.getFullName(), profile.getPhoneNumber(), profile.getGrade(), profile.getFaculty(), profile.getCouncil().getRoles(),profile.getCouncil().getFromYear(),profile.getCouncil().getToYear(), image);
    }

    public static void updateEntity(Profile profile, UserDto userDto) {
        profile.setUsername(userDto.getUsername());
        profile.setEmail(userDto.getEmail());
        profile.setFullName(userDto.getFullName());
        profile.setPhoneNumber(userDto.getPhoneNumber());
        profile.setGrade(userDto.getGrade());
        profile.setFaculty(userDto.getFaculty());
    }
}
