package com.example.ssak3.common.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageResponse<T> {

    private List<T> content;
    private long totalElements;
    private int size;
    private int page;

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
