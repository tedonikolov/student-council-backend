package bg.tuvarna.resources;

import bg.tuvarna.model.dto.PersonRequestDTO;
import bg.tuvarna.service.CouncilService;
import bg.tuvarna.service.ProfileService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("/sc-api/v1/person")
public class PersonResource {
    private final CouncilService councilService;
    private final ProfileService profileService;


    public PersonResource(CouncilService councilService, ProfileService profileService) {
        this.councilService = councilService;
        this.profileService = profileService;
    }

    @POST
    @Path("/add")
    @RolesAllowed("admin")
    public Response save(@RequestBody PersonRequestDTO personRequestDTO) {
        profileService.createProfile(personRequestDTO);
        return Response.ok("Person added successful!").build();
    }

    @PUT
    @Path("/update/{id}")
    @RolesAllowed("admin")
    public Response update(@RequestBody PersonRequestDTO personRequestDTO, @PathParam("id") Long id){
        profileService.update(personRequestDTO, id);
        return Response.ok("Update successful!").build();
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("admin")
    public Response delete(@PathParam("id") Long id) {
        councilService.delete(id);
        return Response.ok("Deleted successful!").build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response getPerson(@PathParam("id") Long id) {
        return Response.ok(councilService.getPerson(id)).build();
    }

    @GET
    @Path("/all/person")
    @RolesAllowed("admin")
    public Response getAllPerson() {
        return Response.ok(councilService.getAll()).build();
    }

    @GET
    @Path("/all/council")
    @PermitAll
    public Response getCouncil() {
        return Response.ok(councilService.getCouncil()).build();
    }

    @GET
    @Path("/all/administrator")
    @PermitAll
    public Response getAdministrator() {
        return Response.ok(councilService.getAdministrator()).build();
    }

    @GET
    @Path("/all/chairman")
    @PermitAll
    public Response getChairman() {
        return Response.ok(councilService.getChairman()).build();
    }
}
