package com.example.expensemanager.data;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.expensemanager.R;

public class ExpenseCategoryAdapter extends BaseAdapter {
	//String[] category;
	//int[] images;
	ArrayList<CategoryData> category;
	// int _intRadio [];
	private boolean userSelected = false;
	//private RadioButton mCurrentlyCheckedRB;
	private LayoutInflater mInflater;

	public ExpenseCategoryAdapter(LayoutInflater inflator, ArrayList<CategoryData> catStrings) {
		this.category = catStrings;
		//this.images = catImages;
		
		this.mInflater = inflator;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return category.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return category.get(position);
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
			convertView = mInflater.inflate(R.layout.expense_type_list, null);
			item = new ViewItem();

			/*item.imageItem = (Button) convertView
					.findViewById(R.id.imageViewET);
*/
			item.categoryDesc = (TextView) convertView
					.findViewById(R.id.textViewET);
			/*item.editAmount = (EditText) convertView.findViewById(R.id.editTextAmount_ET);

			EditTextLocker decimalEditTextLocker = new EditTextLocker(item.editAmount);
			decimalEditTextLocker.limitFractionDigitsinDecimal(2);
			item.radioItem = (RadioButton) convertView
					.findViewById(R.id.radioButtonET);*/

			convertView.setTag(item);

		}

		else {
			item = (ViewItem) convertView.getTag();
		}
		/*if (position == getCount() - 1 && userSelected == false) {
			// item.radioItem.setChecked(true);
			mCurrentlyCheckedRB = item.radioItem;
		} else {
			item.radioItem.setChecked(false);
		}

		item.radioItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mCurrentlyCheckedRB != null) {
					if (mCurrentlyCheckedRB == null)
						mCurrentlyCheckedRB = (RadioButton) v;
					mCurrentlyCheckedRB.setChecked(true);
				}

				if (mCurrentlyCheckedRB == v)
					return;

				mCurrentlyCheckedRB.setChecked(false);
				((RadioButton) v).setChecked(true);
				mCurrentlyCheckedRB = (RadioButton) v;
			}
		});
*/		//item.imageItem.setImageResource(R.drawable.catmaster);

	//item.imageItem.setBackgroundColor(category.get(position).colorCode);


		item.categoryDesc.setText(category.get(position).name);
		item.categoryDesc.setTextColor(category.get(position).colorCode);
		//item.radioItem.setSelected(false);

		return convertView;
	}

	private class ViewItem {
		//ImageView imageItem;
		//Button imageItem;
		TextView categoryDesc;
		//EditText editAmount;
		//RadioButton radioItem;

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
