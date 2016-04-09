package com.example.expensemanager;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.widget.Toast;

public class ExpMgrPreferenceActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		this.addPreferencesFromResource(R.xml.preferences);
		this.initSummaries(this.getPreferenceScreen());

		this.getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	/**
	 * Set the summaries of all preferences
	 */
	private void initSummaries(PreferenceGroup pg) {
		for (int i = 0; i < pg.getPreferenceCount(); ++i) {
			Preference p = pg.getPreference(i);
			if (p instanceof PreferenceGroup)
				this.initSummaries((PreferenceGroup) p); // recursion
			else
				this.setSummary(p);
		}
	}

	/**
	 * Set the summaries of the given preference
	 */
	private void setSummary(Preference pref) {
		// react on type or key
		if (pref instanceof ListPreference) {
			ListPreference listPref = (ListPreference) pref;
			pref.setSummary(listPref.getEntry());
		} else if (pref instanceof EditTextPreference) {
			EditTextPreference editPref = (EditTextPreference) pref;
			
				pref.setSummary(editPref.getText().trim());
			
		} else if (pref instanceof CurrencyPreference) {
			DialogPreference cur = (DialogPreference) pref;

			pref.setSummary(cur.getSharedPreferences().getString(cur.getKey(),
					"A"));
		}
	}

	/**
	 * used to change the summary of a preference
	 */

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Preference pref = findPreference(key);
		 
		this.setSummary(pref);

	}

}
