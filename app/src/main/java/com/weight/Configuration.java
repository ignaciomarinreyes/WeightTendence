package com.weight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Configuration extends AppCompatActivity {

    String initialDate;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        Button return_ = findViewById(R.id.return_);
        return_.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initialDate = sharedPreferences.getString("initialDate", "");
        if(!initialDate.isEmpty()) ((EditText) findViewById(R.id.initialDate)).setText(sharedPreferences.getString("initialDate", ""));
        Button save = findViewById(R.id.save);
        save.setOnClickListener(view -> {
            initialDate = ((EditText) findViewById(R.id.initialDate)).getText().toString();
            if(isValid(initialDate)) {
                editor.putString("initialDate", initialDate);
                editor.commit();
                Toast toast = Toast.makeText(getApplicationContext(), "Fecha inicial guardada con éxito", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private boolean isValid(String dateString){
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateString);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}