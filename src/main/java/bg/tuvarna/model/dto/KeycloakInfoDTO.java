package bg.tuvarna.model.dto;

public record KeycloakInfoDTO(
        String keycloakClientId,
        String keycloakClientSecret,
        String keycloakClientRedirectUri,
        String keycloakClientServerUri
) {
}
