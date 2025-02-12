package com.example.bankApp.data.model;

import java.util.Date;

public class Transaction {
    private final String id;
    private String category;
    private String description;
    private String vendor;
    private double total;
    private String userId;
    private String accountId;
    private int repeatAmmount;
    private String repeateMetric;
    private Date repeateStart;
    private Date repeatEnd;
    private final Date createdAt;
    private Date updatedAt;

    public Transaction(String id, Date createdAt, String category, String description, String vendor, float total, String userId, String accountId, int repeatAmmount, String repeateMetric, Date repeateStart, Date repeatEnd, Date updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.category = category;
        this.description = description;
        this.vendor = vendor;
        this.total = total;
        this.userId = userId;
        this.accountId = accountId;
        this.repeatAmmount = repeatAmmount;
        this.repeateMetric = repeateMetric;
        this.repeateStart = repeateStart;
        this.repeatEnd = repeatEnd;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getRepeatAmmount() {
        return repeatAmmount;
    }

    public void setRepeatAmmount(int repeatAmmount) {
        this.repeatAmmount = repeatAmmount;
    }

    public String getRepeateMetric() {
        return repeateMetric;
    }

    public void setRepeateMetric(String repeateMetric) {
        this.repeateMetric = repeateMetric;
    }

    public Date getRepeateStart() {
        return repeateStart;
    }

    public void setRepeateStart(Date repeateStart) {
        this.repeateStart = repeateStart;
    }

    public Date getRepeatEnd() {
        return repeatEnd;
    }

    public void setRepeatEnd(Date repeatEnd) {
        this.repeatEnd = repeatEnd;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", vendor='" + vendor + '\'' +
                ", total=" + total +
                ", userId='" + userId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", repeatAmmount=" + repeatAmmount +
                ", repeateMetric='" + repeateMetric + '\'' +
                ", repeateStart=" + repeateStart +
                ", repeatEnd=" + repeatEnd +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
