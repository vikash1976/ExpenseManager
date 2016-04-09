package com.example.expensemanager.data;

public class MonthlyExpense {
	public int day;
	public double expense;
	public String catName;
	public String notes;
	
	public MonthlyExpense(int d, double e, String catName, String notes) {
		this.day = d;
		this.expense =e;
		this.catName = catName;
		this.notes = notes;
	}

}
