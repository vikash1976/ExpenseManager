package com.example.expensemanager.data;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class EditTextLocker {

    private EditText editText;

    private int charactersLimit;

    private int fractionLimit;

    public EditTextLocker(EditText editText) {

        this.editText = editText;

        editText.setOnKeyListener(editTextOnKeyListener);
    }

    private OnKeyListener editTextOnKeyListener = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_DEL) {
                startStopEditing(false);
            }

            return false;
        }
    };

    private TextWatcher editTextWatcherForCharacterLimits = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!editText.getText().toString().equalsIgnoreCase("")) {

                int editTextLength = editText.getText().toString().trim().length();

                if (editTextLength >= charactersLimit) {

                    startStopEditing(true);

                }

            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher editTextWatcherForFractionLimit = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!editText.getText().toString().equalsIgnoreCase("")) {

                String editTextString = editText.getText().toString().trim();
                int decimalIndexOf = editTextString.indexOf(".");

                if (decimalIndexOf >= 0) {

                    if (editTextString.substring(decimalIndexOf).length() > fractionLimit) {

                        startStopEditing(true);

                    }
                }

            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void limitCharacters(final int limit) {

        this.charactersLimit = limit;
        editText.addTextChangedListener(editTextWatcherForCharacterLimits);
    }

    public void limitFractionDigitsinDecimal(int fractionLimit) {

        this.fractionLimit = fractionLimit;
        editText.addTextChangedListener(editTextWatcherForFractionLimit);
    }

    public void unlockEditText() {

        startStopEditing(false);
    }

    public void startStopEditing(boolean isLock) {

        if (isLock) {

            editText.setFilters(new InputFilter[] { new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
                }
            } });

        } else {

            editText.setFilters(new InputFilter[] { new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    return null;
                }
            } });
        }
    }
}