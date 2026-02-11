package com.example.ssak3.common.model;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageResponse<T> {

    private final List<T> content;
    private final long totalPages;
    private final long totalElements;
    private final int size;
    private final int page;

    private PageResponse(List<T> content, long totalPages, long totalElements, int size, int page) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.page = page;
    }

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber()
        );
    }
}
