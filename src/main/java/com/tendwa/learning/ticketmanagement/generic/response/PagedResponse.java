package com.tendwa.learning.ticketmanagement.generic.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse <T>{
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;
}
