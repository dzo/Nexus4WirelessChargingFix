package com.marginz.nexus4wirelesschargingfix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BackgroundIntentService extends IntentService {
	private static final String TAG = "Nexus4Fix";

	public static void launchService(Context context) {
		if (context == null) return;
		context.startService(new Intent(context, BackgroundIntentService.class));
	}

	public BackgroundIntentService() {
		super("BackgroundIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			Process p=null;
			p=Runtime.getRuntime().exec("ps sensors.qcom");
			BufferedReader reader = new BufferedReader( new InputStreamReader(p.getInputStream()),8192);
			String l=reader.readLine();
			l=reader.readLine();
			if(l==null) 
				return;
			p.waitFor();
			String[] wds=l.split("\\s+");
			Log.i(TAG,"Found sensors process:"+wds[1]);
			p=Runtime.getRuntime().exec("su -c kill -9 "+wds[1]+"");
//			reader = new BufferedReader( new InputStreamReader(p.getInputStream()),8192);
//			while ((l=reader.readLine())!=null)
//				Log.i(TAG,l);
			p.waitFor();
			p=Runtime.getRuntime().exec("su -c echo PowerManagerService.WirelessChargerDetector > /sys/power/wake_unlock  ");
			p.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// the code you put here will be executed in a background thread
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
