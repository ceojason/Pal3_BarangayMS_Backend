package com.javaguides.sps.jdbc.repository;

import com.javaguides.sps.model.StrandsModel;

import java.util.List;

public interface StrandsJDBCRepository {
    List<StrandsModel> findAllActiveStrands();

    StrandsModel findDtlsByKey(String key);
}
