package com.rahuldhanawade.chemcaliba.adapter;

import java.util.LinkedHashMap;
import java.util.Map;

public class OurCourcesPOJO {

    private String courseMasterId;
    private String courseCategoryId;
    private String courseName;
    private String courseInfo;
    private String courseImage;
    private String courseActualPrice;
    private String courseSellPrice;
    private String courseDurationNumberOfDays;
    private String courseNumberOfInstallments;
    private Object telegramGroupLink;
    private Object whatsappGroupLink;
    private String courseStartDate;
    private String courseEndDate;
    private String isAllowPurchaseAfterExpire;
    private String courseStatus;
    private String createdBy;
    private String createdDate;
    private Object updatedBy;
    private Object updatedDate;
    private String courseCategoryName;
    private String courseCategoryInfo;
    private String courseCategoryStatus;
    private Boolean isAlreadyBought;
    private Boolean isCourseExpired;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public String getCourseMasterId() {
        return courseMasterId;
    }

    public void setCourseMasterId(String courseMasterId) {
        this.courseMasterId = courseMasterId;
    }

    public String getCourseCategoryId() {
        return courseCategoryId;
    }

    public void setCourseCategoryId(String courseCategoryId) {
        this.courseCategoryId = courseCategoryId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(String courseInfo) {
        this.courseInfo = courseInfo;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    public String getCourseActualPrice() {
        return courseActualPrice;
    }

    public void setCourseActualPrice(String courseActualPrice) {
        this.courseActualPrice = courseActualPrice;
    }

    public String getCourseSellPrice() {
        return courseSellPrice;
    }

    public void setCourseSellPrice(String courseSellPrice) {
        this.courseSellPrice = courseSellPrice;
    }

    public String getCourseDurationNumberOfDays() {
        return courseDurationNumberOfDays;
    }

    public void setCourseDurationNumberOfDays(String courseDurationNumberOfDays) {
        this.courseDurationNumberOfDays = courseDurationNumberOfDays;
    }

    public String getCourseNumberOfInstallments() {
        return courseNumberOfInstallments;
    }

    public void setCourseNumberOfInstallments(String courseNumberOfInstallments) {
        this.courseNumberOfInstallments = courseNumberOfInstallments;
    }

    public Object getTelegramGroupLink() {
        return telegramGroupLink;
    }

    public void setTelegramGroupLink(Object telegramGroupLink) {
        this.telegramGroupLink = telegramGroupLink;
    }

    public Object getWhatsappGroupLink() {
        return whatsappGroupLink;
    }

    public void setWhatsappGroupLink(Object whatsappGroupLink) {
        this.whatsappGroupLink = whatsappGroupLink;
    }

    public String getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(String courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public String getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(String courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public String getIsAllowPurchaseAfterExpire() {
        return isAllowPurchaseAfterExpire;
    }

    public void setIsAllowPurchaseAfterExpire(String isAllowPurchaseAfterExpire) {
        this.isAllowPurchaseAfterExpire = isAllowPurchaseAfterExpire;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Object getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Object updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCourseCategoryName() {
        return courseCategoryName;
    }

    public void setCourseCategoryName(String courseCategoryName) {
        this.courseCategoryName = courseCategoryName;
    }

    public String getCourseCategoryInfo() {
        return courseCategoryInfo;
    }

    public void setCourseCategoryInfo(String courseCategoryInfo) {
        this.courseCategoryInfo = courseCategoryInfo;
    }

    public String getCourseCategoryStatus() {
        return courseCategoryStatus;
    }

    public void setCourseCategoryStatus(String courseCategoryStatus) {
        this.courseCategoryStatus = courseCategoryStatus;
    }

    public Boolean getIsAlreadyBought() {
        return isAlreadyBought;
    }

    public void setIsAlreadyBought(Boolean isAlreadyBought) {
        this.isAlreadyBought = isAlreadyBought;
    }

    public Boolean getIsCourseExpired() {
        return isCourseExpired;
    }

    public void setIsCourseExpired(Boolean isCourseExpired) {
        this.isCourseExpired = isCourseExpired;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}