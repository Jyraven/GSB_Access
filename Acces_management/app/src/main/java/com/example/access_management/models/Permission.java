package com.example.access_management.models;

public class Permission {
    private String zone;
    private String type; // Temporaire ou Permanent
    private String dateRange;

    public Permission(String zone, String type, String dateRange) {
        this.zone = zone;
        this.type = type;
        this.dateRange = dateRange;
    }

    public String getZone() {
        return zone;
    }

    public String getType() {
        return type;
    }

    public String getDateRange() {
        return dateRange;
    }
}