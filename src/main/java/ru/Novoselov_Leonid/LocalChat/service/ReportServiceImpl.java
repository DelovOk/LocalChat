package ru.Novoselov_Leonid.LocalChat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.Novoselov_Leonid.LocalChat.entity.Report;
import ru.Novoselov_Leonid.LocalChat.enums.ReportStatus;
import ru.Novoselov_Leonid.LocalChat.repository.MessageRepository;
import ru.Novoselov_Leonid.LocalChat.repository.ReportRepository;
import ru.Novoselov_Leonid.LocalChat.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final TemplateEngine templateEngine;

    public ReportServiceImpl(ReportRepository reportRepository, UserRepository userRepository, MessageRepository messageRepository, TemplateEngine templateEngine) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.templateEngine = templateEngine;
    }

    @Override
    public Report getReport(Long reportId) {
        return reportRepository.findById(reportId).orElseThrow(() -> new IllegalArgumentException("Отчет не найден"));
    }

    @Override
    public Long createReport() {
        Report report = new Report();
        report.setStatus(ReportStatus.CREATED);
        report = reportRepository.save(report);
        return report.getId();
    }

    @Override
    public void generateReport(Long reportId) {
        CompletableFuture.runAsync(() -> {
            long totalStartTime = System.currentTimeMillis();
            AtomicLong usersCount = new AtomicLong();
            AtomicLong userDuration = new AtomicLong();
            AtomicReference<List<String>> itemsList = new AtomicReference<>();
            AtomicLong itemsDuration = new AtomicLong();
            try {
                Thread usersThread = new Thread(() -> {
                    long start = System.currentTimeMillis();
                    long count = userRepository.count();
                    usersCount.set(count);
                    userDuration.set(System.currentTimeMillis() - start);
                });
                Thread itemsThread = new Thread(() -> {
                    long start = System.currentTimeMillis();
                    List<String> items = new ArrayList<>();
                    messageRepository.findAll().forEach(message -> items.add(message.getContent()));
                    itemsList.set(items);
                    itemsDuration.set(System.currentTimeMillis() - start);
                });
                usersThread.start();
                itemsThread.start();
                usersThread.join();
                itemsThread.join();
                long totalDuration = System.currentTimeMillis() - totalStartTime;
                String html = buildHtmlReport(
                        usersCount.get(),
                        userDuration.get(),
                        itemsList.get(),
                        itemsDuration.get(),
                        totalDuration
                );
                updateReport(reportId, ReportStatus.COMPLETED, html);
            } catch (Exception e) {
                updateReport(reportId, ReportStatus.ERROR, e.getMessage());
            }
        });
    }

    private void updateReport(Long reportId, ReportStatus status, String content) {
        reportRepository.findById(reportId).ifPresent(report -> {
            report.setStatus(status);
            report.setContent(content);
            reportRepository.save(report);
        });
    }

    private String buildHtmlReport(long usersCount, long userDuration, List<String> items, long itemsDuration, long totalDuration) {
        Context context = new Context();
        context.setVariable("usersCount", usersCount);
        context.setVariable("userDuration", userDuration);
        context.setVariable("items", items);
        context.setVariable("itemsDuration", itemsDuration);
        context.setVariable("totalDuration", totalDuration);
        return templateEngine.process("report", context);
    }
}
