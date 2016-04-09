package com.example.expensemanager.data;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.expensemanager.R;

public class PerformanceSummaryAdapter extends BaseAdapter {
	private ArrayList<PerformanceReport> mPerfSummaryList;
	 private LayoutInflater mInflater;
	 
	 public PerformanceSummaryAdapter( ArrayList<PerformanceReport> listSum, LayoutInflater inflator) {
		this.mPerfSummaryList = listSum;
		this.mInflater = inflator;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPerfSummaryList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mPerfSummaryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 final ViewItem item;

		  if (convertView == null) {
		   convertView = mInflater.inflate(R.layout.performance_summary_item, null);
		   item = new ViewItem();

		   item.yearViewItem = (TextView) convertView
		     .findViewById(R.id.textViewYear);

		   item.monthViewItem = (TextView) convertView
		     .findViewById(R.id.textViewMonth);

		   item.expenseViewItem = (TextView) convertView
		     .findViewById(R.id.textViewExpense);

		   item.budgetViewItem = (TextView) convertView
				     .findViewById(R.id.textViewBudget);
		   
		   item.deviationViewItem = (TextView) convertView
				     .findViewById(R.id.textViewDeviation);
		   convertView.setTag(item);
		  } else {
		   item = (ViewItem) convertView.getTag();
		  }
		  PerformanceReport currPerfRep = mPerfSummaryList.get(position);

		  item.yearViewItem.setText(""+currPerfRep.year);
		  item.monthViewItem.setText(""+String.format("%1$02d",currPerfRep.month));
		  item.expenseViewItem.setText(""+String.format("%.2f",currPerfRep.expensePerMonth));
		  item.budgetViewItem.setText(""+String.format("%.2f",currPerfRep.budgetPerMonth));
		  item.deviationViewItem.setText(""+String.format("%.2f",currPerfRep.deviation));
		  if(currPerfRep.deviation < 0){
			  convertView.setBackgroundColor(Color.argb(0x80, 0x20, 0xa0, 0x40));
		  }
		  

		  return convertView;
	}
	private class ViewItem {
		  TextView monthViewItem;
		  TextView yearViewItem;
		  TextView expenseViewItem;
		  TextView budgetViewItem;
		  TextView deviationViewItem;
		 }
	@Override
	public boolean areAllItemsEnabled() {
	    return true;
	}

	@Override
	public boolean isEnabled(int position) {
	    return true;
	}

}
