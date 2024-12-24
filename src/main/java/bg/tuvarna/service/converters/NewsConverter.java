package bg.tuvarna.service.converters;

import bg.tuvarna.model.dto.CreateNewsDTO;
import bg.tuvarna.model.entities.News;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;

@ApplicationScoped
public class NewsConverter {
    public static News toEntity(CreateNewsDTO newsDTO) {
        News news = new News();
        news.setTitle(newsDTO.title());
        news.setSubtitle(newsDTO.subtitle());
        news.setContent(newsDTO.content());
        news.setPublished(LocalDate.now());
        return news;
    }
}
