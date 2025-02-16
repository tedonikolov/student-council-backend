package bg.tuvarna.service.impl;

import bg.tuvarna.model.dto.ProfileDTO;
import bg.tuvarna.model.dto.UserDto;
import bg.tuvarna.model.entities.Council;
import bg.tuvarna.repository.CouncilRepository;
import bg.tuvarna.service.CouncilService;
import bg.tuvarna.service.S3Service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import static bg.tuvarna.enums.CouncilRole.*;

@ApplicationScoped
public class CouncilServiceImpl implements CouncilService {
    private final CouncilRepository councilRepository;
    private final S3Service s3Service;

    public CouncilServiceImpl(CouncilRepository councilRepository, S3Service s3Service) {
        this.councilRepository = councilRepository;
        this.s3Service = s3Service;
    }

    @Override
    @Transactional
    public Council saveCouncil(Long profileId, UserDto userDto) {
        Council council = new Council();
        council.setId(profileId);
        council.setFromYear(userDto.getFromYear());
        council.setToYear(userDto.getToYear());
        council.setRoles(userDto.getRoles());

        councilRepository.persist(council);
        return council;
    }

    @Override
    @Transactional
    public void updateCouncil(Long id, UserDto userDto) {
        Council council = councilRepository.findById(id);
        council.setFromYear(userDto.getFromYear());
        council.setToYear(userDto.getToYear());
        council.setRoles(userDto.getRoles());

        councilRepository.persist(council);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        councilRepository.deleteById(id);
    }

    @Override
    public List<ProfileDTO> getAll() {
        List<Council> list = councilRepository.findAll().stream().toList();

        return list.stream().map(council ->
                {
                    try {
                        byte[] file = s3Service.getFile(council.getProfile().getImageUrl()).readAllBytes();
                        String image = Base64.getEncoder().encodeToString(file);
                        return new ProfileDTO(council, image);
                    } catch (IOException | RuntimeException e) {
                        return new ProfileDTO(council, null);
                    }
                }
        ).toList();
    }

    @Override
    public List<ProfileDTO> getCouncil() {
        List<Council> list = councilRepository.findByCouncilRoles(List.of(PR,SPORT,VOLUNTEER,MEMBER,CAREER,ACCOUNTANT,ASSOCIATE)).stream().toList();

        return list.stream().map(this::convertToProfileDTO).toList();
    }

    @Override
    public List<ProfileDTO> getAdministrator() {
        List<Council> list = councilRepository.findByCouncilRoles(List.of(CLUB)).stream().toList();

        return list.stream().map(this::convertToProfileDTO).toList();
    }

    @Override
    public List<ProfileDTO> getChairman() {
        List<Council> list = councilRepository.findByCouncilRoles(List.of(PRESIDENT,VICE_PRESIDENT,SECRETARY,FACULTY)).stream().toList();

        return list.stream().map(this::convertToProfileDTO).toList();
    }

    @Override
    public ProfileDTO getPerson(Long id) {
        Council council = councilRepository.findById(id);
        return convertToProfileDTO(council);
    }

    private ProfileDTO convertToProfileDTO(Council council) {
        try {
            byte[] file = s3Service.getFile(council.getProfile().getImageUrl()).readAllBytes();
            String image = Base64.getEncoder().encodeToString(file);
            return new ProfileDTO(council, image);
        } catch (IOException | RuntimeException e) {
            return new ProfileDTO(council, null);
        }
    }
}
