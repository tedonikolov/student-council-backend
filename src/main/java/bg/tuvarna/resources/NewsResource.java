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

import java.util.List;

@Path("/sc-api/news")
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
    public Response addAnnouncement(NewsRequestDTO newsRequestDTO) {
        return Response.ok(newsService.save(newsRequestDTO)).build();
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
    @Path("/{id}")
    public Response getNews(@PathParam("id") Long id) {
        NewsResponse news = newsService.getNews(id);
        return Response.ok(news).build();
    }
}
