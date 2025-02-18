package bg.tuvarna.repository;

import bg.tuvarna.model.entities.Profile;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProfileRepository implements PanacheRepository<Profile> {
    public Profile findByName(String name) {
        return find("username", name).firstResult();
    }
}
