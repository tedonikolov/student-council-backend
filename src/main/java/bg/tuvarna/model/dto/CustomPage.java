package bg.tuvarna.model.dto;

import java.util.List;

public class CustomPage<T> {
    private int currentPage;
    private int totalPages;
    private int size;
    private long totalItems;
    private List<T> items;

    public CustomPage(int currentPage, List<T> items, int size, long totalItems, int totalPages) {
        this.currentPage = currentPage;
        this.items = items;
        this.size = size;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}

