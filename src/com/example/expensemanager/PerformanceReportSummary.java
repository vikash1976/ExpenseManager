package com.example.expensemanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import com.example.expensemanager.data.DBHelper;
import com.example.expensemanager.data.PerformanceReport;
import com.example.expensemanager.data.PerformanceSummaryAdapter;
import com.example.expensemanager.data.YearlyBarChartBvE;

public class PerformanceReportSummary extends ActionBarActivity {

	protected static final String EXP_MONTH = "EXP_MONTH";
	protected static final String EXP_YEAR = "EXP_YEAR";
	private ArrayList<PerformanceReport> listSummary = new ArrayList<PerformanceReport>();
	int forYear;
	int forMonth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_performance_report_summary);
		DBHelper myDB = new DBHelper(getApplicationContext());
		forYear = getIntent().getIntExtra("FOR_YEAR", -1);
		forMonth = getIntent().getIntExtra("FOR_MONTH", -1);
		listSummary = myDB.getPerformanceOfYear(forYear, forMonth);
		ListView listSummaryView = (ListView) findViewById(R.id.listPerfRepSum);
		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup)inflater.inflate(R.layout.perf_report_header, listSummaryView, false);
		listSummaryView.addHeaderView(header, null, false);
		
		ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.perf_report_footer, listSummaryView, false);
		listSummaryView.addFooterView(footer, null, false);
		
		Button barChartButton = (Button)footer.findViewById(R.id.buttonChartView);
		barChartButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showBarChartView();
				
			}
		});
		
		
		listSummaryView.setAdapter(new PerformanceSummaryAdapter(listSummary,
				getLayoutInflater()));
		//listSummaryView.setClickable(true);

		listSummaryView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent expenseDetailsIntent = new Intent(
						getApplicationContext(), DetailExpenseActivity.class);
				expenseDetailsIntent.putExtra(EXP_MONTH,
						listSummary.get(position-1).month);
				expenseDetailsIntent.putExtra(EXP_YEAR,
						listSummary.get(position-1).year);
				
				startActivity(expenseDetailsIntent);
			}
		});
		
		

	}
	
	private void showBarChartView() {
		final Dialog dialog = new Dialog(PerformanceReportSummary.this);
		//final String dayOfMonth1 = expenseDay;
		//final double expense = currExpense;

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.bar_chart_fragment);
		dialog.setCanceledOnTouchOutside(true);
		LinearLayout  LayoutToDisplayChart=(LinearLayout)dialog.findViewById(R.id.barChartArea);
		Intent achartIntent = new YearlyBarChartBvE().execute(getApplicationContext(),LayoutToDisplayChart, listSummary, forYear);
		dialog.show();
		
		Button barChartCloseButton = (Button)dialog.findViewById(R.id.buttonChartClose);
		barChartCloseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			dialog.dismiss();
				
			}
		});
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.performance_report_summary, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.chartView) {
			//LinearLayout  LayoutToDisplayChart=(LinearLayout)findViewById(R.id.barChartArea);
			//Intent achartIntent = new YearlyBarChartBvE().execute(getApplicationContext(),LayoutToDisplayChart, listSummary, forYear);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
