package bg.tuvarna.service.impl;

import bg.tuvarna.model.dto.CustomPage;
import bg.tuvarna.model.dto.NewsRequestDTO;
import bg.tuvarna.model.dto.PageRequest;
import bg.tuvarna.model.entities.News;
import bg.tuvarna.repository.NewsRepository;
import bg.tuvarna.resources.execptions.CustomException;
import bg.tuvarna.resources.execptions.ErrorCode;
import bg.tuvarna.service.ContentProcessingService;
import bg.tuvarna.service.NewsService;
import bg.tuvarna.service.converters.NewsConverter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Year;
import java.util.*;
import java.util.logging.Logger;

@ApplicationScoped
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final ContentProcessingService contentProcessingService;

    public NewsServiceImpl(NewsRepository newsRepository, ContentProcessingService contentProcessingService) {
        this.newsRepository = newsRepository;
        this.contentProcessingService = contentProcessingService;
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
    public List<News> firstThreeNews(){
        List<News> news = getAll();
        return news.stream().limit(3).toList();
    }

    @Override
    public List<News> getAllByYear(Year date) {
        return new ArrayList<>();
    }

    @Override
    public News getNews(Long id) {
        return newsRepository.findByIdOptional(id).orElseThrow(()->new CustomException("News with id:"+id+" not found!", ErrorCode.EntityNotFound));
    }

    @Override
    public CustomPage<News> getPagesWithNews(PageRequest pageRequest) {
        PanacheQuery<News> news = newsRepository.findAll();
        return new CustomPage<>(pageRequest.getPage(),
                news.page(Page.of(pageRequest.getPage(),
                        pageRequest.getItemsPerPage())).list(),
                pageRequest.getItemsPerPage(),
                news.count(),
                news.pageCount());
    }
}
