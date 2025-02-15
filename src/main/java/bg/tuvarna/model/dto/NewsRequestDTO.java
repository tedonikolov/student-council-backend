package bg.tuvarna.model.dto;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;

import java.io.File;
import java.util.List;

public class NewsRequestDTO {
    @FormParam("createNewsDTO")
    @PartType(MediaType.APPLICATION_JSON)
    private CreateNewsDTO createNewsDTO;

    @FormParam("images")
    @PartType("application/octet-stream")
    private List<File> images;

    @FormParam("videos")
    @PartType("application/octet-stream")
    private List<File> videos;

    @FormParam("frontImage")
    @PartType("application/octet-stream")
    private File frontImage;

    public CreateNewsDTO getCreateNewsDTO() {
        return createNewsDTO;
    }

    public void setCreateNewsDTO(CreateNewsDTO createNewsDTO) {
        this.createNewsDTO = createNewsDTO;
    }

    public File getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(File frontImage) {
        this.frontImage = frontImage;
    }

    public List<File> getImages() {
        return images;
    }

    public void setImages(List<File> images) {
        this.images = images;
    }

    public List<File> getVideos() {
        return videos;
    }

    public void setVideos(List<File> videos) {
        this.videos = videos;
    }
}
