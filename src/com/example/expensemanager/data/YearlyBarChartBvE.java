package com.example.expensemanager.data;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.expensemanager.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class YearlyBarChartBvE {
	private String[] mMonth = new String[] { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	private GraphicalView mBarChartView;

	public Intent execute(Context context, LinearLayout parent,
			ArrayList<PerformanceReport> dataSeries, int year) {
		int[] x = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
		

		double expense[] = new double[12];
		double budget[] = new double[12];
		//int j = 0;
		for (PerformanceReport d : dataSeries) {
			expense[d.month -1] = d.expensePerMonth;
			budget[d.month -1] = d.budgetPerMonth;
			//j++;

		}

		// Creating an XYSeries for Income
		XYSeries budgetSeries = new XYSeries("Budget");
		// Creating an XYSeries for Expense
		XYSeries expenseSeries = new XYSeries("Expense");
		// Adding data to Income and Expense Series
		for (int i = 0; i < x.length; i++) {
			budgetSeries.add(i, budget[i]);
			expenseSeries.add(i, expense[i]);
		}

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(budgetSeries);
		// Adding Expense Series to dataset
		dataset.addSeries(expenseSeries);

		// Creating XYSeriesRenderer to customize incomeSeries
		XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
		incomeRenderer.setColor(Color.parseColor("#52D017"));
		incomeRenderer.setFillPoints(true);
		incomeRenderer.setLineWidth(2);
		incomeRenderer.setDisplayChartValues(true);

		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
		expenseRenderer.setColor(Color.parseColor("#FF00FF"));
		expenseRenderer.setFillPoints(true);
		expenseRenderer.setLineWidth(2);
		expenseRenderer.setDisplayChartValues(true);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle(context.getString(R.string.bar_chart_title));
		multiRenderer.setChartTitleTextSize(20);
		multiRenderer.setXTitle(context.getString(R.string.bar_x_title) + year);
		multiRenderer.setYTitle(context.getString(R.string.bar_y_title)); 
		multiRenderer.setZoomButtonsVisible(true);
		multiRenderer.setScale((float) 1.0);
		multiRenderer.setYAxisMax(10000);
		for (int i = 0; i < x.length; i++) {
			multiRenderer.addXTextLabel(i, mMonth[i]);
		}

		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to
		// multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(incomeRenderer);
		multiRenderer.addSeriesRenderer(expenseRenderer);

		mBarChartView = ChartFactory.getBarChartView(context, dataset,
				multiRenderer, Type.DEFAULT);
		mBarChartView.setPadding(10, 0, 10, 0);
		parent.addView(mBarChartView);
		// Creating an intent to plot bar chart using dataset and
		// multipleRenderer
		Intent intent = ChartFactory.getBarChartIntent(context, dataset,
				multiRenderer, Type.DEFAULT);
		return intent;
	}

}
