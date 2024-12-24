package bg.tuvarna.repository;

import bg.tuvarna.model.entities.Council;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CouncilRepository implements PanacheRepository<Council> {
}
