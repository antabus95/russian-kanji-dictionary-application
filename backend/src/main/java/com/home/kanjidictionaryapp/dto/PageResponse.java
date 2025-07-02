package com.home.kanjidictionaryapp.dto;

import lombok.Value;

import java.util.List;

@Value
public class PageResponse<T> {
    List<T> content;
    int number;
    int pageSize;
    long totalElements;
    int totalPages;
    boolean last;
}
