package bg.tuvarna.resources;

import bg.tuvarna.model.dto.PersonRequestDTO;
import bg.tuvarna.service.CouncilService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("/sc-api/person")
public class PersonResource {
    private final CouncilService councilService;

    public PersonResource(CouncilService councilService) {
        this.councilService = councilService;
    }

    @POST
    @Path("/add")
    public Response save(@RequestBody PersonRequestDTO personRequestDTO) {
        councilService.save(personRequestDTO);
        return Response.ok("Person added successful!").build();
    }

    @PUT
    @Path("/update/{id}")
    public Response update(@RequestBody PersonRequestDTO personRequestDTO) {
        councilService.update(personRequestDTO, 1L);
        return Response.ok("Update successful!").build();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response delete(Long id) {
        councilService.delete(id);
        return Response.ok("Deleted successful!").build();
    }

    @GET
    @Path("/{id}")
    public Response getPerson(Long id) {
        return Response.ok(councilService.getPerson(id)).build();
    }

    @GET
    @Path("/all/person")
    public Response getAllPerson() {
        return Response.ok(councilService.getAll()).build();
    }

    @GET
    @Path("/all/council")
    public Response getCouncil() {
        return Response.ok(councilService.getCouncil()).build();
    }

    @GET
    @Path("/all/administrator")
    public Response getAdministrator() {
        return Response.ok(councilService.getAdministrator()).build();
    }

    @GET
    @Path("/all/chairman")
    public Response getChairman() {
        return Response.ok(councilService.getChairman()).build();
    }
}
