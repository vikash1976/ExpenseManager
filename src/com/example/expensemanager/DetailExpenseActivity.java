package com.example.expensemanager;

import java.security.PublicKey;
import java.util.ArrayList;

import com.example.expensemanager.data.CatDayExpNotes;
import com.example.expensemanager.data.CategoryExpenseDetailsAdapter;
import com.example.expensemanager.data.CategoryWiseExpense;
import com.example.expensemanager.data.CategoryWisePieChart;
import com.example.expensemanager.data.CommonUtilities;
import com.example.expensemanager.data.DBHelper;
import com.example.expensemanager.data.DayExpNotes;
import com.example.expensemanager.data.ExpenseDetailsAdapter;
import com.example.expensemanager.data.MonthlyExpense;
import com.example.expensemanager.data.PerformanceSummaryAdapter;
import com.example.expensemanager.data.YearlyBarChartBvE;

import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DetailExpenseActivity extends ActionBarActivity {

	//private ArrayList<MonthlyExpense> mEList = new ArrayList<MonthlyExpense>();
	private ArrayList<CategoryWiseExpense> mEList = new ArrayList<CategoryWiseExpense>();
	DBHelper myDB;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_expense);
		//TextView headerMsg = (TextView)findViewById(R.id.textView1Msg);
		myDB = new DBHelper(getApplicationContext());
		final int forYear = getIntent().getIntExtra("EXP_YEAR", -1);
		final int forMonth = getIntent().getIntExtra("EXP_MONTH", 0);
		//headerMsg.setText(this.getString(R.string.monthly_expense_header)+": "+forYear+", "+String.format("%1$02d",forMonth));
		mEList = myDB.getExpenseOfMYCatWise(forMonth, forYear);
		ListView listSummaryView = (ListView) findViewById(R.id.listExpDetails);
		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup)inflater.inflate(R.layout.exp_details_header, listSummaryView, false);
		TextView headerMsg = (TextView)header.findViewById(R.id.textView1Msg);
		headerMsg.setText(this.getString(R.string.monthly_expense_header)+": "+forYear+", "+String.format("%1$02d",forMonth));
		listSummaryView.addHeaderView(header, null, false);
		
		listSummaryView.setAdapter(new ExpenseDetailsAdapter(mEList,
				getLayoutInflater()));
		listSummaryView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CategoryWiseExpense categoryWiseExpense = (CategoryWiseExpense)parent.getItemAtPosition(position);
				showCatWiseExpenseDialog(categoryWiseExpense.categoryName, forMonth, forYear);
				
				
			}

			
		});

		LinearLayout  LayoutToDisplayChart=(LinearLayout)findViewById(R.id.pieChartArea);
		Intent achartIntent = new CategoryWisePieChart().execute(DetailExpenseActivity.this,LayoutToDisplayChart, mEList, forMonth, forYear);
	}
	
	public void showCatWiseExpenseDialog(String categoryName, int month, int year) {
		// TODO Auto-generated method stub
		ArrayList<CatDayExpNotes> dayExpNotesList = myDB.getCatWiseExpenseItems(categoryName, month, year);
		final Dialog dialog = new Dialog(DetailExpenseActivity.this);
		//final String monthN = monthName;
		//final String yearN = year;
		//dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setTitle(categoryName);
		dialog.setContentView(R.layout.cat_expense_details);
		ListView catWiseExpListView = (ListView)dialog.findViewById(R.id.listViewCatExp);
		ViewGroup header = (ViewGroup) dialog.getLayoutInflater().inflate(
				R.layout.cat_exp_details_header, catWiseExpListView, false);
		catWiseExpListView.addHeaderView(header, null, false);
		catWiseExpListView.setAdapter(new CategoryExpenseDetailsAdapter(dayExpNotesList, dialog.getLayoutInflater()));
		dialog.show();
		
		Button closeButton = (Button)dialog.findViewById(R.id.buttonCatClose);
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_expense, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
