package com.example.ssak3.common.model;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {

    private final List<T> content;
    private final long totalElements;
    private final int size;
    private final int page;

    private PageResponse(List<T> content, long totalElements, int size, int page) {
        this.content = content;
        this.totalElements = totalElements;
        this.size = size;
        this.page = page;
    }

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber()
        );
    }
}
