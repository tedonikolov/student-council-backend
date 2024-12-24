package bg.tuvarna.resources;

import bg.tuvarna.service.impl.ProfileServiceImpl;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/sc-api/admin")
public class ProfileResource {
    private final ProfileServiceImpl profileService;

    public ProfileResource(ProfileServiceImpl profileService) {
        this.profileService = profileService;
    }

    @GET
    public Response getAllProfiles() {
        profileService.getAll();
        return Response.ok().build();
    }
}
