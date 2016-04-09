package com.example.expensemanager;

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
import android.widget.TextView;

@SuppressLint("WorldReadableFiles")
public class ExpenseFragment extends Fragment {
	
	//private String file = "mydata.properties";
	EditText expToday;
	TextView currExp;
	private String currentExp;
	 
	//Button submit;
	
	public ExpenseFragment(String currentExpense){
		currentExp = currentExpense;
	}
	 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  //ListAdapter myListAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, new String [] {"a", "b"});
		  //myListAdapter = new ProductListAdaptor(getActivity(), DatabaseActivity.productList);
		  
		  //getListView().setOnItemClickListener(this);
		  
		  
		 }
	 ExpenseListener activityCallback;
	 public interface ExpenseListener {
	        public void onExpenseSumbit(String arg1);
	  }
	  
	  @Override
	  public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        try {
	            activityCallback = (ExpenseListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement ExpenseListener");
	        }
	    }
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
		    super.onViewCreated(view, savedInstanceState);
		    expToday = (EditText)view.findViewById(R.id.editTextExpense);
		    
		    currExp = (TextView)view.findViewById(R.id.currExpense);
		    currExp.setText(currentExp);
		    		   /* final Button button = 
	                (Button) view.findViewById(R.id.expense);
		        button.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		                buttonClicked(v);
		            }
		        });
		        */

		    
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
		  return inflater.inflate(R.layout.expense_fragment, container, false);
		  
		 }
		 
		 public void onStart(){
			 super.onStart();
			// myListAdapter.notifyDataSetChanged();
		 }
		 
		 public void buttonClicked(View view){
			 
			 activityCallback.onExpenseSumbit(expToday.getText().toString());
			 
		 }
		 @Override
		 public void onDestroyView(){
			 super.onDestroyView();
			 Log.v("Expense Fragment", "Destroying View");
		 }
	
}
