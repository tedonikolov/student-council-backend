package bg.tuvarna.repository;

import bg.tuvarna.model.entities.Profile;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProfileRepository implements PanacheRepository<Profile> {
}
