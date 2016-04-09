package com.example.expensemanager.data;
@SuppressWarnings("unused")
public class PerformanceReport {
	
	private final String[] months = { "January", "February", "March",
			"April", "May", "June", "July", "August", "September",
			"October", "November", "December" };
	public int year;
	public int month;
	public String monthName;
	
	public double expensePerMonth;
	public double budgetPerMonth;
	public double deviation;
	public PerformanceReport(int year, int month, double exp, double budget, double deviation) {
		this.month = month;
		this.year = year;
		this.expensePerMonth = exp;
		this.budgetPerMonth = budget;
		this.monthName = months[month-1];
		this.deviation = deviation;
		
	}

}
