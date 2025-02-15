package bg.tuvarna.model.dto;


import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;

import java.io.File;

public class PersonRequestDTO {
    @FormParam("userDto")
    @PartType(MediaType.APPLICATION_JSON)
    private UserDto userDto;
    @FormParam("image")
    @PartType("application/octet-stream")
    private File image;

    public PersonRequestDTO() {
    }

    public PersonRequestDTO(UserDto userDto) {
        this.userDto = userDto;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
