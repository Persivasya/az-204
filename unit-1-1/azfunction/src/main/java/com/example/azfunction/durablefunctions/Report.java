package com.example.azfunction.durablefunctions;

import java.time.LocalDateTime;

public class Report {

    private Integer id;
    private String name;
    private String description;
    private ReportStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Report(Integer id, String name, String description, ReportStatus status, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
