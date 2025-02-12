package bg.tuvarna.service.impl;

import bg.tuvarna.model.dto.ProfileDTO;
import bg.tuvarna.model.entities.Profile;
import bg.tuvarna.repository.ProfileRepository;
import bg.tuvarna.service.ProfileService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }


    @Override
    @Transactional
    public Response createProfile(ProfileDTO profileDTO) {
        Profile profile = new Profile();
        profile.setUsername(profileDTO.username());

        profile.setRole("admin");

        profileRepository.persist(profile);

        return Response.ok().build();
    }

    @Override
    public Profile getProfile(String name) {
        return null;
    }

    @Override
    public List<Profile> getAll() {
        return List.of();
    }

    @Override
    public void checkUserCredentials(ProfileDTO profileDTO) {

    }

    @Override
    public void frozen(String name, boolean frozen) {

    }
}
