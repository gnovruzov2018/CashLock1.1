package com.cashlog.Entities;

/**
 * Created by Nahid on 23.11.2016.
 */

public class Expenses

{

    private int id;
    private String payee;
    private double amount;
    private String category;
    private String date;
    private String note;
    private String time;
    private float rating;

    public int getBudget_id() {
        return budget_id;
    }

    public void setBudget_id(int budget_id) {
        this.budget_id = budget_id;
    }

    private int budget_id;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }



    public Expenses(int id, String payee, double amount, String category, String date, String note, String time, float rating,int budget_id) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.id = id;
        this.note = note;
        this.payee = payee;
        this.time = time;
        this.rating = rating;
        this.budget_id=budget_id;
    }

    public Expenses() {
    }

    public Expenses(String payee, double amount, String category, String date, String note, String time, float rating,int budget_id) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
        this.payee = payee;
        this.time = time;
        this.rating = rating;
        this.budget_id=budget_id;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }


    public void setNote(String note) {
        this.note = note;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

}
