package com.example.expensemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CurrencyPreference extends DialogPreference{
EditText curr_Edit;
Button cancelbutton;
Button okbutton;
RadioGroup curr_Group;
RadioButton checkedCurr;
	public CurrencyPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.currency_dialog);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
            builder.setTitle(R.string.currency);
            builder.setInverseBackgroundForced(true);
            builder.setPositiveButton(null, null);
            builder.setNegativeButton(null, null);
            super.onPrepareDialogBuilder(builder);  
    }
	 @Override
     public void onBindDialogView(View view){
             curr_Edit = (EditText)view.findViewById(R.id.editText1);
             curr_Group = (RadioGroup)view.findViewById(R.id.currencyGroup);
             
             curr_Group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					RadioButton checkedCurr = (RadioButton)curr_Group.findViewById(checkedId);
					if(checkedCurr.getText().equals("Other")){
						curr_Edit.setVisibility(View.VISIBLE);
					}
					else{
						curr_Edit.setText("");
						curr_Edit.setVisibility(View.GONE);
					}
				}
			});
             cancelbutton = (Button)view.findViewById(R.id.button10);
             cancelbutton.setOnClickListener(new View.OnClickListener() {
            	 
        		 
                 @Override  
            	 public void onClick(View v) {
                            getDialog().dismiss();
                    }
             });
             okbutton = (Button)view.findViewById(R.id.button9);
             okbutton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                    	 int checkedId = curr_Group.getCheckedRadioButtonId();
                    	 if (!curr_Edit.getText().toString().isEmpty()){
                    		 persistString(curr_Edit.getText().toString());
                     }else{
                    		 checkedCurr = (RadioButton)curr_Group.findViewById(checkedId);
                    		 persistString(checkedCurr.getText().toString());
                     }
                    	 getDialog().dismiss();//do something on "OK" click
                             //you can get edittext value 
                             //and add it to persistence 
                             //you can decide whether to close the dialog 
                             //or still keep it
                     }
             });
             super.onBindDialogView(view);
     }
	 @Override
	 protected void onDialogClosed(boolean positiveResult) {
	     super.onDialogClosed(positiveResult);

	     if (positiveResult) {
	        /* Editor editor = getEditor();
	         editor.putString(myKey1, myView.getValue1());
	         editor.putString(myKey2, myView.getValue2());
	         editor.commit();*/
	    	 persistString(checkedCurr.getText().toString());
	     }
	 }

	
}
