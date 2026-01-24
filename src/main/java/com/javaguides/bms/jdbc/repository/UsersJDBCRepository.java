package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface UsersJDBCRepository {

    int saveEnrollment(UsersModel modelObj);

    Integer getUsersCount();

    Page<UsersModel> searchUsers(MainSearchRequest requestObj, PageRequest page);

    List<UsersModel> findAllUsersByClassificationKeys(List<Integer> keys);

    Optional<UsersModel> findById(String id);
}
