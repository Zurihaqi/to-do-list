package enigma.to_do_list.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PageResponseWrapper<T>{
    private List<T> items;
    private Long totalItems;
    private Integer totalPages;
    private Integer currentPage;
    private Integer size;

    public PageResponseWrapper(Page<T> page) {
        this.items = page.getContent();
        this.totalItems = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber();
        this.size = page.getSize();
    }
}
