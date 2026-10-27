package com.example.smart_pantry_manager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddIngredientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        Spinner spinnerUnit = findViewById(R.id.spinnerUnit);

        String[] units = {
                "Pieces",
                "Grams",
                "Kilograms",
                "Millilitres",
                "Litres"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerUnit.setAdapter(adapter);
        EditText editName = findViewById(R.id.editIngredientName);
        EditText editQuantity = findViewById(R.id.editQuantity);
        Button btnSave = findViewById(R.id.btnSaveIngredient);
        int ingredientId = getIntent().getIntExtra("ingredient_id", -1);

        if (ingredientId !=-1) {

        DatabaseHelper db = new DatabaseHelper(this);
        Cursor cursor = db.getIngredientById(ingredientId);

        if (cursor.moveToFirst()) {
            String existingName = cursor.getString(
                    cursor.getColumnIndexOrThrow("name"));

            double existingQuantity = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("quantity"));

            String existingUnit = cursor.getString(
                    cursor.getColumnIndexOrThrow("unit"));

            editName.setText(existingName);
            editQuantity.setText(String.valueOf(existingQuantity));

            if (existingUnit != null) {
                for (int i = 0; i < spinnerUnit.getCount(); i++) {
                    if (spinnerUnit.getItemAtPosition(i).toString().equals(existingUnit)) {
                        spinnerUnit.setSelection(i);
                        break;
                    }
                }
            }
        }

        cursor.close();
        db.close();

            btnSave.setText("Update Ingredient");
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        btnSave.setOnClickListener(v -> {

            String name = editName.getText().toString().trim();
            String quantityText = editQuantity.getText().toString().trim();
            String unit = spinnerUnit.getSelectedItem().toString();

            if (name.isEmpty() || quantityText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double quantity = Double.parseDouble(quantityText);

            if (quantity <= 0) {
                Toast.makeText(this, "Quantity must be greater than zero", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success;

            if (ingredientId != -1) {
                success = databaseHelper.updateIngredient(
                        ingredientId, name, quantity, unit
                );
            } else {
                success = databaseHelper.addIngredient(name, quantity, unit);
            }

            if (success) {
                Toast.makeText(this, "Ingredient saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save ingredient", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
