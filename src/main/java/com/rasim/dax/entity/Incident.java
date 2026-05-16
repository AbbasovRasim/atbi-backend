package com.rasim.dax.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String createdBy;
    private String fullName;
    private String position;
    private String department;
    private String incidentType;
    private String punishment;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate incidentDate;

    private String fileName;
    private String filePath;
    private Boolean deleted = false;

    // ✅ PDF UPLOAD ÜÇÜN YENİ FİELDLƏR (BUNLAR ƏLAVƏ EDİLMƏLİDİR!)
    private String pdfFileName;
    private String pdfFilePath;

    public Incident() {}

    // GETTERS
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getCreatedBy() { return createdBy; }
    public String getFullName() { return fullName; }
    public String getPosition() { return position; }
    public String getDepartment() { return department; }
    public String getIncidentType() { return incidentType; }
    public String getPunishment() { return punishment; }
    public String getStatus() { return status; }
    public LocalDate getIncidentDate() { return incidentDate; }
    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public Boolean getDeleted() { return deleted; }
    public String getPdfFileName() { return pdfFileName; }      // ✅ BUNU ƏLAVƏ ET
    public String getPdfFilePath() { return pdfFilePath; }      // ✅ BUNU ƏLAVƏ ET

    // SETTERS
    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPosition(String position) { this.position = position; }
    public void setDepartment(String department) { this.department = department; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }
    public void setPunishment(String punishment) { this.punishment = punishment; }
    public void setStatus(String status) { this.status = status; }
    public void setIncidentDate(LocalDate incidentDate) { this.incidentDate = incidentDate; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }
    public void setPdfFileName(String pdfFileName) { this.pdfFileName = pdfFileName; }   // ✅ BUNU ƏLAVƏ ET
    public void setPdfFilePath(String pdfFilePath) { this.pdfFilePath = pdfFilePath; }   // ✅ BUNU ƏLAVƏ ET
}