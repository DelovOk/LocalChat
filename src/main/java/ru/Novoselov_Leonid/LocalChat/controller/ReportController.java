package ru.Novoselov_Leonid.LocalChat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Novoselov_Leonid.LocalChat.entity.Report;
import ru.Novoselov_Leonid.LocalChat.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<Void> generateFromBrowser() {
        Long reportId = reportService.createReport();
        reportService.generateReport(reportId);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/reports/" + reportId)
                .build();
    }

    @PostMapping
    public ResponseEntity<Long> createReport() {
        Long reportId = reportService.createReport();
        reportService.generateReport(reportId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reportId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getReport(@PathVariable Long id) {
        Report report = reportService.getReport(id);

        return switch (report.getStatus()) {
            case CREATED -> ResponseEntity.ok("Отчет еще формируется");
            case ERROR -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(report.getContent());
            case COMPLETED -> ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(report.getContent());
        };
    }
}
