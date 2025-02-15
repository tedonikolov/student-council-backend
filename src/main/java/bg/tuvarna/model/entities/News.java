package bg.tuvarna.model.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "news")
public class News extends PanacheEntity {
    private String title;
    private String subtitle;
    @ElementCollection
    private List<String> content;
    private LocalDate published;
    private String frontImage;
    @ElementCollection
    private List<String> images;
    @ElementCollection
    private List<String> videos;

    public News() {
    }

    public News(List<String> content, String frontImage, List<String> images, LocalDate published, String subtitle, String title, List<String> videos) {
        this.content = content;
        this.frontImage = frontImage;
        this.images = images;
        this.published = published;
        this.subtitle = subtitle;
        this.title = title;
        this.videos = videos;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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
