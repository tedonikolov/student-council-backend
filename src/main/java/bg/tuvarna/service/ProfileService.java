package bg.tuvarna.service;

import bg.tuvarna.model.dto.PersonRequestDTO;
import bg.tuvarna.model.dto.ProfileDTO;
import bg.tuvarna.model.entities.Profile;
import jakarta.ws.rs.core.Response;

import java.util.List;

public interface ProfileService {
    Response createProfile(PersonRequestDTO requestDTO);
    ProfileDTO getProfile(String name);
    List<Profile> getAll();
    void frozen(String name, boolean frozen);
    void update(PersonRequestDTO personRequestDTO, Long id);
}
