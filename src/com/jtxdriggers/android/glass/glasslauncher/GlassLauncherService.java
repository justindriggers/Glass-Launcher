/*
 * Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2013 Justin Driggers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jtxdriggers.android.glass.glasslauncher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

public class GlassLauncherService extends Service {
	
	private final IBinder mBinder = new GlassLauncherBinder();
	private final BroadcastReceiver mApplicationsReceiver = new ApplicationsIntentReceiver();
	
    private static ArrayList<ApplicationInfo> mApplications;
    
	private LiveCard mLiveCard;

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public class GlassLauncherBinder extends Binder {
		GlassLauncherService getService() {
			return GlassLauncherService.this;
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(mApplicationsReceiver, filter);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		loadApplications(false);
		publishCard(getApplicationContext());
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(mApplicationsReceiver);
		
		super.onDestroy();
	}
	
	/**
     * Loads the list of installed applications in mApplications.
     */
    private void loadApplications(boolean isLaunching) {
        if (isLaunching && mApplications != null) {
            return;
        }

        PackageManager manager = getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

        if (apps != null) {
            final int count = apps.size();

            if (mApplications == null) {
                mApplications = new ArrayList<ApplicationInfo>(count);
            }
            mApplications.clear();

            for (int i = 0; i < count; i++) {
                ApplicationInfo application = new ApplicationInfo();
                ResolveInfo info = apps.get(i);
                
                if (info.activityInfo.applicationInfo.packageName.equals("com.google.glass.home") ||
                		info.activityInfo.applicationInfo.packageName.equals(getPackageName())) {
                	continue;
                }

                application.title = info.loadLabel(manager);
                application.setActivity(new ComponentName(
                        info.activityInfo.applicationInfo.packageName,
                        info.activityInfo.name),
                        Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                application.icon = info.activityInfo.loadIcon(manager);

                mApplications.add(application);
            }
        }
        
        sendBroadcast(new Intent(MenuActivity.UPDATE_RECEIVER));
    }
    
    public static ArrayList<ApplicationInfo> getApplications() {
    	return mApplications;
    }

    /**
     * Receives notifications when applications are added/removed.
     */
    private class ApplicationsIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadApplications(false);
        }
    }
    
    private void publishCard(Context context) {
	    if (mLiveCard == null) {
	        String cardId = "glass_launcher";
	        mLiveCard = new LiveCard(context, cardId);
	
	        mLiveCard.setViews(new RemoteViews(context.getPackageName(),
	                R.layout.glasslauncher_layout));
	        Intent intent = new Intent(context, MenuActivity.class);
	        mLiveCard.setAction(PendingIntent.getActivity(context, 0,
	                intent, 0));
	        mLiveCard.publish(PublishMode.SILENT);
	    } else {
	        // Card is already published.
	        return;
	    }
	}

}
