package bg.tuvarna.resources;

import bg.tuvarna.model.dto.LoginDTO;
import bg.tuvarna.model.dto.UserDto;
import bg.tuvarna.service.impl.KeycloakService;
import bg.tuvarna.service.impl.ProfileServiceImpl;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.reactive.RestQuery;

@Path("/sc-api/admin")
public class ProfileResource {
    private final ProfileServiceImpl profileService;
    private final KeycloakService keycloakService;

    public ProfileResource(ProfileServiceImpl profileService, KeycloakService keycloakService) {
        this.profileService = profileService;
        this.keycloakService = keycloakService;
    }

    @GET
    @Authenticated
    public Response getProfile(@RestQuery String username) {
        return Response.ok(profileService.getProfile(username)).build();
    }

    @POST
    @Path("/register")
    @PermitAll
    public Response addProfile(@RequestBody UserDto userDto) {
        keycloakService.registerUser(userDto);
        return Response.ok().build();
    }

    @Path("/login")
    @POST
    @PermitAll
    public Response loginUser(@RequestBody LoginDTO user) {
        try {
            String token = keycloakService.loginUser(user.username(), user.password());
            if (token == null) return Response.noContent().build();
            return Response.ok(user.username()).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }
}
