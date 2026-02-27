package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.helper.JwtUtil;
import com.javaguides.bms.service.DashboardService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {

    private DashboardService dashboardService;

    @GetMapping("/{roleKey}")
    public ApiResponseModel getUsersList(@PathVariable Integer roleKey, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ApiResponseModel("Unauthorized: Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        Claims claims = JwtUtil.validate(token);
        String userId = claims.get("userId", String.class);

        return new ApiResponseModel(dashboardService.getDashboardData(roleKey, userId));
    }

}
