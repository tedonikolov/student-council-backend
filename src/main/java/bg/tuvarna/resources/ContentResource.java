package bg.tuvarna.resources;

import bg.tuvarna.service.S3Service;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/sc-api/content")
public class ContentResource {
    @Inject
    S3Service s3service;

    @GET
    @Path("/download/{fileName}")
    public Response downloadFile(@PathParam("fileName") String fileName) {
        return s3service.download(fileName);
    }

    @GET
    @Path("/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@PathParam("fileName") String fileName) {
        return Response.ok(s3service.getFile(fileName)).build();
    }
}