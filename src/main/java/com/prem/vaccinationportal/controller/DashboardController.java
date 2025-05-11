package com.prem.vaccinationportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prem.vaccinationportal.service.DashboardService;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public Map<String, Object> getDashboardData() {
        return dashboardService.getDashboardData();
    }
}
