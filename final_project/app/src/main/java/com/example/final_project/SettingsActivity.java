package com.example.final_project;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;


public class SettingsActivity extends AppCompatActivity {

//    Preference.OnPreferenceChangeListener changeListener;




//    private static Preference.OnPreferenceChangeListener
//            sBindPreferenceSummaryToValueListener =
//            new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object value) {
//                    String stringValue = value.toString();
//                    Log.d("000test",stringValue);
//
//
//
//                    preference.setSummary(stringValue);
//
//                    if (preference instanceof EditTextPreference){
//                        preference.setSummary(stringValue);
//                    }


//                    if (preference instanceof ListPreference) {
//                        // For list preferences, look up the correct display value in
//                        // the preference's 'entries' list.
//                        ListPreference listPreference = (ListPreference) preference;
//                        int index = listPreference.findIndexOfValue(stringValue);
//
//                        // Set the summary to reflect the new value.
//                        preference.setSummary(
//                                index >= 0
//                                        ? listPreference.getEntries()[index]
//                                        : null);
//
//                    } else if (preference instanceof RingtonePreference) {
//                        // For ringtone preferences, look up the correct display value
//                        // using RingtoneManager.
//                        if (TextUtils.isEmpty(stringValue)) {
//                            // Empty values correspond to 'silent' (no ringtone).
//                            preference.setSummary(R.string.face_ratio_title);
//
//                        } else {
//                            Ringtone ringtone = RingtoneManager.getRingtone(
//                                    preference.getContext(), Uri.parse(stringValue));
//
//                            if (ringtone == null) {
//                                // Clear the summary if there was a lookup error.
//                                preference.setSummary(null);
//                            } else {
//                                // Set the summary to reflect the new ringtone display
//                                // name.
//                                String name = ringtone.getTitle(preference.getContext());
//                                preference.setSummary(name);
//                            }
//                        }
//
//                    } else {
//                        // For all other preferences, set the summary to the value's
//                        // simple string representation.
//                        preference.setSummary(stringValue);
//                    }
//                    return true;
//                }
//            };

//    private static void bindPreferenceSummaryToValue(Preference preference) {
//        // Set the listener to watch for value changes.
//        preference.setOnPreferenceChangeListener(
//                sBindPreferenceSummaryToValueListener);
//
//        // Trigger the listener immediately with the preference's
//        // current value.
//        sBindPreferenceSummaryToValueListener
//                .onPreferenceChange(preference, PreferenceManager
//                        .getDefaultSharedPreferences(preference.getContext())
//                        .getString(preference.getKey(), ""));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

//        PreferenceManager.setDefaultValues(this,
//                R.xml.preferences, false);



//        changeListener = new Preference.OnPreferenceChangeListener() {
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//
//                String stringValue = newValue.toString();
//                Log.d("000test",stringValue);
//
//                return true;
//            }
//        };
//        bindPreferenceSummaryToValue(findPreference("face_ratio_title"));




    }


}
