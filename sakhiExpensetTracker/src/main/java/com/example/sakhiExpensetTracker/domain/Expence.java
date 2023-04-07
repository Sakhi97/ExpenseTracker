package com.example.sakhiExpensetTracker.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate; 

@Entity
public class Expence {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double amount;
    private LocalDate date; 
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appuserId")
    private AppUser appuser;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    public Expence() {
        super();
    }

    public Expence(double amount, LocalDate date, String remark, Category category) { 
        super();
        this.amount = amount;
        this.date = date;
        this.remark = remark;
        this.category = category;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}


	public AppUser getAppuser() {
		return appuser;
	}

	public void setAppuser(AppUser appuser) {
		this.appuser = appuser;
	}
}