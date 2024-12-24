package bg.tuvarna.resources;

import bg.tuvarna.model.dto.CustomPage;
import bg.tuvarna.model.dto.NewsRequestDTO;
import bg.tuvarna.model.dto.PageRequest;
import bg.tuvarna.model.entities.News;
import bg.tuvarna.service.impl.NewsServiceImpl;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("/sc-api/news")
public class NewsResource {
    private final NewsServiceImpl newsService;

    public NewsResource(NewsServiceImpl newsService) {
        this.newsService = newsService;
    }

    @POST
    @Operation(summary = "Create news.",
            description = "Creates news")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addAnnouncement(NewsRequestDTO newsRequestDTO) {
        return Response.ok(newsService.save(newsRequestDTO)).build();
    }

    @GET
    @Path("/get/newsByPages")
    public Response newsByPages(@RequestBody PageRequest request){
        CustomPage<News> news = newsService.getPagesWithNews(request);
        return Response.ok(news).build();
    }
}
