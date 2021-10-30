package com.laila.unifacs.avaliacao1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.laila.unifacs.avaliacao1.R;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private final String PREFERENCE_NAME = "myPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void featureNotImplemented(View view) {
                Toast.makeText(this,
                R.string.feature_not_implemented_toast,
                Toast.LENGTH_SHORT).show();
    }

    public void changeToConfigScreen(View view) {
        Intent intent = new Intent(this, ConfigScreen.class);
        startActivity(intent);
    }

    public void changeToNavegationScreen(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void changeToCreditScreen (View view) {
        Intent intentCredit = new Intent(this, CreditScreen.class);
        startActivity(intentCredit);
    }

    public void getData() {

    }

}