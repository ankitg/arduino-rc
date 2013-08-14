package com.nitinmalik.arduino_rc;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;
import fi.iki.elonen.HelloServer;


public class MainActivity extends Activity implements ShutterCallback, PictureCallback {
	
/** Called when the activity is first created. */
        
    private boolean mPermissionRequestPending;
    private PendingIntent mPermissionIntent;
    private UsbManager mUsbManager;
	
    static final String TAG = "com.nitinmalik.arduino_rc";
	static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	static final String ACTION_STOP_SERVICE = "com.victorint.android.usb.StartServiceActivity.action.STOP_SERVICE";
	
	private Intent startServiceIntent;
	private Camera mCamera;
    private CameraPreview mPreview;
	
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
		
		cameraStuff();
		initWebServer();
	}

	private void cameraStuff() {
		
		if (!checkCameraHardware(this)) {
			return;
		}
		
		// Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
//		cam.takePicture(this, this.rawCallback, this);
	}
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    	Log.d(TAG, "NO CAMERA" + e.toString());
	    	e.printStackTrace();
	    }
	    return c; // returns null if camera is unavailable
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
	
PictureCallback rawCallback = new PictureCallback() {
		
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - raw");
		}
		
	};
	
	PictureCallback jpegCallback = new PictureCallback() {
		
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - jpeg");
		}
		
	};
	
	@Override
	public void onShutter() {
		
		Log.d(TAG, "onShutter");
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		
//		Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
////		DisplayMetrics dm = new DisplayMetrics();
//		
//		ImageView imgView = (ImageView)findViewById(R.id.displayImageView);
//		imgView.setImageBitmap(bmp);
	}
	
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(
	            PackageManager.FEATURE_CAMERA)) {
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	    	Log.d(TAG, "This device doesn't have a camera.");
	        return false;
	    }
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		
		super.onConfigurationChanged(newConfig);
	}
	
	public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    //if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress() ) {
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress()) ) {
                        String ipAddr = inetAddress.getHostAddress();
                        return ipAddr;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.d(TAG, ex.toString());
        }
        return null;
    } 
	
	private void initWebServer() {
        String ipAddr = getLocalIpAddress();
        TextView textView = (TextView)findViewById(R.id.textView1);
        textView.setText(ipAddr.toString());
        
        Log.d(TAG, "The ip is "+ipAddr);
        
        HelloServer webserver = new HelloServer();
        
        try {
        	webserver.start();
		} catch (Exception e) {
			Log.d(TAG, "Webserver start:" + e.toString());
		}  
    }

}
