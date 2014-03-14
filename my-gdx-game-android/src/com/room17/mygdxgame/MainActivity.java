package com.room17.mygdxgame;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.room17.shakey.logic.ShakeDetector;
import com.room17.shakey.logic.Shaker;

public class MainActivity extends AndroidApplication {
	
//	private SharedPreferences mSettings;
//	private PedometerSettings mPedometerSettings;
//	
//	private int mStepValue;
//	private static int outputStep;
//	private boolean mQuitting = false; 
//	private boolean mIsRunning;

	private Shaker shakey;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		shakey = new Shaker(this.getContext());
		shakey.addListener(new ShakeDetector() {
			@Override
			public void shakeDetected() {
				vibeShake();
			}
		});
		
//		mStepValue = 0;

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = true;

		initialize(new MyGdxGame(), cfg);
	}

	public void vibeShake() {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(100);
	}

	@Override
	protected void onResume() {
		super.onResume();
		shakey.start();
		getWindow().addFlags(
				android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
//		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
//		mPedometerSettings = new PedometerSettings(mSettings);
//
//		mIsRunning = mPedometerSettings.isServiceRunning();
//
//		if (!mIsRunning && mPedometerSettings.isNewStart()) {
//			startStepService();
//			bindStepService();
//		} else if (mIsRunning) {
//			bindStepService();
//			InStream.addSteps(mStepValue);
//		}
//
//		mPedometerSettings.clearServiceRunning();
		
	}

	@Override
	protected void onPause() {
		getWindow().clearFlags(
				android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		shakey.stop();
		
//		if (mIsRunning) {
//			resetValues(true);
//			unbindStepService();
//		}
//		if (mQuitting) {
//			mPedometerSettings.saveServiceRunningWithNullTimestamp(mIsRunning);
//		} else {
//			mPedometerSettings.saveServiceRunningWithTimestamp(mIsRunning);
//		}
		
		super.onPause();
	}
	
//	private StepService mService;
//
//	private ServiceConnection mConnection = new ServiceConnection() {
//		public void onServiceConnected(ComponentName className, IBinder service) {
//			mService = ((StepService.StepBinder) service).getService();
//
//			mService.registerCallback(mCallback);
//			mService.reloadSettings();
//
//		}
//
//		public void onServiceDisconnected(ComponentName className) {
//			mService = null;
//		}
//	};
//
//	private void startStepService() {
//		if (!mIsRunning) {
//			mIsRunning = true;
//			startService(new Intent(MainActivity.this, StepService.class));
//		}
//	}
//
//	private void bindStepService() {
//		bindService(new Intent(MainActivity.this, StepService.class), mConnection,
//				Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
//	}
//
//	private void unbindStepService() {
//		unbindService(mConnection);
//	}
//
//	private void stopStepService() {
//		if (mService != null) {
//			stopService(new Intent(MainActivity.this, StepService.class));
//		}
//		mIsRunning = false;
//	}
//
//	private void resetValues(boolean updateDisplay) {
//		if (mService != null && mIsRunning) {
//			mService.resetValues();
//		} else {
//			SharedPreferences state = getSharedPreferences("state", 0);
//			SharedPreferences.Editor stateEditor = state.edit();
//			if (updateDisplay) {
//				stateEditor.putInt("steps", 0);
//				stateEditor.commit();
//			}
//		}
//	}
//	
//	private StepService.ICallback mCallback = new StepService.ICallback() {
//		public void stepsChanged(int value) {
//			mHandler.sendMessage(mHandler.obtainMessage(STEPS_MSG, value, 0));
//		}
//	};
//
//	private static final int STEPS_MSG = 1;
//
//	@SuppressLint("HandlerLeak")
//	private Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case STEPS_MSG:
//				mStepValue = (int) msg.arg1;
//				SharedPreferences settings = getSharedPreferences("NumberOfSteps", 0); // create or get the container
//				outputStep = settings.getInt("steps", 0); //get the steps if exists, else get 0
//				break;
//			default:
//				super.handleMessage(msg);
//			}
//		}
//	};
//	
//	//steps getter for widget
//	public static int getStep(){
//		return outputStep;
//	}
//	
//	@Override
//	public void onBackPressed() {
//		resetValues(false);
//		unbindStepService();
//		stopStepService();
//		mQuitting = true;
//		finish();
//	}
}