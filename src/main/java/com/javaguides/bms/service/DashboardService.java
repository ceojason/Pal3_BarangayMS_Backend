package com.javaguides.bms.service;

import com.javaguides.bms.model.returnmodel.DashboardReturnModel;
import jakarta.servlet.http.HttpSession;

public interface DashboardService {
    DashboardReturnModel getDashboardData(Integer roleKey, HttpSession session);
}
