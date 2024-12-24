package bg.tuvarna.service.impl;

import bg.tuvarna.model.dto.PersonRequestDTO;
import bg.tuvarna.model.entities.Council;
import bg.tuvarna.service.CouncilService;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CouncilServiceImpl implements CouncilService {
    @Override
    public Council save(PersonRequestDTO personRequestDTO) {
        return null;
    }

    @Override
    public Council update(PersonRequestDTO personRequestDTO, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Council> getAll() {
        return List.of();
    }

    @Override
    public List<Council> getCouncil() {
        return List.of();
    }

    @Override
    public List<Council> getAdministrator() {
        return List.of();
    }

    @Override
    public List<Council> getChairman() {
        return List.of();
    }

    @Override
    public Council getPerson(Long id) {
        return null;
    }
}
