package com.weight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    private String gPerWeek;
    private String numberWeeks;
    private String todayWeight;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button calculate = findViewById(R.id.save);
        View configuration = findViewById(R.id.configuration);
        Button clear = findViewById(R.id.clear);
        loadSharedPreferences();
        calculate.setOnClickListener(view -> {
            loadData();
            if(validate()) {
                process();
            }
        });
        clear.setOnClickListener(view -> {
            clear();
        });
        configuration.setOnClickListener(view -> {
            Intent intent = new Intent(this, Configuration.class);
            startActivity(intent);
        });
    }

    private void loadSharedPreferences() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gPerWeek = sharedPreferences.getString("gWeek", "");
        if(!gPerWeek.isEmpty()) ((EditText) findViewById(R.id.gPerWeek)).setText(sharedPreferences.getString("gWeek", ""));
        numberWeeks = sharedPreferences.getString("numberWeeks", "");
        if(!numberWeeks.isEmpty()) ((EditText) findViewById(R.id.numberWeeks)).setText(sharedPreferences.getString("numberWeeks", ""));
        todayWeight = sharedPreferences.getString("todayWeight", "");
        if(!todayWeight.isEmpty()) ((EditText) findViewById(R.id.todayWeight)).setText(sharedPreferences.getString("todayWeight", ""));
    }

    private void loadData(){
        gPerWeek = ((EditText) findViewById(R.id.gPerWeek)).getText().toString();
        numberWeeks = ((EditText) findViewById(R.id.numberWeeks)).getText().toString();
        todayWeight = ((EditText) findViewById(R.id.todayWeight)).getText().toString();
    }

    private void clear() {
        ((EditText) findViewById(R.id.gPerWeek)).setText("");
        ((EditText) findViewById(R.id.numberWeeks)).setText("");
        ((EditText) findViewById(R.id.todayWeight)).setText("");
    }

    private boolean validate() {
        return !gPerWeek.isEmpty() && !numberWeeks.isEmpty() && !todayWeight.isEmpty();
    }

    private void process() {
        saveSharedPreferences();
        calculateInitialWeight();
    }

    private void calculateInitialWeight() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate initialDate = LocalDate.parse(sharedPreferences.getString("initialDate", "10/07/2019"), formatter);
        LocalDate finalDate = LocalDate.now().plusWeeks(Long.parseLong(numberWeeks));
        ((TextView) findViewById(R.id.finalDate)).setText(formatter.format(finalDate));
        double finalDay = DAYS.between(initialDate, finalDate);
        double todayDay = DAYS.between(initialDate, LocalDate.now());
        double todayWeight_ = Double.parseDouble(todayWeight) * 1000;
        double finalWeight = todayWeight_ + (Double.parseDouble(gPerWeek)/7) * Double.parseDouble(numberWeeks) * 7;
        ((TextView) findViewById(R.id.finalWeight)).setText(String.valueOf(finalWeight/1000));
        double initialWeight = ((finalWeight - todayWeight_)/(finalDay - todayDay)) * (0 - todayDay) + todayWeight_;
        ((TextView) findViewById(R.id.initialWeight)).setText(String.valueOf(Math.round((initialWeight/1000)*100.0)/100.0));
    }

    private void saveSharedPreferences() {
        editor.putString("gWeek", ((EditText) findViewById(R.id.gPerWeek)).getText().toString());
        editor.putString("numberWeeks", ((EditText) findViewById(R.id.numberWeeks)).getText().toString());
        editor.putString("todayWeight", ((EditText) findViewById(R.id.todayWeight)).getText().toString());
        editor.commit();
    }
}