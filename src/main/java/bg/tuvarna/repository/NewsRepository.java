package bg.tuvarna.repository;

import bg.tuvarna.model.entities.News;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class NewsRepository implements PanacheRepository<News> {
    public List<News> findByYear(int year) {
        return list("year(published) = ?1", Sort.descending("published"), year);
    }
}
