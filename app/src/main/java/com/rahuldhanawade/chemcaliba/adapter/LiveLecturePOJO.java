package com.rahuldhanawade.chemcaliba.adapter;

public class LiveLecturePOJO {
    private Integer courseMasterId;
    private String courseName;
    private String videoTitle;
    private String videoLink;
    private String validUpto;
    private String uploadedDate;
    private String courseCategoryName;

    public Integer getCourseMasterId() {
        return courseMasterId;
    }

    public void setCourseMasterId(Integer courseMasterId) {
        this.courseMasterId = courseMasterId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getValidUpto() {
        return validUpto;
    }

    public void setValidUpto(String validUpto) {
        this.validUpto = validUpto;
    }

    public String getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(String uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public String getCourseCategoryName() {
        return courseCategoryName;
    }

    public void setCourseCategoryName(String courseCategoryName) {
        this.courseCategoryName = courseCategoryName;
    }
}
