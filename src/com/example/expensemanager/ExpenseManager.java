package com.example.expensemanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.achartengine.model.CategorySeries;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.expensemanager.ExpenseFragment.ExpenseListener;
import com.example.expensemanager.UtilityFragment.UtilityListener;
import com.example.expensemanager.data.CategoryData;
import com.example.expensemanager.data.CategoryWiseExpense;
import com.example.expensemanager.data.CommonUtilities;
import com.example.expensemanager.data.DBHelper;
import com.example.expensemanager.data.DailyExpenseDetailsAdapter;
import com.example.expensemanager.data.DayExpNotes;
import com.example.expensemanager.data.EditTextLocker;
import com.example.expensemanager.data.ExpenseCategoryAdapter;
import com.example.expensemanager.data.MonthlyExpense;
import com.example.expensemanager.data.PayInstWiseAmountPieChart;
import com.example.expensemanager.data.PayInstWiseTransPieChart;
import com.example.expensemanager.data.PayInstWiseTransactions;
import com.example.expensemanager.data.PerformanceReport;
import com.example.expensemanager.data.YearlyBarChartBvE;

public class ExpenseManager extends ActionBarActivity implements
		OnClickListener, UtilityListener {

	private static final String tag = "MyExpenseManager";

	private TextView spentSoFar;
	private TextView budgetedFor;
	private TextView fairingPoint;
	private TextView currentMonth;
	private Button exportCSVCat;
	private Button exportCSVDay;
	private Button buttonToday;
	private Button shareOnTwitter;
	private Button payInstWise;
	private Button selectedDayMonthYearButton;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private ImageView prevYear;
	private ImageView nextYear;
	private Button settings;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	String selectedCategory;

	private int month, year;
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";

	private static final String FOR_YEAR = "FOR_YEAR";

	private static final String FOR_MONTH = "FOR_MONTH";

	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	UtilityFragment settingsFrag;
	ExpenseFragment expenseFragment;
	DBHelper myDB;
	Context applicationContext;
	HashMap<String, Integer> categoryMap = new HashMap<String, Integer>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_expense_manager);
		applicationContext = getApplicationContext();
		String[] categoryList = new String[] { "Food/Drinks", "Healthcare",
				"Transportation", "Donations/Gifts", "Grocery",
				"Entertainment", "Education", "Personal/Debt Payments",
				"Savings and Insurance", "Clothing/Personal Care",
				"Utilities/Bills", "Housing(EMI/Rent)", "Miscellaneous" };
		Resources resources = applicationContext.getResources();
		int[] catImages = new int[] { resources.getColor(R.color.purple),
				resources.getColor(R.color.red),
				resources.getColor(R.color.green),
				resources.getColor(R.color.darksalmon),
				resources.getColor(R.color.darkorange),
				resources.getColor(R.color.darkturquoise),
				resources.getColor(R.color.lime),
				resources.getColor(R.color.cornflowerblue),
				resources.getColor(R.color.greenyellow),
				resources.getColor(R.color.olivedrab),
				resources.getColor(R.color.lightsteelblue),
				resources.getColor(R.color.paleturquoise),
				resources.getColor(R.color.mediumaquamarine) };

		myDB = new DBHelper(getApplicationContext());
		categoryMap = myDB.getAllCategories();
		if (categoryMap.isEmpty()) {
			myDB.createCategories(categoryList, catImages);
			categoryMap = myDB.getAllCategories();
		}

		_calendar = Calendar.getInstance(Locale.getDefault());
		this.month = _calendar.get(Calendar.MONTH) + 1;
		this.year = _calendar.get(Calendar.YEAR);

		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
				+ year);

		selectedDayMonthYearButton = (Button) this
				.findViewById(R.id.selectedDayMonthYear);
		selectedDayMonthYearButton.setText("Selected: ");

		prevYear = (ImageView) this.findViewById(R.id.prevYear);
		prevYear.setOnClickListener(this);

		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		nextYear = (ImageView) this.findViewById(R.id.nextYear);
		nextYear.setOnClickListener(this);

		settings = (Button) this.findViewById(R.id.settings);
		settings.setOnClickListener(this);

		exportCSVCat = (Button) this.findViewById(R.id.exportCSVCat);
		exportCSVCat.setOnClickListener(this);

		exportCSVDay = (Button) this.findViewById(R.id.exportCSVDay);
		exportCSVDay.setOnClickListener(this);

		buttonToday = (Button) this.findViewById(R.id.buttonToday);
		buttonToday.setOnClickListener(this);

		shareOnTwitter = (Button) this.findViewById(R.id.shareOnTwitter);
		shareOnTwitter.setOnClickListener(this);
		
		payInstWise = (Button) this.findViewById(R.id.payInstWise);
		payInstWise.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);

		// Initialised
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);

		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);

		/*
		 * if (findViewById(R.id.fragment_container) != null) {
		 * 
		 * // However, if we're being restored from a previous state, // then we
		 * don't need to do anything and should return or else // we could end
		 * up with overlapping fragments. if (savedInstanceState != null) {
		 * return; } }
		 */
		spentSoFar = (TextView) findViewById(R.id.totalExpense);
		budgetedFor = (TextView) findViewById(R.id.budgetedFor);
		fairingPoint = (TextView) findViewById(R.id.fairingPoint);
		setStatusMessages();
		settingsFrag = new UtilityFragment();

	}

	public void setStatusMessages() {
		String spentSoFarMsg = this.getString(R.string.spent) + ": ";
		String budgetedForMsg = this.getString(R.string.budgeted) + ": ";
		String fairingPointrMsg = this.getString(R.string.stats) + ": ";
		double expenseSoFar = myDB.getExpenseOfMY(month, year);
		spentSoFar.setText(spentSoFarMsg + String.format("%.2f", expenseSoFar));
		double budgetOfTheMonth = myDB.getBudgetOfMY(month, year);
		budgetedForMsg += budgetOfTheMonth == -1.0 ? "Not Yet" : String.format(
				"%.2f", budgetOfTheMonth);
		budgetedFor.setText(budgetedForMsg);
		if (expenseSoFar != 0.0 && budgetOfTheMonth != -1.0) {
			fairingPointrMsg += String.format("%.2f",
					(expenseSoFar / budgetOfTheMonth) * 100) + "%";
			fairingPoint.setText(fairingPointrMsg);
		} else {
			fairingPoint.setText(fairingPointrMsg + 0.00);
		}

	}

	/**
	 * 
	 * @param month
	 * @param year
	 */
	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(applicationContext,
				R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));
		this.month = month;
		this.year = year;
		adapter.notifyDataSetChanged();
		setStatusMessages();
		calendarView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v == prevMonth) {
			if (month <= 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
			/*
			 * fragmentManager = getFragmentManager(); fragmentTransaction =
			 * fragmentManager.beginTransaction();
			 * fragmentTransaction.remove(settingsFrag);
			 * 
			 * fragmentTransaction.commit();
			 */
		}
		if (v == nextMonth) {
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
			/*
			 * fragmentManager = getFragmentManager(); fragmentTransaction =
			 * fragmentManager.beginTransaction();
			 * fragmentTransaction.remove(settingsFrag);
			 * 
			 * fragmentTransaction.commit();
			 */
		}
		if (v == prevYear) {

			year--;
			Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
			/*
			 * fragmentManager = getFragmentManager(); fragmentTransaction =
			 * fragmentManager.beginTransaction();
			 * fragmentTransaction.remove(settingsFrag);
			 * 
			 * fragmentTransaction.commit();
			 */
		}
		if (v == nextYear) {

			year++;
			Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
					+ month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
			/*
			 * fragmentManager = getFragmentManager(); fragmentTransaction =
			 * fragmentManager.beginTransaction();
			 * fragmentTransaction.remove(settingsFrag);
			 * 
			 * fragmentTransaction.commit();
			 */
		}
		if (v == settings) {
			/*
			 * fragmentManager = getFragmentManager(); fragmentTransaction =
			 * fragmentManager.beginTransaction();
			 * fragmentTransaction.add(R.id.fragment_container, settingsFrag,
			 * "HELLO");
			 * 
			 * fragmentTransaction.commit();
			 */
			Intent toPerSumAct = new Intent(getApplicationContext(),
					PerformanceReportSummary.class);
			toPerSumAct.putExtra(FOR_YEAR, year);
			toPerSumAct.putExtra(FOR_MONTH, month);
			startActivity(toPerSumAct);
		}
		if (v == exportCSVCat) {
			exportAsCSVCatWise();
		}
		if (v == exportCSVDay) {
			exportAsCSVDayWise();
		}
		if (v == buttonToday) {
			Calendar calendarTemp = Calendar.getInstance(Locale.getDefault());
			int monthTemp = calendarTemp.get(Calendar.MONTH) + 1;
			int yearTemp = calendarTemp.get(Calendar.YEAR);
			setGridCellAdapterToDate(monthTemp, yearTemp);
		}
		if (v == payInstWise) {
			showPieChartView();
		}
		if (v == shareOnTwitter) {
			double expenseSoFar = myDB.getExpenseOfMY(month, year);
			// spentSoFar.setText(spentSoFarMsg + String.format("%.2f",
			// expenseSoFar));
			double budgetOfTheMonth = myDB.getBudgetOfMY(month, year);
			// budgetedForMsg += budgetOfTheMonth == -1.0 ? "Not Yet" :
			// String.format(
			// "%.2f", budgetOfTheMonth);
			// budgetedFor.setText(budgetedForMsg);
			String tweetMsg = "";
			if (expenseSoFar != 0.0 && budgetOfTheMonth != -1.0) {
				tweetMsg = "Budgeted: "
						+ String.format("%.2f", budgetOfTheMonth)
						+ ", Spent: "
						+ String.format("%.2f", expenseSoFar)
						+ ", faired as: "
						+ String.format("%.2f",
								(expenseSoFar / budgetOfTheMonth) * 100) + "%";

			} else {
				tweetMsg = "";
			}
			// Create intent using ACTION_VIEW and a normal Twitter url:
			String tweetUrl = String.format(
					"https://twitter.com/intent/tweet?text=%s&url=%s",
					urlEncode("Look how i manage my expenditure for: " + month
							+ ", " + year + " " + tweetMsg),
					urlEncode("https://www.google.fi/"));
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

			// Narrow down to official Twitter app, if available:
			List<ResolveInfo> matches = getPackageManager()
					.queryIntentActivities(intent, 0);
			for (ResolveInfo info : matches) {
				if (info.activityInfo.packageName.toLowerCase().startsWith(
						"com.twitter")) {
					intent.setPackage(info.activityInfo.packageName);
				}
			}

			if (tweetMsg.isEmpty()) {
				Toast.makeText(applicationContext,
						"Nothing significant to tweet.", Toast.LENGTH_LONG)
						.show();
			} else {
				startActivity(intent);
			}
		}
		selectedDayMonthYearButton.setText(this
				.getString(R.string.selected_date_message) + ": ");

	}
	
	private void showPieChartView() {
		final Dialog dialog = new Dialog(ExpenseManager.this);
		//final String dayOfMonth1 = expenseDay;
		//final double expense = currExpense;

		ArrayList<PayInstWiseTransactions> payInstWiseTransactions = myDB.getPayInstWiseData(month, year);
		if(payInstWiseTransactions.isEmpty()){
			Toast.makeText(applicationContext, "No expense logged yet", Toast.LENGTH_LONG).show();
			return;
		}
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.instwise_pie_chart_fragment);
		LinearLayout  LayoutToDisplayChart=(LinearLayout)dialog.findViewById(R.id.transactionsArea);
		Intent achartIntent = new PayInstWiseTransPieChart().execute(ExpenseManager.this,LayoutToDisplayChart, payInstWiseTransactions, month, year);
		
		LinearLayout  LayoutToDisplayChart1=(LinearLayout)dialog.findViewById(R.id.amountsArea);
		Intent achartIntent1 = new PayInstWiseAmountPieChart().execute(ExpenseManager.this,LayoutToDisplayChart1, payInstWiseTransactions, month, year);
		dialog.show();
		
		Button pieChartCloseButton = (Button)dialog.findViewById(R.id.buttonPieClose);
		pieChartCloseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			dialog.dismiss();
				
			}
		});
		
	}

	public static String urlEncode(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {

			throw new RuntimeException("URLEncoder.encode() failed for " + s);
		}
	}

	public void exportAsCSVCatWise() {
		ArrayList<CategoryWiseExpense> catExp = new ArrayList<CategoryWiseExpense>();
		catExp = myDB.getExpenseOfMYCatWise(month, year);
		StringBuilder csvFileContent = new StringBuilder();
		// setting header
		// csvFileContent.append("Day,");
		csvFileContent.append(this.getString(R.string.cat_csv_header_category)
				+ ",");
		csvFileContent.append(this.getString(R.string.cat_csv_header_expense)
				+ "\n");

		for (CategoryWiseExpense categoryWiseExpense : catExp) {
			csvFileContent.append(categoryWiseExpense.categoryName);
			csvFileContent.append(",");
			csvFileContent.append(categoryWiseExpense.expense);
			csvFileContent.append("\n");

		}
		csvFileContent.append("\n");
		String fileName = "exp_mgr_" + year + "_" + month + "_catwise.csv";
		storeFile(fileName, csvFileContent);

	}

	public void exportAsCSVDayWise() {
		ArrayList<MonthlyExpense> dayExp = new ArrayList<MonthlyExpense>();
		dayExp = myDB.getAllExpenseList(month, year);
		StringBuilder csvFileContent = new StringBuilder();
		// setting header
		// csvFileContent.append("Day,");
		csvFileContent
				.append(this.getString(R.string.day_csv_header_day) + ",");
		csvFileContent.append(this.getString(R.string.day_csv_header_category)
				+ ",");
		csvFileContent.append(this.getString(R.string.day_csv_header_expense)
				+ ",");
		csvFileContent.append(this.getString(R.string.day_csv_header_notes)
				+ "\n");

		for (MonthlyExpense monthlyExpense : dayExp) {
			csvFileContent.append(monthlyExpense.day);
			csvFileContent.append(",");
			csvFileContent.append(monthlyExpense.catName);
			csvFileContent.append(",");
			csvFileContent.append(monthlyExpense.expense);
			csvFileContent.append(",");
			csvFileContent.append(monthlyExpense.notes);
			csvFileContent.append("\n");

		}
		csvFileContent.append("\n");
		String fileName = "exp_mgr_" + year + "_" + month + "_daywise.csv";
		storeFile(fileName, csvFileContent);

	}

	private void storeFile(String fileName, StringBuilder csvFileContent) {
		Boolean isSDPresent = android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
		if (isSDPresent) {
			try {

				File myFile = new File(
						applicationContext.getExternalFilesDir(null)
								+ "/"
								+ PreferenceManager
										.getDefaultSharedPreferences(
												applicationContext)
										.getString(
												applicationContext
														.getString(R.string.settings_csv_export_folder),
												"expensemgr"));
				// getApplicationContext().getFilesDir().getPath(), fileName);
				myFile.mkdirs();
				File outPutFile = new File(myFile, fileName);

				FileOutputStream fOut = new FileOutputStream(outPutFile);
				OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

				myOutWriter.append(csvFileContent);
				myOutWriter.close();
				fOut.close();
				Toast.makeText(getApplicationContext(),
						outPutFile.getPath() + " saved", Toast.LENGTH_LONG)
						.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(applicationContext, "SDCard not found",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.expense_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			finish();
			return true;
		} else if (id == R.id.pref_settings) {
			Intent intent = new Intent(getApplicationContext(),
					ExpMgrPreferenceActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onButtonClick(double arg1, int arg2) {
		ArrayList<PerformanceReport> pReport = myDB.getPerformanceOfYear(year,
				month);
		/*
		 * fragmentManager = getFragmentManager(); fragmentTransaction =
		 * fragmentManager.beginTransaction();
		 * fragmentTransaction.remove(settingsFrag);
		 * 
		 * fragmentTransaction.commit();
		 */
		if (myDB.getBudgetOfMY(arg2, year) == -1.0) {
			myDB.insertBudget(arg2, year, 0, arg1);
			setStatusMessages();
		} else {
			Toast.makeText(getApplicationContext(),
					this.getString(R.string.budget_reste_message),
					Toast.LENGTH_LONG).show();
		}

	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;
		// private TextView expToday;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView exp_items_per_month;
		private HashMap<String, Double> expenseItemsPerMonthMap;
		// private HashMap<String, Double> expenseItemsPerMonthTable = new
		// HashMap<String, Double>();;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd-MMM-yyyy");

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId,
				int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
					+ "Year: " + year);
			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

			// Print Month
			printMonth(month, year);

			// Find Number of Events
			expenseItemsPerMonthMap = findExpenseItemsPerMonth(year, month);
		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 * 
		 * @param year
		 * @param month
		 * @return
		 */
		private HashMap<String, Double> findExpenseItemsPerMonth(int year,
				int month) {

			HashMap<String, Double> map = myDB.getAllExpense(month, year);
			return map;

		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);

			gridcell.setOnClickListener(this);
			// expToday = (TextView) row.findViewById(R.id.exp_today);

			// ACCOUNT FOR SPACING

			Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			if ((!expenseItemsPerMonthMap.isEmpty())
					&& (expenseItemsPerMonthMap != null)) {
				if (expenseItemsPerMonthMap.containsKey(theday)
						&& !(day_color[1].equals("GREY"))) {
					exp_items_per_month = (TextView) row
							.findViewById(R.id.exp_today);
					Double spending = (Double) expenseItemsPerMonthMap
							.get(theday);
					exp_items_per_month.setText(spending.toString());
				}
			}

			// Set the Day GridCell
			gridcell.setText(theday);
			gridcell.setTag(theday + "-" + themonth + "-" + theyear);
			Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
					+ theyear);

			if (day_color[1].equals("GREY")) {
				gridcell.setTextColor(getResources()
						.getColor(R.color.lightgray));
				gridcell.setClickable(false);
			}
			if (day_color[1].equals("WHITE")) {
				gridcell.setTextColor(getResources().getColor(
						R.color.cornflowerblue));
			}
			if (day_color[1].equals("BLUE")) {
				gridcell.setTextColor(getResources().getColor(
						R.color.midnightblue));

			}
			return row;
		}

		@Override
		public void onClick(View view) {
			String date_month_year = (String) view.getTag();
			selectedDayMonthYearButton.setText("Selected: " + date_month_year);
			String dmy_Array[] = date_month_year.split("-");
			Log.e("Selected date", date_month_year);

			if (myDB.getBudgetOfMY(month, year) == -1.0) {
				showBudgetDialog(dmy_Array[1], dmy_Array[2]);

			} else {
				String expenseDay = ((Button) view).getText().toString();
				double currExpense = expenseItemsPerMonthMap.get(expenseDay) == null ? 0.0
						: expenseItemsPerMonthMap.get(expenseDay);

				showActionChoiceDialog(expenseDay, currExpense);
				try {
					Date parsedDate = dateFormatter.parse(date_month_year);
					Log.d(tag, "Parsed Date: " + parsedDate.toString());

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		private void showActionChoiceDialog(String expenseDay,
				double currExpense) {
			final Dialog dialog = new Dialog(ExpenseManager.this);
			final String dayOfMonth1 = expenseDay;
			final double expense = currExpense;

			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.day_choice_dialog);

			Button buttonAdd = (Button) dialog.findViewById(R.id.buttonAddExp);

			Button buttonView = (Button) dialog
					.findViewById(R.id.buttonViewExp);

			buttonAdd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					showExpenseDialog(dayOfMonth1, expense);

				}
			});

			buttonView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					showExpenseDetails(dayOfMonth1, expense);

				}

			});

			dialog.show();

		}

		private void showExpenseDetails(String dayOfMonth1, double expense) {
			final Dialog dialog = new Dialog(ExpenseManager.this);
			final String dayOfMonth2 = dayOfMonth1;
			final double expense1 = expense;
			ArrayList<DayExpNotes> listItems = myDB.getExpenseItemForADay(
					Integer.parseInt(dayOfMonth1), month, year);

			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.day_expense_details);
			TextView titleMsgView = (TextView) dialog
					.findViewById(R.id.textViewTitle);
			titleMsgView.setText("Total Expense of the day: " + expense);
			ListView dayExpListView = (ListView) dialog
					.findViewById(R.id.listViewDayExp);
			LayoutInflater inflater = getLayoutInflater();
			ViewGroup header = (ViewGroup) inflater.inflate(
					R.layout.day_exp_details_header, dayExpListView, false);
			dayExpListView.addHeaderView(header, null, false);

			ViewGroup footer = (ViewGroup) inflater.inflate(
					R.layout.day_exp_details_footer, dayExpListView, false);
			dayExpListView.addFooterView(footer, null, false);

			Button footerAddExpButton = (Button) footer
					.findViewById(R.id.buttonAddExpF);

			footerAddExpButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					showExpenseDialog(dayOfMonth2, expense1);

				}
			});
			dayExpListView.setAdapter(new DailyExpenseDetailsAdapter(listItems,
					getLayoutInflater()));
			Button buttonClose = (Button) dialog.findViewById(R.id.buttonClose);

			buttonClose.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					// showExpenseDialog(dayOfMonth1, expense);

				}
			});
			dialog.show();

		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}

		protected void addExpenseItem(String day, double d, int catCode,
				String notes, String payMethod, boolean expExistForCat) {
			if (expExistForCat) {
				myDB.updateExpense(month, year, Integer.parseInt(day), d,
						catCode, notes, payMethod);

			} else {
				myDB.insertExpense(month, year, Integer.parseInt(day), d,
						catCode, notes, payMethod);
			}
			expenseItemsPerMonthMap = findExpenseItemsPerMonth(year, month);
			adapter.notifyDataSetChanged();
			setStatusMessages();
		}

		protected void showExpenseDialog(String dayOfMonth, double currExp) {

			final Dialog dialog = new Dialog(ExpenseManager.this);
			final String dayOfMonth1 = dayOfMonth;
			final double expense = currExp;

			final HashMap<String, DayExpNotes> expBreakupOfDay = myDB
					.getExpenseofTheDay(Integer.parseInt(dayOfMonth), month,
							year);
			selectedCategory = "";
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.expense_fragment);
			TextView textView1 = (TextView) dialog
					.findViewById(R.id.currExpense);
			textView1.setText(getApplicationContext().getString(
					R.string.curr_expense_msg)
					+ ":" + currExp);
			final LinearLayout editAreLayout = (LinearLayout)dialog.findViewById(R.id.editArea);
			final TextView textView2 = (TextView) dialog
					.findViewById(R.id.catExpOfDay);
			final EditText editText2 = (EditText) dialog
					.findViewById(R.id.editTextExpNotes);

			final EditText editText = (EditText) dialog
					.findViewById(R.id.editTextExpense);
			final Spinner paymentMethod = (Spinner)dialog.findViewById(R.id.payInstType);
			EditTextLocker decimalEditTextLocker = new EditTextLocker(editText);
			decimalEditTextLocker.limitFractionDigitsinDecimal(2);
			editText.setFocusableInTouchMode(false);
			editText.setFocusable(false);
			editText2.setFocusableInTouchMode(false);
			editText2.setFocusable(false);
			ListView expCatListView = (ListView) dialog
					.findViewById(R.id.listViewExpCat);
			LayoutInflater inflater = getLayoutInflater();
			ViewGroup header = (ViewGroup) inflater.inflate(
					R.layout.exp_cat_header, expCatListView, false);
			expCatListView.addHeaderView(header, null, false);
			String[] categoryNameArray = new String[] {};
			int ctr = 0;
			ArrayList<CategoryData> tempCatList = myDB.getAllCategoriesName();

			expCatListView.setAdapter(new ExpenseCategoryAdapter(
					getLayoutInflater(), tempCatList));
			final Button buttonAdd = (Button) dialog
					.findViewById(R.id.addExpense);
			final Button buttonClose = (Button) dialog
					.findViewById(R.id.buttonClose);
			// final Button buttonNew = (Button)
			// dialog.findViewById(R.id.newExpense);
			final Button buttonSub = (Button) dialog
					.findViewById(R.id.minusExpense);

			expCatListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					CategoryData aData = (CategoryData) parent
							.getItemAtPosition(position);
					selectedCategory = aData.name;
					// categoryList[position];
					/*
					 * Toast.makeText(getApplicationContext(), "Category is: " +
					 * selectedCategory, Toast.LENGTH_LONG).show();
					 */
					Double catExpense = expBreakupOfDay.get(selectedCategory) == null ? 0.0
							: expBreakupOfDay.get(selectedCategory).expense;
					textView2.setText(selectedCategory
							+ " "
							+ getApplicationContext().getString(
									R.string.expense_literal) + ": "
							+ catExpense);
					editAreLayout.setVisibility(View.VISIBLE);
					textView2.setVisibility(View.VISIBLE);
					//editText.setVisibility(View.VISIBLE);
					//paymentMethod.setVisibility(View.VISIBLE);
					editText.setFocusableInTouchMode(true);
					editText.setFocusable(true);
					editText.requestFocus();
					editText.setText("" + catExpense);
					//editText2.setVisibility(View.VISIBLE);
					editText2.setFocusableInTouchMode(true);
					editText2.setFocusable(true);
					editText2
							.setText(expBreakupOfDay.get(selectedCategory) == null ? ""
									: expBreakupOfDay.get(selectedCategory).notes);
					
					int payInstChoice = CommonUtilities.getPayInstId(expBreakupOfDay.get(selectedCategory) == null ? "":expBreakupOfDay.get(selectedCategory).payMethod );

					paymentMethod.setSelection(payInstChoice, true);
					if (catExpense != 0.0) {
						// buttonAdd.setVisibility(View.VISIBLE);
						buttonSub.setVisibility(View.VISIBLE);
					} else {
						buttonSub.setVisibility(View.GONE);
					}

				}
			});

			buttonAdd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					// Double total = Double.parseDouble((editText.getText()
					// .toString())) + expense;
					String notes = "";
					if (selectedCategory.isEmpty()) {
						Toast.makeText(
								getApplicationContext(),
								getApplicationContext().getString(
										R.string.cat_not_selected),
								Toast.LENGTH_LONG).show();
						return;
					}
					if (expBreakupOfDay.containsKey(selectedCategory)) {
						Double catTotal = Double.parseDouble((editText
								.getText().toString()))
								+ expBreakupOfDay.get(selectedCategory).expense;
						// notes = expBreakupOfDay.get(selectedCategory).notes;
						addExpenseItem(dayOfMonth1, catTotal, categoryMap
								.get(selectedCategory), editText2.getText()
								.toString(), (String)paymentMethod.getSelectedItem(), true);
					} else {
						// notes = expBreakupOfDay.get(selectedCategory).notes;
						addExpenseItem(dayOfMonth1, Double
								.parseDouble((editText.getText().toString())),
								categoryMap.get(selectedCategory), editText2
										.getText().toString(), (String)paymentMethod.getSelectedItem(), false);
					}
					//Toast.makeText(applicationContext, "payment thru: " + (String)paymentMethod.getSelectedItem(), Toast.LENGTH_LONG).show();
					dialog.dismiss();
				}
			});

			buttonSub.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// _textDialog.setText(editText.getText().toString());
					// Double total = Double.parseDouble((editText.getText()
					// .toString())) + expense;
					String notes = "";
					if (selectedCategory.isEmpty()) {
						Toast.makeText(
								getApplicationContext(),
								getApplicationContext().getString(
										R.string.cat_not_selected),
								Toast.LENGTH_LONG).show();
						return;
					}
					if (expBreakupOfDay.containsKey(selectedCategory)) {
						if(! expBreakupOfDay.get(selectedCategory).payMethod.equalsIgnoreCase((String)paymentMethod.getSelectedItem())){
							Toast.makeText(applicationContext, "Pay Instrument type can't be changed", Toast.LENGTH_LONG).show();
							return;
						}
						Double catTotal = expBreakupOfDay.get(selectedCategory).expense
								- Double.parseDouble((editText.getText()
										.toString()));
						// notes = expBreakupOfDay.get(selectedCategory).notes;
						addExpenseItem(dayOfMonth1, catTotal, categoryMap
								.get(selectedCategory), editText2.getText()
								.toString(), (String)paymentMethod.getSelectedItem(), true);
					} /*else {
						// notes = expBreakupOfDay.get(selectedCategory).notes;
						addExpenseItem(dayOfMonth1, Double
								.parseDouble((editText.getText().toString())),
								categoryMap.get(selectedCategory), editText2
										.getText().toString(), (String)paymentMethod.getSelectedItem(), false);
					}*/
					dialog.dismiss();
				}
			});

			buttonClose.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});
			
			

			/*
			 * buttonNew.setOnClickListener(new View.OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { // TODO Auto-generated
			 * method stub //
			 * _textDialog.setText(editText.getText().toString()); Double total
			 * = Double.parseDouble((editText.getText() .toString())) + expense;
			 * if (selectedCategory.isEmpty()) {
			 * Toast.makeText(getApplicationContext(), "Category is not chosen",
			 * Toast.LENGTH_LONG) .show(); return; }
			 * if(expBreakupOfDay.containsKey(selectedCategory)){ Double
			 * catTotal = Double.parseDouble((editText.getText() .toString())) +
			 * expBreakupOfDay.get(selectedCategory);
			 * addExpenseItem(dayOfMonth1, catTotal,
			 * categoryMap.get(selectedCategory), true); } else{
			 * addExpenseItem(dayOfMonth1,
			 * Double.parseDouble((editText.getText() .toString())),
			 * categoryMap.get(selectedCategory), false); } dialog.dismiss(); }
			 * });
			 */
			dialog.show();
		}

		protected void showBudgetDialog(String monthName, String year) {
			// TODO Auto-generated method stub
			final Dialog dialog = new Dialog(ExpenseManager.this);
			final String monthN = monthName;
			final String yearN = year;
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.utility_fragment);
			TextView textView1 = (TextView) dialog.findViewById(R.id.monthName);
			textView1.setText(getApplicationContext().getString(
					R.string.budget_set_message)
					+ ": " + monthName);
			final EditText editText = (EditText) dialog
					.findViewById(R.id.editTextBudget);
			EditTextLocker decimalEditTextLocker = new EditTextLocker(editText);
			decimalEditTextLocker.limitFractionDigitsinDecimal(2);
			Button button = (Button) dialog.findViewById(R.id.setBudget);
			// if (currExp == 0.0) {

			button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					myDB.insertBudget(CommonUtilities.getMonthNum(monthN),
							Integer.parseInt(yearN), 0,
							Double.parseDouble(editText.getText().toString()));
					dialog.dismiss();
					setStatusMessages();
				}
			});

			dialog.show();
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 * 
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
					+ daysInMonth + " days.");

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
						+ prevMonth + " NextMonth: " + nextMonth
						+ " NextYear: " + nextYear);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			Log.d(tag, "Week Day:" + currentWeekDay + " is "
					+ getWeekDayAsString(currentWeekDay));
			Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

			if (cal.isLeapYear(cal.get(Calendar.YEAR)))
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag,
						"PREV MONTH:= "
								+ prevMonth
								+ " => "
								+ getMonthAsString(prevMonth)
								+ " "
								+ String.valueOf((daysInPrevMonth
										- trailingSpaces + DAY_OFFSET)
										+ i));
				list.add(String
						.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
								+ i)
						+ "-GREY"
						+ "-"
						+ getMonthAsString(prevMonth)
						+ "-"
						+ prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " "
						+ getMonthAsString(currentMonth) + " " + yy);
				if (i == getCurrentDayOfMonth()) {
					list.add(String.valueOf(i) + "-BLUE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-WHITE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-"
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

	}

}
