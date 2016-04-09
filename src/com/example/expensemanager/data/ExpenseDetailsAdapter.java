package com.example.expensemanager.data;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.expensemanager.R;

public class ExpenseDetailsAdapter extends BaseAdapter {
	private ArrayList<CategoryWiseExpense> mExpList;
	 private LayoutInflater mInflater;
	 
	 public ExpenseDetailsAdapter( ArrayList<CategoryWiseExpense> listSum, LayoutInflater inflator) {
		this.mExpList = listSum;
		this.mInflater = inflator;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mExpList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mExpList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 final ViewItem item;

		  if (convertView == null) {
		   convertView = mInflater.inflate(R.layout.expense_details_item, null);
		   item = new ViewItem();

		   item.catViewItem = (TextView) convertView
		     .findViewById(R.id.textViewCatE);

		   item.expenseViewItem = (TextView) convertView
		     .findViewById(R.id.textViewExpenseE);

		   		   convertView.setTag(item);
		  } else {
		   item = (ViewItem) convertView.getTag();
		  }
		  CategoryWiseExpense currExpenseRow = mExpList.get(position);

		  
		  item.catViewItem.setText(currExpenseRow.categoryName);
		  item.expenseViewItem.setText(""+String.format("%.2f",currExpenseRow.expense));
		  
		  

		  return convertView;
	}
	private class ViewItem {
		  TextView catViewItem;
		
		  TextView expenseViewItem;
		
		  
		 }

}
