package com.marginz.nexus4wirelesschargingfix;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.widget.Toast;
import android.util.Log;

public class DataReceiver extends BroadcastReceiver {
        private static final String TAG = "Nexus4Fix";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		SharedPreferences sp;
		sp=context.getSharedPreferences("com.marginz.n4",context.MODE_PRIVATE);
		int lastplugged=sp.getInt("lastplugged", 0);
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = context.registerReceiver(null, ifilter);
        int plugged = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
        Log.i(TAG,"Changed:"+lastplugged+"->"+plugged);
//        Toast.makeText(context, "Changed:"+lastplugged+"->"+plugged, Toast.LENGTH_SHORT).show();
        if(lastplugged==BatteryManager.BATTERY_PLUGGED_WIRELESS)
        	BackgroundIntentService.launchService(context);
        sp.edit().putInt("lastplugged", plugged).commit();
	}
}
