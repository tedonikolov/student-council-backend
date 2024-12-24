package bg.tuvarna.service;

import bg.tuvarna.model.dto.ProfileDTO;
import bg.tuvarna.model.entities.Profile;

import java.util.List;

public interface ProfileService {
    void createProfile(ProfileDTO profileDTO);
    Profile getProfile(String name);
    List<Profile> getAll();
    void checkUserCredentials(ProfileDTO profileDTO);
    void frozen(String name, boolean frozen);
}
