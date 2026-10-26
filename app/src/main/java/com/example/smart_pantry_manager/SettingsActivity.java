package com.example.smart_pantry_manager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        Spinner spinnerPreferredUnit = findViewById(R.id.spinnerPreferredUnit);
        Button btnSaveSettings = findViewById(R.id.btnSaveSettings);

        String[] units = {"Grams", "Kilograms", "Millilitres", "Litres", "Pieces"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerPreferredUnit.setAdapter(adapter);

        SharedPreferences preferences = getSharedPreferences(
                "SmartPantrySettings",
                MODE_PRIVATE
        );

        String savedUnit = preferences.getString("preferred_unit", "Grams");

        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(savedUnit)) {
                spinnerPreferredUnit.setSelection(i);
                break;
            }
        }

        btnSaveSettings.setOnClickListener(v -> {
            String selectedUnit = spinnerPreferredUnit.getSelectedItem().toString();

            preferences.edit()
                    .putString("preferred_unit", selectedUnit)
                    .apply();

            Toast.makeText(
                    SettingsActivity.this,
                    "Settings saved successfully",
                    Toast.LENGTH_SHORT
            ).show();
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}