package bg.tuvarna.service;

import bg.tuvarna.model.dto.ProfileDTO;
import bg.tuvarna.model.dto.UserDto;
import bg.tuvarna.model.entities.Council;

import java.util.List;

public interface CouncilService {
    Council saveCouncil(Long profileId, UserDto userDto);
    void updateCouncil(Long id, UserDto userDto);
    void delete(Long id);
    List<ProfileDTO> getAll();
    List<ProfileDTO> getCouncil();
    List<ProfileDTO> getAdministrator();
    List<ProfileDTO> getChairman();
    ProfileDTO getPerson(Long id);
}
