package com.javaguides.bms.service;

import com.javaguides.bms.jdbc.repository.ProcessFeeJDBCRepository;
import com.javaguides.bms.model.ProcessFeeModel;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProcessFeeServiceImpl extends BaseServiceImpl implements ProcessFeeService {

    private final ProcessFeeJDBCRepository processFeeJDBCRepository;

    @Override
    public ProcessFeeModel getProcessFeeByKey(Integer key) {
        ProcessFeeModel feeObj = new ProcessFeeModel();
        if (key!=null) {
            feeObj = processFeeJDBCRepository.getProcessFeeByKey(key);
        }
        return feeObj;
    }

}
