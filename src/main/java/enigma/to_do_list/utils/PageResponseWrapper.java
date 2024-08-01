package enigma.to_do_list.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PageResponseWrapper<T>{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<T> items;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<T> users;
    private Long totalItems;
    private Integer totalPages;
    private Integer currentPage;
    private Integer size;

    public PageResponseWrapper(Page<T> page) {
        this(page, false);
    }

    public PageResponseWrapper(Page<T> page, boolean isAdmin) {
        if (isAdmin) {
            this.users = page.getContent();
        } else {
            this.items = page.getContent();
        }
        this.totalItems = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber();
        this.size = page.getSize();
    }
}
