package com.example.demoaerisproject;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import com.aerisweather.aeris.logging.Logger;
import com.example.preference.PrefManager;
import com.example.service.NotificationJobService;
import com.example.service.NotificationService;

import com.aerisweather.aeris.communication.AerisEngine;
import com.aerisweather.aeris.maps.AerisMapsEngine;


public class BaseApplication extends Application
{
	private static final int REQUEST_NTF_SERVICE = 10;
	public static final String PRIMARY_NOTIF_CHANNEL = "default";
	public static final int NOTIFICATION_JOB_ID = 2001;
	public static final int PRIMARY_FOREGROUND_NOTIF_SERVICE_ID = 1001;
	private static final int ONE_MIN = 60 * 1000;
	private static final String TAG = BaseApplication.class.getSimpleName();

	@Override
	public void onCreate()
    {
		super.onCreate();

		// setting up secret key and client id for oauth to aeris
		AerisEngine.initWithKeys(this.getString(R.string.aerisapi_client_id), this.getString(R.string.aerisapi_client_secret), this);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationChannel chan1 = new NotificationChannel(
					PRIMARY_NOTIF_CHANNEL,
					"default",
					NotificationManager.IMPORTANCE_NONE);
			chan1.setLightColor(Color.TRANSPARENT);
			chan1.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
			notificationManager.createNotificationChannel(chan1);
		}

		// Setting up default options from res values in maps sdk.
		enableNotificationService(this, PrefManager.getBoolPreference(this, getString(R.string.pref_ntf_enabled)));

		/*
		 * can override default point parameters programmatically used on the
		 * map. dt:-1 -> sorts to closest time| -4hours -> 4 hours ago. Limit is
		 * a required parameter.Can also be done through the xml values in the
		 * aeris_default_values.xml
		 */
		AerisMapsEngine.getInstance(this).getDefaultPointParameters().setLightningParameters("dt:-1", 500, null, null);
    }

	public static void enableNotificationService(Context context, boolean enable)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			Logger.d(TAG, "enableNotificationService() - using JobScheduler");
			ComponentName notificationComponent = new ComponentName(context, NotificationJobService.class);
			JobInfo.Builder notificationBuilder = new JobInfo.Builder(NOTIFICATION_JOB_ID, notificationComponent)
					// schedule it to run any time between 15-20 minutes
					.setMinimumLatency(ONE_MIN * 15)
					.setOverrideDeadline(ONE_MIN * 20)
					.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
					.setPersisted(true);
			JobScheduler notificationJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
			notificationJobScheduler.schedule(notificationBuilder.build());
		}
		else
		{
			Intent intent = new Intent(context.getApplicationContext(), NotificationService.class);
			AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			PendingIntent pendingIntent;

			pendingIntent = PendingIntent.getService(context, PRIMARY_FOREGROUND_NOTIF_SERVICE_ID, intent, 0);

			if (enable)
			{
				manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 0, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
			}
			else
			{
				AerisNotification.cancelNotification(context);
				manager.cancel(pendingIntent);
			}
		}
	}

}
