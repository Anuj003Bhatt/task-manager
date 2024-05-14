package com.abcorp.taskmanager.util;

import com.abcorp.taskmanager.model.base.DtoBridge;
import com.abcorp.taskmanager.model.response.ListResponse;
import com.abcorp.taskmanager.model.response.Pagination;
import org.springframework.data.domain.Page;

import java.util.List;

public abstract class BridgeUtil {

    public static <T,I extends DtoBridge<T>> ListResponse<T> buildPaginatedResponse (Page<I> list) {
        return ListResponse.<T>builder()
                .list(list.map(I::toDto).toList())
                .pagination(
                        Pagination
                                .builder()
                                .totalPages(list.getTotalPages())
                                .currentPage(list.getNumber())
                                .elements(list.getTotalElements())
                                .build()
                ).build();
    }

    public static <T,I extends DtoBridge<T>> ListResponse<T> buildResponse (List<I> list) {
        return ListResponse.<T>builder()
                .list(list.stream().map(I::toDto).toList())
                .pagination(
                        null
                ).build();
    }
}