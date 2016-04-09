package com.example.expensemanager.data;

import java.io.FileOutputStream;
import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.expensemanager.DetailExpenseActivity;
import com.example.expensemanager.R;

public class CategoryWisePieChart {

	private GraphicalView mChartView2;
	private Activity _mContext;
	DBHelper myDB;
	int month;
	int year;

	// static int count=5;

	public Intent execute(Activity context, LinearLayout parent,
			final ArrayList<CategoryWiseExpense> catExpList, int month, int year) {
		_mContext = context;
		this.month = month;
		this.year = year;
		myDB = new DBHelper(context);
		
		int[] colors = new int[catExpList.size()];
		ArrayList<CategoryData> catList = myDB.getAllCategoriesName();

		for (int i = 0; i < catExpList.size(); i++) {
			colors[i] = catList.get(i).colorCode;
		}
		final DefaultRenderer renderer = buildCategoryRenderer(colors);
		renderer.setPanEnabled(false);// Disable User Interaction
		renderer.setLabelsColor(Color.WHITE);
		renderer.setShowLabels(true);
		renderer.setScale((float) 1.0);
		renderer.setZoomButtonsVisible(true);
		renderer.setDisplayValues(true);
		renderer.setChartTitle(context.getString(R.string.pie_chart_title));
		renderer.setChartTitleTextSize(20);

		renderer.setClickEnabled(true);
		// renderer.setChartTitle("Total Assets");
		renderer.setLabelsTextSize(12);
		final CategorySeries categorySeries = new CategorySeries("Category");
		for (int i = 0; i < catExpList.size(); i++) {

			categorySeries.add(catExpList.get(i).categoryName,
					catExpList.get(i).expense);
		}

		mChartView2 = ChartFactory.getPieChartView(context, categorySeries,
				renderer);
		mChartView2.setPadding(10, 0, 10, 0);
		mChartView2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SeriesSelection seriesSelection = mChartView2
						.getCurrentSeriesAndPoint();
				if (seriesSelection == null) {
					Toast.makeText(_mContext, "No chart element selected",
							Toast.LENGTH_SHORT).show();
				} else {
					for (int i = 0; i < categorySeries.getItemCount(); i++) {
						renderer.getSeriesRendererAt(i).setHighlighted(
								i == seriesSelection.getPointIndex());

					}
					mChartView2.repaint();
					/*
					 * Toast.makeText( _mContext, "Chart data point index " +
					 * seriesSelection.getPointIndex() + " selected" +
					 * " point value=" + seriesSelection.getValue() +
					 * catExpList.
					 * get(seriesSelection.getPointIndex()).instrName,
					 * Toast.LENGTH_SHORT).show();
					 */
					showTransactionsForCategory(catExpList.get(seriesSelection
							.getPointIndex()).categoryName);

				}
			}
		});
		parent.addView(mChartView2);

		return ChartFactory.getPieChartIntent(context, categorySeries,
				renderer, null);
	}

	protected void showTransactionsForCategory(String categoryName) {
		ArrayList<CatDayExpNotes> dayExpNotesList = myDB
				.getCatWiseExpenseItems(categoryName, month, year);
		final Dialog dialog = new Dialog(_mContext);
		// final String monthN = monthName;
		// final String yearN = year;
		// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setTitle(categoryName);
		dialog.setContentView(R.layout.cat_expense_details);
		ListView catWiseExpListView = (ListView) dialog
				.findViewById(R.id.listViewCatExp);
		ViewGroup header = (ViewGroup) dialog.getLayoutInflater().inflate(
				R.layout.cat_exp_details_header, catWiseExpListView, false);
		catWiseExpListView.addHeaderView(header, null, false);
		catWiseExpListView.setAdapter(new CategoryExpenseDetailsAdapter(
				dayExpNotesList, dialog.getLayoutInflater()));
		dialog.show();

		Button closeButton = (Button) dialog.findViewById(R.id.buttonCatClose);
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				myDB.close();
				dialog.dismiss();
			}
		});
	}

	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);

		}
		return renderer;
	}
}
