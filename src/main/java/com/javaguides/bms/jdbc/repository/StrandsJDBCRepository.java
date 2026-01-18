package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.StrandsModel;

import java.util.List;

public interface StrandsJDBCRepository {
    List<StrandsModel> findAllActiveStrands();

    StrandsModel findDtlsByKey(String key);
}
