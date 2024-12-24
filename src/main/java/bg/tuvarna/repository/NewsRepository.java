package bg.tuvarna.repository;

import bg.tuvarna.model.entities.News;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NewsRepository implements PanacheRepository<News> {
}
