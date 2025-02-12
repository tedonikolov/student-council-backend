package bg.tuvarna.resources;

import bg.tuvarna.model.dto.ProfileDTO;
import bg.tuvarna.model.dto.UserDto;
import bg.tuvarna.service.impl.KeycloakService;
import bg.tuvarna.service.impl.ProfileServiceImpl;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("/sc-api/admin")
public class ProfileResource {
    private final ProfileServiceImpl profileService;
    private final KeycloakService keycloakService;

    public ProfileResource(ProfileServiceImpl profileService, KeycloakService keycloakService) {
        this.profileService = profileService;
        this.keycloakService = keycloakService;
    }

    @GET
    @RolesAllowed("admin")
    public Response getAllProfiles() {
        profileService.getAll();
        return Response.ok().build();
    }

    @POST
    @Path("/register")
    @PermitAll
    public Response addProfile(@RequestBody ProfileDTO profileDTO) {
        keycloakService.registerUser(profileDTO.username(), profileDTO.username(), profileDTO.password());
        return Response.ok().build();
    }

    @Path("/login")
    @POST
    @PermitAll
    public Response loginUser(UserDto user) {
        try {
            String token = keycloakService.loginUser(user.getUsername(), user.getPassword());
            if (token == null) return Response.noContent().build();
            return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }
}
