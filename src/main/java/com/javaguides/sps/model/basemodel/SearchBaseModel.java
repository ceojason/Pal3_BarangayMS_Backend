package com.javaguides.sps.model.basemodel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class SearchBaseModel {
    private int page;
    private int size;
    private String sortBy;
    private String direction;

    public PageRequest getPageRequest() {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        return PageRequest.of(page, size, sort);
    }
}

