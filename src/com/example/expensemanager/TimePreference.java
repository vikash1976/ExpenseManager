package com.example.expensemanager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePreference extends DialogPreference {
	private Calendar calendar;
	private TimePicker picker = null;
	private Context context;

	public TimePreference(Context ctxt) {
		this(ctxt, null);
	}

	public TimePreference(Context ctxt, AttributeSet attrs) {
		this(ctxt, attrs, android.R.attr.dialogPreferenceStyle);
		
		
	}

	public TimePreference(Context ctxt, AttributeSet attrs, int defStyle) {
		super(ctxt, attrs, defStyle);
		this.context = ctxt;

		setPositiveButtonText(R.string.setTime);
		
		setNegativeButtonText(null);
		calendar = new GregorianCalendar();
	}

	@Override
	protected View onCreateDialogView() {
		
		picker = new TimePicker(getContext());
		
		
		return (picker);
	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);
		picker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		picker.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			calendar.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
			calendar.set(Calendar.MINUTE, picker.getCurrentMinute());

			setSummary(getSummary());
			if (callChangeListener(calendar.getTimeInMillis())) {
				persistLong(calendar.getTimeInMillis());
				notifyChanged();
			}
			scheduleNotification(getNotification("Log your expense for the Day"));
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return (a.getString(index));
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

		if (restoreValue) {
			if (defaultValue == null) {
				calendar.setTimeInMillis(getPersistedLong(System
						.currentTimeMillis()));
			} else {
				calendar.setTimeInMillis(Long
						.parseLong(getPersistedString((String) defaultValue)));
			}
		} else {
			if (defaultValue == null) {
				calendar.setTimeInMillis(System.currentTimeMillis());
			} else {
				calendar.setTimeInMillis(Long.parseLong((String) defaultValue));
			}
		}
		setSummary(getSummary());
	}

	@Override
	public CharSequence getSummary() {
		if (calendar == null) {
			return null;
		}
		return DateFormat.getTimeFormat(getContext()).format(
				new Date(calendar.getTimeInMillis()));
	}

	private void scheduleNotification(Notification notification) {

		Intent notificationIntent = new Intent(
				this.context.getApplicationContext(),
				NotificationPublisher.class);
		notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
		notificationIntent.putExtra(NotificationPublisher.NOTIFICATION,
				notification);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				this.context.getApplicationContext(), 0, notificationIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		long futureInMillis = PreferenceManager.getDefaultSharedPreferences(
				this.context.getApplicationContext()).getLong(
				"user.reminder.when", 0);
		Calendar setterCal = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		setterCal.setTimeInMillis(futureInMillis);
		if ((calendar1.get(Calendar.HOUR_OF_DAY) > setterCal
				.get(Calendar.HOUR_OF_DAY))
				&& (calendar1.get(Calendar.MINUTE) > setterCal
						.get(Calendar.MINUTE))) {
			setterCal.add(Calendar.DAY_OF_YEAR, 1);
		}
		calendar1
				.set(Calendar.HOUR_OF_DAY, setterCal.get(Calendar.HOUR_OF_DAY));
		calendar1.set(Calendar.MINUTE, setterCal.get(Calendar.MINUTE));
		AlarmManager alarmManager = (AlarmManager) this.context
				.getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				pendingIntent);
	}

	private Notification getNotification(String content) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this.context.getApplicationContext());
		builder.setContentTitle("Expense Manager");
		builder.setContentText(content);
		builder.setSmallIcon(R.drawable.ic_stat_expensify_icon);
		builder.setAutoCancel(true);
		builder.setTicker("Log your expense");
		Intent myIntent = new Intent(context.getApplicationContext(), ExpenseManager.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
               context.getApplicationContext(), 
               0, 
               myIntent, 
               Intent.FLAG_ACTIVITY_NEW_TASK);
        builder.setContentIntent(pendingIntent);
        //builder.setStyle(new NotificationCompat.InboxStyle());
        
		return builder.build();
	}
}