package com.abcorp.taskmanager.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {
    Integer totalPages;
    Integer currentPage;
    Long elements;
}
