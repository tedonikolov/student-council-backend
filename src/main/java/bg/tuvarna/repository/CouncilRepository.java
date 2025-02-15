package bg.tuvarna.repository;

import bg.tuvarna.enums.CouncilRole;
import bg.tuvarna.model.entities.Council;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CouncilRepository implements PanacheRepository<Council> {
    public List<Council> findByCouncilRoles(List<CouncilRole> roles) {
        return list("roles in ?1", roles);
    }
}
