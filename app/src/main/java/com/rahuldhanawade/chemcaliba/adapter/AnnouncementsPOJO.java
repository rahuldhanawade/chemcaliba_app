package com.rahuldhanawade.chemcaliba.adapter;

public class AnnouncementsPOJO {
    private Integer announcementMasterId;
    private String announcementTitle;
    private String announcementDescription;
    private String createdDate;
    private String updatedDate;
    private String courseCategoryName;

    public Integer getAnnouncementMasterId() {
        return announcementMasterId;
    }

    public void setAnnouncementMasterId(Integer announcementMasterId) {
        this.announcementMasterId = announcementMasterId;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getAnnouncementDescription() {
        return announcementDescription;
    }

    public void setAnnouncementDescription(String announcementDescription) {
        this.announcementDescription = announcementDescription;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCourseCategoryName() {
        return courseCategoryName;
    }

    public void setCourseCategoryName(String courseCategoryName) {
        this.courseCategoryName = courseCategoryName;
    }
}
