package com.example.final_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;


public class SettingsFragment extends PreferenceFragmentCompat {


    public SettingsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreatePreferences(Bundle
                                            savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.preferences, rootKey);

        final SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        final EditTextPreference ratio = (EditTextPreference)findPreference( "face_ratio" );
        final ListPreference enlarge = (ListPreference)findPreference( "enlarge" );
        final ListPreference server = (ListPreference)findPreference( "server" );
        ratio.setSummary(sp.getString("face_ratio", "0.0002"));
        enlarge.setSummary(sp.getString("enlarge", "30%"));
        server.setSummary(sp.getString("server", "server 1"));
        Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (preference.getKey().equals("face_ratio")){
                    String ratio_str = o.toString();
                    sp.edit().putString("face_ratio", ratio_str).apply();
                    ratio.setSummary(ratio_str);
                    Log.d("888",ratio_str);

                }
                else if (preference.getKey().equals("enlarge")){
                    String enlarge_str = o.toString();
                    enlarge_str = enlarge_str + "%";
                    sp.edit().putString("enlarge", enlarge_str).apply();
                    enlarge.setSummary(enlarge_str);
                    Log.d("888",enlarge_str);
                }
                else if (preference.getKey().equals("server")){
                    String server_str = o.toString();
                    sp.edit().putString("server", server_str).apply();
                    server.setSummary(server_str);
                    Log.d("888",server_str);
                }
                return false;
            }
        };

        ratio.setOnPreferenceChangeListener(listener);
        enlarge.setOnPreferenceChangeListener(listener);
        server.setOnPreferenceChangeListener(listener);

    }

}
