package bg.tuvarna.service.impl;

import bg.tuvarna.model.dto.CustomPage;
import bg.tuvarna.model.dto.NewsRequestDTO;
import bg.tuvarna.model.dto.NewsResponse;
import bg.tuvarna.model.dto.PageRequest;
import bg.tuvarna.model.entities.News;
import bg.tuvarna.repository.NewsRepository;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import bg.tuvarna.service.ContentProcessingService;
import bg.tuvarna.service.NewsService;
import bg.tuvarna.service.S3Service;
import bg.tuvarna.service.converters.NewsConverter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.*;
import java.util.logging.Logger;

@ApplicationScoped
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final ContentProcessingService contentProcessingService;
    private final S3Service s3Service;

    public NewsServiceImpl(NewsRepository newsRepository, ContentProcessingService contentProcessingService, S3Service s3Service) {
        this.newsRepository = newsRepository;
        this.contentProcessingService = contentProcessingService;
        this.s3Service = s3Service;
    }

    @Override
    @Transactional
    public News save(NewsRequestDTO newsRequestDTO) {
        News news=NewsConverter.toEntity(newsRequestDTO.getCreateNewsDTO());
        newsRepository.persist(news);

        List<String> keys = new ArrayList<>();
        int i = 1;
        for(File file: newsRequestDTO.getFiles()) {
            String keyName = news.id.toString()+"_"+i;
            try {
                String path = contentProcessingService.process(file, keyName);
                keys.add(path);
                Logger.getLogger(S3ServiceImpl.class.getName()).info("File uploaded successfully! " + keyName);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("Error uploading files", ErrorCode.Failed);
            }
            i++;
        }
        news.setImages(keys);
        return news;
    }

    @Override
    @Transactional
    //TODO: Implement update method
    public News update(NewsRequestDTO newsRequestDTO, String id) {
        News news=NewsConverter.toEntity(newsRequestDTO.getCreateNewsDTO());
        newsRepository.persist(news);
        return news;
    }

    @Override
    public void delete(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public List<News> getAll() {
        List<News> news = newsRepository.listAll();
        news.sort(Comparator.comparing(News::getPublished).reversed());
        return news;
    }

    @Override
    public List<NewsResponse> lastThreeNews(){
        List<News> news = newsRepository.findAll(Sort.by("id").descending()).stream().limit(3).toList();

        List<NewsResponse> newsResponse = new ArrayList<>();

        for(News n: news){
            newsResponse.add(convertToNewsResponse(n));
        }

        return newsResponse;
    }

    @Override
    public List<News> getAllByYear(Year date) {
        return new ArrayList<>();
    }

    @Override
    public NewsResponse getNews(Long id) {
        News news = newsRepository.findByIdOptional(id).orElseThrow(()->new CustomException("News with id:"+id+" not found!", ErrorCode.EntityNotFound));
        return convertToNewsResponse(news);
    }

    @Override
    public CustomPage<NewsResponse> getPagesWithNews(PageRequest pageRequest) {
        PanacheQuery<News> news = newsRepository.findAll(Sort.by("id").descending());
        List<News> newsList = news.page(Page.of(pageRequest.getPage()-1,
                pageRequest.getItemsPerPage())).list();

        List<NewsResponse> newsResponse = new ArrayList<>();

        for(News n: newsList){
            newsResponse.add(convertToNewsResponse(n));
        }

        return new CustomPage<>(pageRequest.getPage(),
                newsResponse,
                pageRequest.getItemsPerPage(),
                news.count(),
                news.pageCount());
    }

    private NewsResponse convertToNewsResponse(News news){

            List<String> thumbnailsUrl = new ArrayList<>();
            List<String> videos = new ArrayList<>();

            if (news.getImages() != null) {
                for (String url : news.getImages()) {
                    byte[] file;
                    try {
                        file = s3Service.getFile(url).readAllBytes();
                    } catch (IOException e) {
                        continue;
                    }
                    if(url.startsWith("images/")){
                        thumbnailsUrl.add(Base64.getEncoder().encodeToString(file));
                        continue;
                    }
                    if(url.startsWith("videos/")){
                        videos.add(Base64.getEncoder().encodeToString(file));
                    }
                }
            }

            return new NewsResponse(news, thumbnailsUrl, videos);
    }
}
