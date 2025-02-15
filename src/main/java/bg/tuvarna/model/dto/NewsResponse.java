package bg.tuvarna.model.dto;

import bg.tuvarna.model.entities.News;

import java.time.LocalDate;
import java.util.List;

public class NewsResponse {
    private Long id;
    private String title;
    private String subtitle;
    private List<String> content;
    private LocalDate published;
    private String thumbnail;
    private List<String> images;
    private List<String> videos;

    public NewsResponse() {
    }

    public NewsResponse(News news, String frontImage, List<String> images, List<String> videos) {
        this.id = news.id;
        this.title = news.getTitle();
        this.subtitle = news.getSubtitle();
        this.content = news.getContent();
        this.published = news.getPublished();
        this.thumbnail = frontImage;
        this.images = images;
        this.videos = videos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public LocalDate getPublished() {
        return published;
    }

    public void setPublished(LocalDate published) {
        this.published = published;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }
}
