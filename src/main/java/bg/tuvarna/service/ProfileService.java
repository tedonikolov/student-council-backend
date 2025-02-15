package bg.tuvarna.service;

import bg.tuvarna.enums.ProfileRole;
import bg.tuvarna.model.dto.PersonRequestDTO;
import bg.tuvarna.model.dto.ProfileDTO;
import bg.tuvarna.model.entities.Profile;
import jakarta.ws.rs.core.Response;

import java.util.List;

public interface ProfileService {
    Response createProfile(PersonRequestDTO requestDTO, ProfileRole role);
    ProfileDTO getProfile(String name);
    List<Profile> getAll();
    void frozen(String name, boolean frozen);
    void update(PersonRequestDTO personRequestDTO, Long id);
}
