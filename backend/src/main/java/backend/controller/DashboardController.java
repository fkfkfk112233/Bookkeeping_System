package backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.dto.dashboard.CategoryAmountResponse;
import backend.dto.dashboard.DashboardSummaryResponse;
import backend.dto.dashboard.TrendResponse;
import backend.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getSummary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        DashboardSummaryResponse response =
                dashboardService.getSummary(
                        startDate,
                        endDate
                );

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/expense")
    public ResponseEntity<List<CategoryAmountResponse>> getExpense(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        List<CategoryAmountResponse> response =
                dashboardService.getExpense(
                        startDate,
                        endDate
                );

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/income")
    public ResponseEntity<List<CategoryAmountResponse>> getIncome(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        List<CategoryAmountResponse> response =
                dashboardService.getIncome(
                        startDate,
                        endDate
                );

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/expense/trend")
    public ResponseEntity<List<TrendResponse>> getExpenseTrend(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        List<TrendResponse> response =
                dashboardService.getExpenseTrend(
                        startDate,
                        endDate
                );

        return ResponseEntity.ok(response);
    }
}