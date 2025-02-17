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
import java.util.*;

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
        News news = NewsConverter.toEntity(newsRequestDTO.getCreateNewsDTO());
        newsRepository.persist(news);

        String keyName = "news/"+news.id.toString() + "_" + 0;
        try {
            String path = contentProcessingService.process(newsRequestDTO.getFrontImage(), keyName);
            news.setFrontImage(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("Error uploading files", ErrorCode.Failed);
        }

        List<String> images = new ArrayList<>();
        int i = 1;
        for (File file : newsRequestDTO.getImages()) {
            keyName = "news/"+ news.id.toString() + "_" + i;
            try {
                String path = contentProcessingService.process(file, keyName);
                images.add(path);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("Error uploading files", ErrorCode.Failed);
            }
            i++;
        }

        news.setImages(images);

        List<String> videos = new ArrayList<>();
        for (File file : newsRequestDTO.getVideos()) {
            keyName = "news/"+ news.id.toString() + "_" + i;
            try {
                String path = contentProcessingService.process(file, keyName);
                videos.add(path);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("Error uploading files", ErrorCode.Failed);
            }
            i++;
        }

        news.setVideos(videos);

        return news;
    }

    @Override
    @Transactional
    public News update(NewsRequestDTO newsRequestDTO, Long id) {
        News news = NewsConverter.toEntity(newsRequestDTO.getCreateNewsDTO());
        news.id = id;
        newsRepository.persist(news);

        if(newsRequestDTO.getFrontImage() != null) {
            String keyName = "news/" + news.id.toString() + "_" + 0;
            try {
                String path = contentProcessingService.process(newsRequestDTO.getFrontImage(), keyName);
                news.setFrontImage(path);
            } catch (IOException e) {
                e.printStackTrace();
                throw new CustomException("Error uploading files", ErrorCode.Failed);
            }
        }

        if(newsRequestDTO.getImages() != null) {
            List<String> images = new ArrayList<>();
            int i = 1;
            for (File file : newsRequestDTO.getImages()) {
                String keyName = "news/" + news.id.toString() + "_" + i;
                try {
                    String path = contentProcessingService.process(file, keyName);
                    images.add(path);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CustomException("Error uploading files", ErrorCode.Failed);
                }
                i++;
            }
            news.setImages(images);
        }

        if(newsRequestDTO.getVideos() != null) {
            List<String> videos = new ArrayList<>();
            int i = 1;
            for (File file : newsRequestDTO.getVideos()) {
                String keyName = "news/" + news.id.toString() + "_" + i;
                try {
                    String path = contentProcessingService.process(file, keyName);
                    videos.add(path);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CustomException("Error uploading files", ErrorCode.Failed);
                }
                i++;
            }
            news.setVideos(videos);
        }

        return news;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public List<NewsResponse> getAll() {
        List<News> news = newsRepository.listAll();
        news.sort(Comparator.comparing(News::getPublished).reversed());
        return news.stream().map(value-> convertToNewsResponse(value,false)).toList();
    }

    @Override
    public List<NewsResponse> lastThreeNews() {
        List<News> news = newsRepository.findAll(Sort.by("published").descending()).stream().limit(3).toList();

        List<NewsResponse> newsResponse = new ArrayList<>();

        for (News n : news) {
            newsResponse.add(convertToNewsResponse(n,false));
        }

        return newsResponse;
    }

    @Override
    public List<NewsResponse> getAllByYear(int year) {
        List<News> news = newsRepository.findByYear(year);
        return news.stream().map(value-> convertToNewsResponse(value,false)).toList();
    }

    @Override
    public NewsResponse getNews(Long id) {
        News news = newsRepository.findByIdOptional(id).orElseThrow(() -> new CustomException("News with id:" + id + " not found!", ErrorCode.EntityNotFound));
        return convertToNewsResponse(news,true);
    }

    @Override
    public CustomPage<NewsResponse> getPagesWithNews(PageRequest pageRequest) {
        PanacheQuery<News> news = newsRepository.findAll(Sort.by("id").descending());
        List<News> newsList = news.page(Page.of(pageRequest.getPage() - 1,
                pageRequest.getItemsPerPage())).list();

        List<NewsResponse> newsResponse = new ArrayList<>();

        for (News n : newsList) {
            newsResponse.add(convertToNewsResponse(n,false));
        }

        return new CustomPage<>(pageRequest.getPage(),
                newsResponse,
                pageRequest.getItemsPerPage(),
                news.count(),
                news.pageCount());
    }

    private NewsResponse convertToNewsResponse(News news, Boolean all) {
        String frontImage = null;
        byte[] file;
        try {
            file = s3Service.getFile(news.getFrontImage()).readAllBytes();
            frontImage = Base64.getEncoder().encodeToString(file);
        } catch (IOException | RuntimeException e) {

        }
        List<String> imagesUrl = new ArrayList<>();
        List<String> videos = new ArrayList<>();

        if(all) {
            if (news.getImages() != null) {
                for (String url : news.getImages()) {
                    try {
                        file = s3Service.getFile(url).readAllBytes();
                    } catch (IOException e) {
                        continue;
                    }
                    if (url.startsWith("images/")) {
                        imagesUrl.add(Base64.getEncoder().encodeToString(file));
                    }
                }
            }
            if (news.getVideos() != null) {
                for (String url : news.getVideos()) {
                    try {
                        file = s3Service.getFile(url).readAllBytes();
                    } catch (IOException e) {
                        continue;
                    }
                    if (url.startsWith("videos/")) {
                        videos.add(Base64.getEncoder().encodeToString(file));
                    }
                }
            }
        }

        return new NewsResponse(news, frontImage, imagesUrl, videos);
    }
}
