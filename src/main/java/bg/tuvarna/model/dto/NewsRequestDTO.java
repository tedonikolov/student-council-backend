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

    @FormParam("files")
    @PartType("application/octet-stream")
    private List<File> files;

    public CreateNewsDTO getCreateNewsDTO() {
        return createNewsDTO;
    }

    public void setCreateNewsDTO(CreateNewsDTO createNewsDTO) {
        this.createNewsDTO = createNewsDTO;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
