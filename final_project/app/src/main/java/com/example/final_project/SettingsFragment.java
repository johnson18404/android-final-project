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


public class SettingsFragment extends PreferenceFragmentCompat {
    private Preference.OnPreferenceChangeListener onPreferenceChange;


//    private OnFragmentInteractionListener mListener;

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
                    ratio.setSummary(ratio_str);
                    Log.d("888",ratio_str);
                }
                else if (preference.getKey().equals("enlarge")){
                    String enlarge_str = o.toString();
                    enlarge_str = enlarge_str + "%";
                    enlarge.setSummary(enlarge_str);
                    Log.d("888",enlarge_str);
                }
                else if (preference.getKey().equals("server")){
                    String server_str = o.toString();
                    server.setSummary(server_str);
                    Log.d("888",server_str);
                }
                return false;
            }
        };

        ratio.setOnPreferenceChangeListener(listener);
        enlarge.setOnPreferenceChangeListener(listener);
        server.setOnPreferenceChangeListener(listener);



//        ratio.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//
//                String ratio_str = o.toString();
//                sp.edit().putString("face_ratio",ratio_str).apply();
//                ratio.setSummary(ratio_str);
//
//                return false;
//            }
//        });
//
//        enlarge.setSummary(sp.getString("enlarge", "30%"));
//
//        enlarge.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//
//                String enlarge_str = o.toString();
//
//                enlarge.setSummary(enlarge_str);
//
//                return false;
//            }
//        });






    }




    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        TextView textView = new TextView(getActivity());
//        textView.setText(R.string.hello_blank_fragment);
//        return textView;
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//            Log.d("258test","999");
//        }
//    }

//    private class OnFragmentInteractionListener {
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
