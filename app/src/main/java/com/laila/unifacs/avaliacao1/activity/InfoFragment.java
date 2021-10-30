package com.laila.unifacs.avaliacao1.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.laila.unifacs.avaliacao1.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static TextView latitudeTextView;
    private static TextView longitudeTextView;
    private static TextView velocidadeTextView;

    private SharedPreferences sharedPreferences;

    private final String PREFERENCE_NAME = "myPref";
    private final String VELOCIDADE_KEY = "velocidadeKey";
    private final String LATITUDE_KEY = "latitudeKey";
    private final String LONGITUDE_KEY = "longitudeKey";

    public static String latitude = "", longitude = "", velocidade = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static View myView;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getPreferences();

        myView = inflater.inflate(R.layout.fragment_info, container, false);

        latitudeTextView = (TextView) myView.findViewById(R.id.latitude_textView);
        longitudeTextView = (TextView) myView.findViewById(R.id.longitude_textView);
        velocidadeTextView = (TextView) myView.findViewById(R.id.velocidade_textView);

        String latitudeString = getResources().getString(R.string.current_latitude);
        String longitudeString = getResources().getString(R.string.current_longitude);
        String velocidadeString = getResources().getString(R.string.current_speed);

        latitudeTextView.setText(latitudeString + latitude);
        longitudeTextView.setText(longitudeString + longitude);
        velocidadeTextView.setText(velocidadeString + velocidade);

        return myView;
    }

    private void getPreferences() {

//        this.sharedPreferences = this.getActivity().getSharedPreferences(this.PREFERENCE_NAME, 0);
//
//        this.latitude = this.sharedPreferences.getString(LATITUDE_KEY, "");
//        this.longitude = this.sharedPreferences.getString(LONGITUDE_KEY, "");
//        this.velocidade = this.sharedPreferences.getString(VELOCIDADE_KEY, "");

    }

    public static void setValues(String latitude, String longitude, String velocidade)
    {
//        InfoFragment.latitude = latitude;
//        View myView = inflater.inflate(R.layout.fragment_info, container, false);
//        String latitudeString = getResources().getString(R.string.current_latitude);
//        String longitudeString = getResources().getString(R.string.current_longitude);
//        String velocidadeString = getResources().getString(R.string.current_speed);
        latitudeTextView = (TextView) myView.findViewById(R.id.latitude_textView);
        latitudeTextView.setText("latitude: " + latitude);
        longitudeTextView = (TextView) myView.findViewById(R.id.longitude_textView);
        longitudeTextView.setText("longitude: " + longitude);

        velocidadeTextView = (TextView) myView.findViewById(R.id.velocidade_textView);
        velocidadeTextView.setText("velocity: " + velocidade);
    }

}