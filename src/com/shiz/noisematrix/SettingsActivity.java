package com.shiz.noisematrix;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity 
							  implements OnSharedPreferenceChangeListener {
	
	public static final String CROSS_EV = "cross_ev";
	public static final String CROSS_SD = "cross_sd";
	public static final String BACKGROUND_EV = "background_ev";
	public static final String BACKGROUND_SD = "background_sd";
	public static final String FIELDS_EV = "fields_ev";
	public static final String FIELDS_SD = "fields_sd";
	public static final String CIRCLES_COUNT = "circles_count";
	
	private final static String LOG_TAG = "settings";
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		
		Preference connectionPref = findPreference(CROSS_EV); 
		String keyValue = pref.getString(CROSS_EV, "");
		connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
		
		connectionPref = findPreference(CROSS_SD); 
		keyValue = pref.getString(CROSS_SD, "");
		connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
		
		connectionPref = findPreference(BACKGROUND_EV); 
		keyValue = pref.getString(BACKGROUND_EV, "");
		connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
		
		connectionPref = findPreference(BACKGROUND_SD); 
		keyValue = pref.getString(BACKGROUND_SD, "");
		connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
		
		connectionPref = findPreference(FIELDS_EV); 
		keyValue = pref.getString(FIELDS_EV, "");
		connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
		
		connectionPref = findPreference(FIELDS_SD); 
		keyValue = pref.getString(FIELDS_SD, "");
		connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
		
		connectionPref = findPreference(CIRCLES_COUNT);
		keyValue = pref.getString(CIRCLES_COUNT, "");
		connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
	}
	
	public static int checkValue(String value) {
		
		int correctValue = 0;
		try {
			correctValue = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			Log.d(LOG_TAG, "e 0");
			return 0;
		}
		
		if (correctValue > 255) {
			Log.d(LOG_TAG, "> 255");
			return 255;
		}
		if (correctValue < 0) {
			Log.d(LOG_TAG, "< 0");
			return 0;
		}
		Log.d(LOG_TAG, "correct = " + correctValue);
		return correctValue; 
	}

	@SuppressWarnings("deprecation")
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		String keyValue;
		if (key.equals(CROSS_EV)) {
			Preference connectionPref = findPreference(key);
			keyValue = sharedPreferences.getString(key, "");
			connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
		}
		if (key.equals(CROSS_SD)) {
			Preference connectionPref = findPreference(key);
			keyValue = sharedPreferences.getString(key, "");
			connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
		}
		if (key.equals(BACKGROUND_EV)) {
			Preference connectionPref = findPreference(key);
			keyValue = sharedPreferences.getString(key, "");
			connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
		}
		if (key.equals(BACKGROUND_SD)) {
			Preference connectionPref = findPreference(key);
			keyValue = sharedPreferences.getString(key, "");
			connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
		}
		if (key.equals(FIELDS_EV)) {
			Preference connectionPref = findPreference(key);
			keyValue = sharedPreferences.getString(key, "");
			connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
		}
		if (key.equals(FIELDS_SD)) {
			Preference connectionPref = findPreference(key);
			keyValue = sharedPreferences.getString(key, "");
			connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
		}
		if (key.equals(CIRCLES_COUNT)) {
			Preference connectionPref = findPreference(key);
			keyValue = sharedPreferences.getString(key, "");
			connectionPref.setSummary(String.valueOf(checkValue(keyValue)));
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
}
