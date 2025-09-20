package com.example.azfunction.durablefunctions;

import java.time.LocalDateTime;

public class Report {

    private Integer id;
    private String name;
    private String description;
    private ReportStatus status;

    public Report() {
    }

    public Report(Integer id, String name, String description, ReportStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
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

    public void setStatus(ReportStatus completed) {
        this.status = completed;
    }
}
