package com.example.expensemanager;

import com.example.expensemanager.data.CommonUtilities;
import com.example.expensemanager.data.EditTextLocker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

@SuppressLint("WorldReadableFiles")
public class UtilityFragment extends Fragment {
	
	//private String file = "mydata.properties";
	EditText budgetAmt;
	//Spinner spinner; 
	Button setBudget;
	String monthName;
	int monthNum;
	public UtilityFragment(){
		
	}
	 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  //ListAdapter myListAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, new String [] {"a", "b"});
		  //myListAdapter = new ProductListAdaptor(getActivity(), DatabaseActivity.productList);
		  
		  //getListView().setOnItemClickListener(this);
		  
		  
		 }
	 UtilityListener activityCallback;
	 public interface UtilityListener {
	        public void onButtonClick(double bgtAmount, int monthNum);
	  }
	  
	  @Override
	  public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        try {
	            activityCallback = (UtilityListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement UtilityListener");
	        }
	    }
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
		    super.onViewCreated(view, savedInstanceState);
		    budgetAmt = (EditText)view.findViewById(R.id.editTextBudget);
		    EditTextLocker decimalEditTextLocker = new EditTextLocker(budgetAmt);
	        decimalEditTextLocker.limitFractionDigitsinDecimal(2);
		    /*spinner = (Spinner) view.findViewById(R.id.budgetMonth);
		    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			
		    	public void onItemSelected(
                        AdapterView<?> adapterView, View view,
                        int i, long l) {
		    		
                    monthName = spinner.getItemAtPosition(i).toString();
                    monthNum =   CommonUtilities.getMonthNum(monthName);
                    		
		    	}

                public void onNothingSelected(
                        AdapterView<?> adapterView) {

                }
            }
			);
*/		    final Button button = 
	                 (Button) view.findViewById(R.id.setBudget);
		        button.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		                buttonClicked(v);
		            }
		        });
		        

		    
		    }
		
		@Override
		 public void onActivityCreated(Bundle savedInstanceState) {

	        super.onActivityCreated(savedInstanceState);
	        //setListAdapter(DatabaseActivity.myListAdapter);
	        //getListView().setOnItemClickListener(this);
	        
		}
		

		 @Override
		 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		   Bundle savedInstanceState) {
		  return inflater.inflate(R.layout.utility_fragment, container, false);
		 }
		 
		 public void onStart(){
			 super.onStart();
			// myListAdapter.notifyDataSetChanged();
		 }
		 
		 public void buttonClicked(View view){
			 
			 activityCallback.onButtonClick(Double.parseDouble(budgetAmt.getText().toString()), monthNum);
			 
		 }
		 @Override
		 public void onDestroyView(){
			 super.onDestroyView();
			 Log.v("Utility Fragment", "Destroying View");
		 }
	
}
