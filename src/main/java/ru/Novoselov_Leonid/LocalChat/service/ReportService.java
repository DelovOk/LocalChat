package ru.Novoselov_Leonid.LocalChat.service;

import ru.Novoselov_Leonid.LocalChat.entity.Report;

public interface ReportService {
    Report getReport(Long reportId);

    Long createReport();

    void generateReport(Long reportId);
}
