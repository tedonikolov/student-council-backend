package bg.tuvarna.model.dto;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

public class PageRequest {
    @QueryParam("itemsPerPage")
    @DefaultValue("10")
    private Integer itemsPerPage;
    @QueryParam(value = "page")
    @DefaultValue("1")
    private Integer page;

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
