/*
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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MenuActivity extends Activity {
	
	public static final String UPDATE_RECEIVER = "com.justindriggers.android.glass.glasslauncher.UPDATE_RECEIVER";
	
	private ApplicationsUpdateReceiver mApplicationsReceiver = new ApplicationsUpdateReceiver();
	private ArrayList<ApplicationInfo> mApplications;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		registerReceiver(mApplicationsReceiver, new IntentFilter(UPDATE_RECEIVER));
	    
	    mApplications = GlassLauncherService.getApplications();
		
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		openOptionsMenu();
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(mApplicationsReceiver);
		
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (mApplications.isEmpty()) {
			MenuItem item = menu.add(Menu.NONE, -1, Menu.NONE, "No Apps Found");
			item.setIcon(R.drawable.ic_warning_50);
		} else {
			ApplicationInfo app;
			MenuItem item;
			
			for (int i = 0; i < mApplications.size(); i++) {
				app = mApplications.get(i);
				item = menu.add(Menu.NONE, i, i+1, app.title);
				item.setIcon(app.icon);
			}
		}
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == -1) {
			return true;
		}
		
		Intent intent = mApplications.get(item.getItemId()).intent;
		startActivity(intent);
		
		return true;
	}
	
	@Override
	public void onOptionsMenuClosed(Menu menu) {
	    finish();
	}
	
	private class ApplicationsUpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent arg1) {
			startActivity(new Intent(context, MenuActivity.class));
		}
	}

}
