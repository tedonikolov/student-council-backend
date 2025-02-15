package bg.tuvarna.service.impl;

import bg.tuvarna.model.dto.PersonRequestDTO;
import bg.tuvarna.model.dto.ProfileDTO;
import bg.tuvarna.model.entities.Council;
import bg.tuvarna.model.entities.Profile;
import bg.tuvarna.repository.ProfileRepository;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import bg.tuvarna.service.ContentProcessingService;
import bg.tuvarna.service.CouncilService;
import bg.tuvarna.service.ProfileService;
import bg.tuvarna.service.S3Service;
import bg.tuvarna.service.converters.ProfileConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@ApplicationScoped
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final CouncilService councilService;
    private final S3Service s3Service;
    private final ContentProcessingService contentProcessingService;

    public ProfileServiceImpl(ProfileRepository profileRepository, CouncilService councilService, S3Service s3Service, ContentProcessingService contentProcessingService) {
        this.profileRepository = profileRepository;
        this.councilService = councilService;
        this.s3Service = s3Service;
        this.contentProcessingService = contentProcessingService;
    }

    @Override
    @Transactional
    public Response createProfile(PersonRequestDTO requestDTO) {
        Profile profile = ProfileConverter.toEntity(requestDTO.getUserDto());

        profileRepository.persist(profile);

        if (requestDTO.getImage()!=null) {
            String keyName = profile.id.toString() + "_" + 0;
            try {
                String path = contentProcessingService.process(requestDTO.getImage(), keyName);
                profile.setImageUrl(path);
            } catch (IOException e) {
                e.printStackTrace();
                throw new CustomException("Error uploading files", ErrorCode.Failed);
            }
        }

        Council council = councilService.saveCouncil(profile.id, requestDTO.getUserDto());
        profile.setCouncil(council);

        profileRepository.persist(profile);

        return Response.ok().build();
    }

    @Override
    public ProfileDTO getProfile(String name) {
        Profile profile = profileRepository.findByName(name);

        String frontImage;
        byte[] file;
        try {
            file = s3Service.getFile(profile.getImageUrl()).readAllBytes();
            frontImage = Base64.getEncoder().encodeToString(file);
        } catch (IOException e) {
            return ProfileConverter.toDto(profile, null);
        }

        return ProfileConverter.toDto(profile, frontImage);
    }

    @Override
    public List<Profile> getAll() {
        return List.of();
    }

    @Override
    public void frozen(String name, boolean frozen) {

    }

    @Override
    @Transactional
    public void update(PersonRequestDTO personRequestDTO, Long id) {
        Profile profile = profileRepository.findById(id);
        ProfileConverter.updateEntity(profile, personRequestDTO.getUserDto());

        if (personRequestDTO.getImage()!=null) {
            String keyName = profile.id.toString() + "_" + 0;
            try {
                String path = contentProcessingService.process(personRequestDTO.getImage(), keyName);
                profile.setImageUrl(path);
            } catch (IOException e) {
                e.printStackTrace();
                throw new CustomException("Error uploading files", ErrorCode.Failed);
            }
        }

        profileRepository.persist(profile);

        councilService.updateCouncil(profile.id, personRequestDTO.getUserDto());
    }
}
