package com.example.bankApp.data.model;

import java.util.Date;

public class Card {
    private final String id;
    private String currency;
    private float total;
    private String ownerName;
    private final Date createdat;
    private Date updatedat;
    private String[] userId;
    private LoggedInUser user;
    private Expense expense;
    private Income income;
    private LoggedInUser[] Users;

    public Card(String id, Date createdat, String currency, float total, String ownerName, Date updatedat, String[] userId, LoggedInUser user, Expense expense, Income income, LoggedInUser[] users) {
        this.id = id;
        this.createdat = createdat;
        this.currency = currency;
        this.total = total;
        this.ownerName = ownerName;
        this.updatedat = updatedat;
        this.userId = userId;
        this.user = user;
        this.expense = expense;
        this.income = income;
        this.Users = users;
    }

    public Card(String id, String currency, float total, String ownerName, Date createdat, Date updatedat, String[] userId, LoggedInUser user, Expense expense, Income income) {
        this.id = id;
        this.currency = currency;
        this.total = total;
        this.ownerName = ownerName;
        this.createdat = createdat;
        this.updatedat = updatedat;
        this.userId = userId;
        this.user = user;
        this.expense = expense;
        this.income = income;
        this.Users=null;
    }

    public String getId() {
        return id;
    }

    public Date getCreatedat() {
        return createdat;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Date getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }

    public LoggedInUser getUser() {
        return user;
    }

    public void setUser(LoggedInUser user) {
        this.user = user;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public Income getIncome() {
        return income;
    }

    public void setIncome(Income income) {
        this.income = income;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String[] getUserId() {
        return userId;
    }

    public void setUserId(String[] userId) {
        this.userId = userId;
    }

    public LoggedInUser[] getUsers() {
        return Users;
    }

    public void setUsers(LoggedInUser[] users) {
        Users = users;
    }
}
