package bg.tuvarna.model.dto;

import bg.tuvarna.enums.ProfileRole;
import org.keycloak.representations.idm.UserRepresentation;

public record UserCreateDto(
        String email,
        String firstName,
        String lastName,
        String personalId,
        String phoneNumber,
        String city,
        String address,
        String postal_code,
        ProfileRole userType
) {
    public UserCreateDto(UserRepresentation userRepresentation, ProfileRole userType) {
        this(
                userRepresentation.getEmail(),
                userRepresentation.getFirstName(),
                userRepresentation.getLastName(),
                null,
                null,
                null,
                null,
                null,
                userType
        );
    }
}
