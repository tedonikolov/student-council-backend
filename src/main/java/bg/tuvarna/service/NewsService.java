package bg.tuvarna.service;

import bg.tuvarna.model.dto.CustomPage;
import bg.tuvarna.model.dto.NewsRequestDTO;
import bg.tuvarna.model.dto.PageRequest;
import bg.tuvarna.model.entities.News;

import java.time.Year;
import java.util.List;

public interface NewsService {
    News save(NewsRequestDTO newsRequestDTO);
    News update(NewsRequestDTO newsRequestDTO, String id);
    void delete(Long id);
    List<News> getAll();
    List<News> getAllByYear(Year date);
    News getNews(Long id);
    List<News> firstThreeNews();
    CustomPage<News> getPagesWithNews(PageRequest pageRequest);
}
