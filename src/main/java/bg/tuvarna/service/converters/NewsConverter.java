package bg.tuvarna.service.converters;

import bg.tuvarna.model.dto.CreateNewsDTO;
import bg.tuvarna.model.entities.News;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;

@ApplicationScoped
public class NewsConverter {
    public static News toEntity(CreateNewsDTO newsDTO) {
        News news = new News();
        updateEntity(news, newsDTO);
        news.setPublished(LocalDate.now());
        return news;
    }

    public static void updateEntity(News news, CreateNewsDTO newsDTO) {
        news.setTitle(newsDTO.title());
        news.setSubtitle(newsDTO.subtitle());
        news.setContent(newsDTO.content());
    }
}
