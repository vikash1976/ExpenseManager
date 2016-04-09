package com.example.expensemanager.data;

public class CategoryWiseExpense {

	public int catCode;
	public double expense;
	public String categoryName;
	
	public CategoryWiseExpense(int catCode, double expense, String categoryName){
		this.catCode = catCode;
		this.expense = expense;
		this.categoryName = categoryName;
	}
	
}
