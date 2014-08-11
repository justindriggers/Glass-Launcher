package com.jtxdriggers.android.glass.glasslauncher;

import java.util.ArrayList;

import net.java.frej.fuzzy.Fuzzy;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;

public class GlassLauncherSearch extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Start speech recognition intent
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say the name of the app you want to launch:");
		startActivityForResult(intent, 100);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 100 && resultCode == RESULT_OK) {
			ArrayList<String> spokenTexts = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			
			if (spokenTexts.size() > 0) {
				// Get the mApplications/mPackages ArrayLists
				ArrayList<ApplicationInfo> apps = GlassLauncherService.getApplications();
				ArrayList<String> packages = GlassLauncherService.getPackages();
				
				// Get the closest matched index in the apps ArrayList between the spoken text and an app name
				String spokenText = spokenTexts.get(0);
				int index = getAppMatch(spokenText, apps);
				
				Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packages.get(index-1));
				startActivity(launchIntent);
				
				finish();
			}
			else {
				Toast.makeText(this, "Didn't catch that.", Toast.LENGTH_SHORT).show();
			}
		}
	}

    /**
     * Finds the closest match between spoken text and app names
     * @param spokenText Transcribed text from speech
     * @param apps List of apps installed in the system
     * @return List index of the matched app
     */
	private int getAppMatch(final String spokenText, ArrayList<ApplicationInfo> apps) {
		int matchedIndex = -1;
		double matchedValue = 1.0;
		for (int i=1; i < apps.size(); i++) {
			double value = Fuzzy.similarity(spokenText, apps.get(i).title);
			
			if (value < matchedValue) {
				matchedValue = value;
				matchedIndex = i;
			}
		}
		return matchedIndex;
	}
}
