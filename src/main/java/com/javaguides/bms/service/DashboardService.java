package com.javaguides.bms.service;

import com.javaguides.bms.model.returnmodel.DashboardReturnModel;

public interface DashboardService {
    DashboardReturnModel getDashboardData(Integer roleKey);
}
