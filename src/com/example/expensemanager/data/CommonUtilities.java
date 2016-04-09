package com.example.expensemanager.data;

public final class CommonUtilities {
	
	private static int monthNum;
	private static int payInstId;
	
	public static int getMonthNum(String monthName){
		switch(monthName){
        case "January": 
        	monthNum = 1;
        	break;
        case "February": 
        	monthNum = 2;
        	break;
        case "March": 
        	monthNum = 3;
        	break;
        case "April": 
        	monthNum = 4;
        	break;
        case "May": 
        	monthNum = 5;
        	break;
        case "June": 
        	monthNum = 6;
        	break;
        case "July": 
        	monthNum = 7;
        	break;
        case "August": 
        	monthNum = 8;
        	break;
        case "September": 
        	monthNum = 9;
        	break;
        case "October": 
        	monthNum = 10;
        	break;
        case "November": 
        	monthNum = 11;
        	break;
        case "December": 
        	monthNum = 12;
        	break;
        }
		return monthNum;

	}
	
	public static int getPayInstId(String payInstName){
		switch (payInstName) {
		case "Credit Card":
			payInstId = 0;
			break;
		case "Debit Card":
			payInstId = 1;
			break;
		case "Cash":
			payInstId = 2;
			break;
		case "Internet Banking":
			payInstId = 3;
			break;
		case "Cheque":
			payInstId = 4;
			break;
		case "Other Cards":
			payInstId = 5;
			break;

		default:
			payInstId = 6;
			break;
		}
		return payInstId;
		
	}

}
