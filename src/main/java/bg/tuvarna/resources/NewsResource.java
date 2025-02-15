package bg.tuvarna.resources;

import bg.tuvarna.model.dto.CustomPage;
import bg.tuvarna.model.dto.NewsRequestDTO;
import bg.tuvarna.model.dto.NewsResponse;
import bg.tuvarna.model.dto.PageRequest;
import bg.tuvarna.service.NewsService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.List;

@Path("/sc-api/v1/news")
public class NewsResource {
    private final NewsService newsService;

    public NewsResource(NewsService newsService) {
        this.newsService = newsService;
    }

    @POST
    @Operation(summary = "Create news.",
            description = "Creates news")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("ADMIN")
    public Response addNews(NewsRequestDTO newsRequestDTO) {
        return Response.ok(newsService.save(newsRequestDTO)).build();
    }

    @PUT
    @Path("{id}")
    @Operation(summary = "Update news.",
            description = "Update news")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed("ADMIN")
    public Response updateNews(@PathParam("id") Long id,NewsRequestDTO newsRequestDTO) {
        return Response.ok(newsService.update(newsRequestDTO,id)).build();
    }

    @GET
    @PermitAll
    @Path("/get/newsByPages")
    public Response newsByPages(@RequestBody PageRequest request){
        CustomPage<NewsResponse> news = newsService.getPagesWithNews(request);
        return Response.ok(news).build();
    }

    @GET
    @PermitAll
    @Path("/get/lastThree")
    public Response lastThreeNews(){
        List<NewsResponse> news = newsService.lastThreeNews();
        return Response.ok(news).build();
    }

    @GET
    @PermitAll
    @Path("/getByYear")
    public Response getBbyYear(@RestQuery("year") int year){
        List<NewsResponse> news = newsService.getAllByYear(year);
        return Response.ok(news).build();
    }

    @GET
    @RolesAllowed("ADMIN")
    @Path("/getAll")
    public Response getAll(){
        List<NewsResponse> news = newsService.getAll();
        return Response.ok(news).build();
    }

    @GET
    @PermitAll
    @Path("/{id}")
    public Response getNews(@PathParam("id") Long id) {
        NewsResponse news = newsService.getNews(id);
        return Response.ok(news).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteNews(@PathParam("id") Long id) {
        newsService.delete(id);
        return Response.ok("News deleted successful!").build();
    }
}
