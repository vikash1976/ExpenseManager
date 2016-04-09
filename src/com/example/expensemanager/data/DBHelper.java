package com.example.expensemanager.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "ExpenseManager.db";
	public static final String EXPENSE_TABLE_NAME = "expense";
	public static final String BUDGET_TABLE_NAME = "budget";
	public static final String CATEGORY_TABLE_NAME = "category";
	public static final String ORDERITEM_TABLE_NAME = "orderitem";
	public static final String EXPENSE_COLUMN_ID = "id";
	public static final String EXPENSE_COLUMN_MONTH = "month";
	public static final String EXPENSE_COLUMN_YEAR = "year";
	public static final String EXPENSE_COLUMN_DAY = "day";
	public static final String EXPENSE_COLUMN_AMOUNT = "amount";
	public static final String EXPENSE_COLUMN_CATCODE = "catCode";
	public static final String EXPENSE_COLUMN_NOTES = "notes";
	public static final String EXPENSE_COLUMN_PAYMETHOD = "payMethod";
	
	public static final String BUDGET_COLUMN_ID = "id";
	public static final String BUDGET_COLUMN_MONTH = "month";
	public static final String BUDGET_COLUMN_YEAR = "year";
	public static final String BUDGET_COLUMN_DAY = "day";
	public static final String BUDGET_COLUMN_AMOUNT = "amount";
	public static final String CATEGORY_COLUMN_ID = "id";
	public static final String CATEGORY_COLUMN_NAME = "name";
	public static final String CATEGORY_COLUMN_COLORCODE = "colorCode";
	/*final String [] categoryList = new String[] { "Food/Drinks", "Healthcare",
			"Transportation", "Donations/Gifts", "Grocery", "Entertainment",
			"Education", "Personal/Debt Payments/Misc",
			"Savings and Insurance", "Clothing/Personal Care",
			"Utilities/Bills", "Housing(EMI/Rent)" };*/

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table "
				+ CATEGORY_TABLE_NAME
				+ "(id integer primary key autoincrement, name text, colorCode int)");
		db.execSQL("create table "
				+ EXPENSE_TABLE_NAME
				+ "(id integer primary key autoincrement, month int ,year int,day int, amount real, notes text, catCode int REFERENCES category(id), payMethod text)");
		db.execSQL("create table "
				+ BUDGET_TABLE_NAME
				+ "(id integer primary key autoincrement, month int ,year int,day int, amount real)");
		
		//createCategories(categoryList);
	}

	public boolean createCategories(String [] catArray, int colorArray[]) {
		SQLiteDatabase db = this.getWritableDatabase();
		//ContentValues contentValues = new ContentValues();

		//contentValues.put(EXPENSE_COLUMN_ID, id);
		
		
	          String sql = "INSERT INTO "+ CATEGORY_TABLE_NAME +" VALUES (?,?, ?);";
	          SQLiteStatement statement = db.compileStatement(sql);
	          db.beginTransaction();
	          for (int i = 0; i<catArray.length; i++) {
	                    statement.clearBindings();
	                    statement.bindLong(1, i);
	                    statement.bindString(2, catArray[i]);
	                    statement.bindLong(3, colorArray[i]);
	                    
	                    statement.execute();
	           }
	          db.setTransactionSuccessful();	
	          db.endTransaction();
	          db.close();


		return true;
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		
		db.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + BUDGET_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
		onCreate(db);
	}

	public boolean insertExpense(int month, int year,
			int day, double amount, int catCode, String notes, String payMethod) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		//contentValues.put(EXPENSE_COLUMN_ID, id);
		contentValues.put(EXPENSE_COLUMN_MONTH, month);
		contentValues.put(EXPENSE_COLUMN_YEAR, year);
		contentValues.put(EXPENSE_COLUMN_DAY, day);
		contentValues.put(EXPENSE_COLUMN_AMOUNT, amount);
		contentValues.put(EXPENSE_COLUMN_CATCODE, catCode);
		contentValues.put(EXPENSE_COLUMN_NOTES, notes);
		contentValues.put(EXPENSE_COLUMN_PAYMETHOD, payMethod);
		db.insert(EXPENSE_TABLE_NAME, null, contentValues);
		db.close();

		return true;
	}

	public boolean insertBudget(int month, int year,
			int day, double amount) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		//contentValues.put(BUDGET_COLUMN_ID, id);
		contentValues.put(BUDGET_COLUMN_MONTH, month);
		contentValues.put(BUDGET_COLUMN_YEAR, year);
		contentValues.put(BUDGET_COLUMN_DAY, day);
		contentValues.put(BUDGET_COLUMN_AMOUNT, amount);

		db.insert(BUDGET_TABLE_NAME, null, contentValues);
		db.close();

		return true;
	}

	public int getLastID(String tableName) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM " + tableName
				+ " ORDER BY id DESC LIMIT 1", null);
		int lastOrderId = 0;
		if (res != null && res.moveToFirst()) {

			lastOrderId = Integer.parseInt(res.getString(res
					.getColumnIndex("id")));
			res.close();

		}

		return lastOrderId;
	}

	/*public boolean insertOrderItem(int orderid, String productName, int quantity) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put("orderid", orderid);
		contentValues.put("itemname", productName);
		contentValues.put("quantity", quantity);

		db.insert(ORDERITEM_TABLE_NAME, null, contentValues);
		db.close();
		return true;
	}*/

	/*public Cursor getData(String title) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from " + EXPENSE_TABLE_NAME
				+ " where title= '" + title + "'", null);
		return res;
	}*/

	public int numberOfExpenseRows() {
		SQLiteDatabase db = this.getReadableDatabase();
		int numRows = (int) DatabaseUtils.queryNumEntries(db,
				EXPENSE_TABLE_NAME);
		return numRows;
	}
	public int numberOfBudgetRows() {
		SQLiteDatabase db = this.getReadableDatabase();
		int numRows = (int) DatabaseUtils.queryNumEntries(db,
				BUDGET_TABLE_NAME);
		return numRows;
	}

	public boolean updateExpense(int month, int year, int day,
			double amount, int catCode, String notes, String payMethod) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(EXPENSE_COLUMN_MONTH, month);
		contentValues.put(EXPENSE_COLUMN_YEAR, year);
		contentValues.put(EXPENSE_COLUMN_DAY, day);
		contentValues.put(EXPENSE_COLUMN_AMOUNT, amount);
		contentValues.put(EXPENSE_COLUMN_CATCODE, catCode);
		contentValues.put(EXPENSE_COLUMN_NOTES, notes);
		contentValues.put(EXPENSE_COLUMN_PAYMETHOD, payMethod);
		db.update(EXPENSE_TABLE_NAME, contentValues,
				"month = " + month + " AND year= "+year+" AND day= "+day + " AND catCode= "+ catCode , null);
		db.close();
		return true;
	}

	/*public Integer deleteProduct(String title) {
		SQLiteDatabase db = this.getWritableDatabase();
		int affectedNumRows = db.delete(EXPENSE_TABLE_NAME, "title = '" + title + "'", null);
		db.close();
		return affectedNumRows;
	}
*/
	public HashMap <String, Double> getAllExpense(int month, int year) {
		HashMap <String, Double> expense_list = new HashMap<String, Double>();
		// hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select day, sum(amount) as total from " + EXPENSE_TABLE_NAME + " where month =" + month +" AND year =" +year + " group by day", null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			expense_list.put(res.getString(res
					.getColumnIndex(EXPENSE_COLUMN_DAY)), Double.parseDouble(res.getString(res
					.getColumnIndex("total"))));
			
			res.moveToNext();
		}
		return expense_list;
	}
	
	public HashMap <String, DayExpNotes> getExpenseofTheDay(int day, int month, int year) {
		HashMap <String, DayExpNotes> day_cat_map = new HashMap<String, DayExpNotes>();
		// hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select amount, notes, payMethod, c.name as catName from " + EXPENSE_TABLE_NAME + " as e, category as c where c.id = e.catCode AND month =" + month +" AND year =" +year + " AND day =" + day, null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			DayExpNotes exp_notes = new DayExpNotes(Double.parseDouble(res.getString(res
					.getColumnIndex(EXPENSE_COLUMN_AMOUNT))), res.getString(res
							.getColumnIndex("notes")), res.getString(res
									.getColumnIndex("payMethod")));
			day_cat_map.put(res.getString(res
					.getColumnIndex("catName")), exp_notes);
			
			res.moveToNext();
		}
		return day_cat_map;
	}
	
	
	public ArrayList<DayExpNotes> getExpenseItemForADay(int day, int month, int year) {
		ArrayList<DayExpNotes> day_exp_list = new ArrayList<DayExpNotes>();
		// hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select amount, notes, payMethod from " + EXPENSE_TABLE_NAME + " as e, category as c where c.id = e.catCode AND month =" + month +" AND year =" +year + " AND day =" + day, null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			DayExpNotes exp_notes = new DayExpNotes(Double.parseDouble(res.getString(res
					.getColumnIndex(EXPENSE_COLUMN_AMOUNT))), res.getString(res
							.getColumnIndex("notes")),res.getString(res
									.getColumnIndex("payMethod")));
			
			day_exp_list.add(exp_notes);
			res.moveToNext();
		}
		return day_exp_list;
	}
	
	public ArrayList<CatDayExpNotes> getCatWiseExpenseItems(String catName, int month, int year) {
		ArrayList<CatDayExpNotes> day_exp_list = new ArrayList<CatDayExpNotes>();
		// hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select day, amount, notes from " + EXPENSE_TABLE_NAME + "  where month =" + month +" AND year =" +year + " AND catCode =" + "(select id from category where name = '"+catName +"')" + " order by day", null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			CatDayExpNotes exp_notes = new CatDayExpNotes(res.getInt(res.getColumnIndex("day")), Double.parseDouble(res.getString(res
					.getColumnIndex(EXPENSE_COLUMN_AMOUNT))), res.getString(res
							.getColumnIndex("notes")));
			
			day_exp_list.add(exp_notes);
			res.moveToNext();
		}
		return day_exp_list;
	}
	
	
	public ArrayList<CatDayExpNotes> getInstWiseExpenseItems(String instName, int month, int year) {
		ArrayList<CatDayExpNotes> day_exp_list = new ArrayList<CatDayExpNotes>();
		// hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select day, amount, notes from " + EXPENSE_TABLE_NAME + "  where month =" + month +" AND year =" +year + " AND payMethod ='" + instName + "' order by day", null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			CatDayExpNotes exp_notes = new CatDayExpNotes(res.getInt(res.getColumnIndex("day")), Double.parseDouble(res.getString(res
					.getColumnIndex(EXPENSE_COLUMN_AMOUNT))), res.getString(res
							.getColumnIndex("notes")));
			
			day_exp_list.add(exp_notes);
			res.moveToNext();
		}
		return day_exp_list;
	}
	
	public ArrayList<MonthlyExpense> getAllExpenseList(int month, int year) {
		ArrayList<MonthlyExpense> expense_list = new ArrayList<MonthlyExpense>();
		// hp = new HashMap();
		int d;
		String catName;
		String notes;
		double e;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select day, amount, c.name as CatName, notes from " + EXPENSE_TABLE_NAME + " as e, category as c where month =" + month +" AND year =" +year+" AND e.catCode = c.id order by day", null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
	
					d = res.getInt(res
					.getColumnIndex(EXPENSE_COLUMN_DAY));
					e = res.getDouble((res
					.getColumnIndex(EXPENSE_COLUMN_AMOUNT)));
					catName = res.getString(res
							.getColumnIndex("CatName"));
					notes = res.getString(res
							.getColumnIndex(EXPENSE_COLUMN_NOTES));
				MonthlyExpense mE = new MonthlyExpense(d, e, catName, notes);
				expense_list.add(mE);
			
			res.moveToNext();
		}
		return expense_list;
	}
	
	public Double getExpenseOfMY(int month, int year) {
		Double expenseOfMY = 0.0;
		// hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select sum(amount) as SumExpense from " + EXPENSE_TABLE_NAME + " where month =" + month +" AND year =" +year, null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			 expenseOfMY = res.getDouble(res
					.getColumnIndex("SumExpense"));
						
			res.moveToNext();
		}
		return expenseOfMY;
	}

	public ArrayList<CategoryWiseExpense> getExpenseOfMYCatWise(int month, int year) {
		Double expenseOfCat = 0.0;
		int category;
		String catName;
		ArrayList<CategoryWiseExpense> listCatWiseExp = new ArrayList<CategoryWiseExpense>();
		// hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select catCode, sum(amount), c.name as CatName from " + EXPENSE_TABLE_NAME + " as e, category as c where month =" + month +" AND year =" +year +" AND e.catCode = c.id group by catCode", null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			expenseOfCat = res.getDouble(res
					.getColumnIndex("sum(amount)"));
			category = res.getInt(res
					.getColumnIndex("catCode"));
			catName = res.getString(res
					.getColumnIndex("CatName"));
				CategoryWiseExpense entry = new CategoryWiseExpense(category, expenseOfCat, catName);		
				listCatWiseExp.add(entry);		
			res.moveToNext();
		}
		return listCatWiseExp;
	}

	public ArrayList<PayInstWiseTransactions> getPayInstWiseData(int month, int year) {
		Double sumExpense = 0.0;
		int numTrans;
		String payMethod;
		ArrayList<PayInstWiseTransactions> listCatWiseExp = new ArrayList<PayInstWiseTransactions>();
		// hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select payMethod, sum(amount) as sumAmount, count(id) as numTransactions from " + EXPENSE_TABLE_NAME + " where month =" + month +" AND year =" +year +" group by payMethod", null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			sumExpense = res.getDouble(res
					.getColumnIndex("sumAmount"));
			numTrans = res.getInt(res
					.getColumnIndex("numTransactions"));
			payMethod = res.getString(res
					.getColumnIndex("payMethod"));
			PayInstWiseTransactions entry = new PayInstWiseTransactions(payMethod, numTrans, sumExpense);		
				listCatWiseExp.add(entry);		
			res.moveToNext();
		}
		return listCatWiseExp;
	}
	
	public Double getBudgetOfMY(int month, int year) {
		Double budgetOfMY = -1.0;
		// hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select amount from " + BUDGET_TABLE_NAME + " where month =" + month +" AND year =" +year, null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			budgetOfMY = res.getDouble(res
					.getColumnIndex("amount"));
						
			res.moveToNext();
		}
		return budgetOfMY;
	}
	public ArrayList<PerformanceReport> getPerformanceOfYear(int year, int month) {
		int m, y;
		double e, b;
		String orderBy = month > 6 ? "DESC":"ASC";
		ArrayList<PerformanceReport> pRList = new ArrayList<PerformanceReport>();
		// hp = new HashMap();
		//select e.year, e.month, sum(e.amount), o.amount from expense as e, orders as o where o.year =e.year and o.month=e.month group by e.month;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select e.year, e.month, sum(e.amount) as sumExpense, o.amount from expense as e, budget as o where o.year =e.year and o.month=e.month and e.year="+year+ " group by e.month order by e.month "+ orderBy, null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			m = res.getInt(res
					.getColumnIndex("month"));
			y = res.getInt(res
					.getColumnIndex("year"));
			e = res.getDouble(res
					.getColumnIndex("sumExpense"));
			b = res.getDouble(res
					.getColumnIndex("amount"));
			double dev = 100 - (e/b)*100;
			PerformanceReport pR = new PerformanceReport(y, m, e, b, dev);
						
			pRList.add(pR);
			res.moveToNext();
		}
		
		return pRList;
	}
		public HashMap <String, Integer> getAllCategories(){
		HashMap <String, Integer> categoryMap = new HashMap<String, Integer>();
		// hp = new HashMap();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select id, name from " + CATEGORY_TABLE_NAME , null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			categoryMap.put(res.getString(res
					.getColumnIndex("name")), res.getInt(res
					.getColumnIndex("id")));
			
			res.moveToNext();
		}
		return categoryMap;

	}
		
		public ArrayList<CategoryData> getAllCategoriesName(){
			ArrayList<CategoryData> nameList = new ArrayList <CategoryData>();
			// hp = new HashMap();
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor res = db.rawQuery("select id, name, colorCode from " + CATEGORY_TABLE_NAME + " order by id", null);
			res.moveToFirst();
			while (res.isAfterLast() == false) {
				nameList.add(new CategoryData(res.getInt(res
						.getColumnIndex("id")), res.getString(res
								.getColumnIndex("name")), res.getInt(res
										.getColumnIndex("colorCode"))));
				
				res.moveToNext();
			}
			return nameList;

		}
		
		public int[] getAllCategoriesColors(){
			int [] colorList = new int[] {};
			// hp = new HashMap();
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor res = db.rawQuery("select colorCode from " + CATEGORY_TABLE_NAME + " order by id", null);
			res.moveToFirst();
			int i=0;
			while (res.isAfterLast() == false) {
				colorList[i] = res.getInt(res
						.getColumnIndex("colorCode"));
				
				res.moveToNext();
				i++;
			}
			return colorList;

		}
	
}