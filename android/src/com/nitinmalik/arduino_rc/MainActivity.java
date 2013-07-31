package com.nitinmalik.arduino_rc;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	
/** Called when the activity is first created. */
    
    private String TAG = "StartServiceActivity";
    
    private boolean mPermissionRequestPending;
    private PendingIntent mPermissionIntent;
    private UsbManager mUsbManager;
	
	static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	static final String ACTION_STOP_SERVICE = "com.victorint.android.usb.StartServiceActivity.action.STOP_SERVICE";
	
	private Intent startServiceIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		registerReceiver(mUsbReceiver, filter);
		
//		UsbAccessory accessory;
//		mUsbManager.requestPermission(accessory, mPermissionIntent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbAccessory accessory = (UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						startService(startServiceIntent);
					} else {
						Log.d(TAG, "permission denied for accessory "
								+ accessory);
					}
					mPermissionRequestPending = false;
				}
				unregisterReceiver(mUsbReceiver);
				finish();
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
			}
		}
	};

}
