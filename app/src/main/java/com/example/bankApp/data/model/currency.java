package com.example.bankApp.data.model;

import java.util.Date;
import java.util.Map;

public class currency {
    private Date date;
    private Map<String, Double> eur;

    public currency(Date date, Map<String, Double> eur) {
        this.date = date;
        this.eur = eur;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, Double> getEur() {
        return eur;
    }

    public void setEur(Map<String, Double> eur) {
        this.eur = eur;
    }
}

