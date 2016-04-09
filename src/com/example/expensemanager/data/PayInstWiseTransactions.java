package com.example.expensemanager.data;

public class PayInstWiseTransactions {
	public String instrName;
	public int noOfTransactions;
	public double sumAmount;
	
	public PayInstWiseTransactions(String name, int number, double amount) {
		this.instrName = name;
		this.noOfTransactions = number;
		this.sumAmount = amount;
	}

}
