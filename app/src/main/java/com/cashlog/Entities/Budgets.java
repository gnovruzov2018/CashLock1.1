package com.cashlog.Entities;

public class Budgets {
    private int budget_id;
    private String budget_name;
    private double overall_income;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }



    public Budgets(String budget_name, double overall_income,int status) {
        this.budget_name = budget_name;
        this.overall_income = overall_income;
        this.status=status;
    }

    public Budgets(int budget_id) {
    this.budget_id=budget_id;
    }
    public Budgets() {
    }

    public Budgets(int budget_id,String budget_name, double overall_income)  {
        this.budget_name = budget_name;
        this.overall_income = overall_income;
        this.budget_id = budget_id;
    }

    public String getBudget_name() {
        return budget_name;
    }

    public void setBudget_name(String budget_name) {
        this.budget_name = budget_name;
    }



    public double getOverall_income() {
        return overall_income;
    }

    public void setOverall_income(double overall_income) {
        this.overall_income = overall_income;
    }

    public int getBudget_id() {
        return budget_id;
    }

    public void setBudget_id(int budget_id) {

        this.budget_id = budget_id;
    }
}
