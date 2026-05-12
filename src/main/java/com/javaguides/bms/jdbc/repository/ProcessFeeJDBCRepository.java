package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.ProcessFeeModel;

public interface ProcessFeeJDBCRepository {
    ProcessFeeModel getProcessFeeByKey(Integer key);
}
