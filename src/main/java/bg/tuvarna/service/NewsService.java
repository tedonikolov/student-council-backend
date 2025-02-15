package bg.tuvarna.service;

import bg.tuvarna.model.dto.CustomPage;
import bg.tuvarna.model.dto.NewsRequestDTO;
import bg.tuvarna.model.dto.NewsResponse;
import bg.tuvarna.model.dto.PageRequest;
import bg.tuvarna.model.entities.News;

import java.util.List;

public interface NewsService {
    News save(NewsRequestDTO newsRequestDTO);
    News update(NewsRequestDTO newsRequestDTO, Long id);
    void delete(Long id);
    List<NewsResponse> getAll();
    List<NewsResponse> getAllByYear(int year);
    NewsResponse getNews(Long id);
    List<NewsResponse> lastThreeNews();
    CustomPage<NewsResponse> getPagesWithNews(PageRequest pageRequest);
}
