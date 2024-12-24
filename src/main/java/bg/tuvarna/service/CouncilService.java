package bg.tuvarna.service;

import bg.tuvarna.model.dto.PersonRequestDTO;
import bg.tuvarna.model.entities.Council;

import java.util.List;

public interface CouncilService {
    Council save(PersonRequestDTO personRequestDTO);
    Council update(PersonRequestDTO personRequestDTO, Long id);
    void delete(Long id);
    List<Council> getAll();
    List<Council> getCouncil();
    List<Council> getAdministrator();
    List<Council> getChairman();
    Council getPerson(Long id);
}
