package com.example.expensemanager.data;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.expensemanager.R;

public class DailyExpenseDetailsAdapter extends BaseAdapter {
	private ArrayList<DayExpNotes> mExpList;
	 private LayoutInflater mInflater;
	 
	 public DailyExpenseDetailsAdapter( ArrayList<DayExpNotes> listSum, LayoutInflater inflator) {
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
		   convertView = mInflater.inflate(R.layout.day_exp_details_item, null);
		   item = new ViewItem();

		   
		   item.expenseViewItem = (TextView) convertView
		     .findViewById(R.id.textViewExp);
		   
		   item.notesViewItTextView = (TextView) convertView
				     .findViewById(R.id.textViewNotes);

		   		   convertView.setTag(item);
		  } else {
		   item = (ViewItem) convertView.getTag();
		  }
		  DayExpNotes currExpenseRow = mExpList.get(position);

		  
		  
		  item.expenseViewItem.setText(""+String.format("%.2f",currExpenseRow.expense));
		  item.notesViewItTextView.setText(currExpenseRow.notes);
		  

		  return convertView;
	}
	private class ViewItem {
		  
		  TextView expenseViewItem;
		  TextView notesViewItTextView;
		
		  
		 }

}
