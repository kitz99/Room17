package com.room17.pedometer.logic;


import com.room17.mygdxgame.MyWidgetProvider;
import com.room17.mygdxgame.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class StepService extends Service {
	private static final String TAG = "name.bagi.levente.pedometer.StepService";
	private SharedPreferences mSettings;
	private PedometerSettings mPedometerSettings;
	private SharedPreferences mState;
	private SharedPreferences.Editor mStateEditor;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private StepDetector mStepDetector;
	private StepDisplayer mStepDisplayer;

	private PowerManager.WakeLock wakeLock;

	private static int mSteps;
	private int mPace;
	private float mDistance;
	private float mSpeed;
	private float mCalories;

	public class StepBinder extends Binder {
		public StepService getService() {
			return StepService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
		mPedometerSettings = new PedometerSettings(mSettings);
		mState = getSharedPreferences("state", 0);

		acquireWakeLock();

		mStepDetector = new StepDetector();
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		registerDetector();

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mReceiver, filter);

		mStepDisplayer = new StepDisplayer(mPedometerSettings);
		mStepDisplayer.setSteps(mSteps = mState.getInt("steps", 0));
		mStepDisplayer.addListener(mStepListener);
		mStepDetector.addStepListener(mStepDisplayer);

		reloadSettings();

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {

		unregisterReceiver(mReceiver);
		unregisterDetector();

		mStateEditor = mState.edit();
		mStateEditor.putInt("steps", mSteps);
		mStateEditor.putInt("pace", mPace);
		mStateEditor.putFloat("distance", mDistance);
		mStateEditor.putFloat("speed", mSpeed);
		mStateEditor.putFloat("calories", mCalories);
		mStateEditor.commit();

		wakeLock.release();

		super.onDestroy();

		mSensorManager.unregisterListener(mStepDetector);

	}

	private void registerDetector() {
		mSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER /*
															 * | Sensor.
															 * TYPE_MAGNETIC_FIELD
															 * | Sensor.
															 * TYPE_ORIENTATION
															 */);
		mSensorManager.registerListener(mStepDetector, mSensor,
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	private void unregisterDetector() {
		mSensorManager.unregisterListener(mStepDetector);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		RemoteViews view = new RemoteViews(getPackageName(),
				R.layout.widget_layout);

		view.setTextViewText(R.id.update, mSteps + " steps so far.");

		// Push update for this widget to the home screen
		ComponentName thisWidget = new ComponentName(this,
				MyWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		manager.updateAppWidget(thisWidget, view);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "[SERVICE] onBind");
		return mBinder;
	}

	private final IBinder mBinder = new StepBinder();

	public interface ICallback {
		public void stepsChanged(int value);
	}

	private ICallback mCallback;

	public void registerCallback(ICallback cb) {
		mCallback = cb;
		// mStepDisplayer.passValue();
		// mPaceListener.passValue();
	}

	public void reloadSettings() {
		mSettings = PreferenceManager.getDefaultSharedPreferences(this);

		if (mStepDetector != null) {
			mStepDetector.setSensitivity(Float.valueOf(mSettings.getString(
					"sensitivity", "10")));
		}

		if (mStepDisplayer != null)
			mStepDisplayer.reloadSettings();
	}

	public void resetValues() {
		mStepDisplayer.setSteps(0);
	}

	private StepDisplayer.Listener mStepListener = new StepDisplayer.Listener() {
		public void stepsChanged(int value) {
			mSteps = value;
			writeStep(); // write steps for widget
			createNotification();// create the notification
			passValue();
		}

		public void passValue() {
			if (mCallback != null) {
				mCallback.stepsChanged(mSteps);
			}
		}
	};

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				StepService.this.unregisterDetector();
				StepService.this.registerDetector();
				if (mPedometerSettings.wakeAggressively()) {
					wakeLock.release();
					acquireWakeLock();
				}
			}
		}
	};

	@SuppressWarnings("deprecation")
	private void acquireWakeLock() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		int wakeFlags;
		if (mPedometerSettings.wakeAggressively()) {
			wakeFlags = PowerManager.SCREEN_DIM_WAKE_LOCK
					| PowerManager.ACQUIRE_CAUSES_WAKEUP;
		} else if (mPedometerSettings.keepScreenOn()) {
			wakeFlags = PowerManager.SCREEN_DIM_WAKE_LOCK;
		} else {
			wakeFlags = PowerManager.PARTIAL_WAKE_LOCK;
		}
		wakeLock = pm.newWakeLock(wakeFlags, TAG);
		wakeLock.acquire();
	}

	// write no. of steps for later access
	public void writeStep() {
		SharedPreferences settings = getSharedPreferences("NumberOfSteps", 0); // create
																				// or
																				// get
																				// container
		SharedPreferences.Editor editor = settings.edit(); // create an editor
															// to write
		editor.putInt("steps", mSteps); // write as "steps"
		editor.commit(); // Commit the edits!
	}

	@SuppressWarnings("deprecation")
	public void createNotification() {

		if (mSteps > 0 && mSteps % 100 == 0) {

			// Prepare intent which is triggered if the
			// notification is selected (eg: open the MainActivity). In our case just make it null (third parameter)
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, null,
					0);

			// Build notification
			CharSequence text = "ZombieJim " + mSteps + " steps";
			CharSequence contentTitle = "ZombieJim";
			CharSequence contentText = "Congratulations! You walked " + mSteps + " steps so far.";
			long when = System.currentTimeMillis();

			//this appears in the notification bar
			Notification notification = new Notification(
					R.drawable.ic_launcher, text, when);
			//make the notification cancel when tapped
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			
			//this appears when you see the full notification
			notification.setLatestEventInfo(this, contentTitle, contentText,
					pIntent);
			
			//get the notification service
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			//put the notification
			notificationManager.notify(1234509876, notification);
		}
	}

}
