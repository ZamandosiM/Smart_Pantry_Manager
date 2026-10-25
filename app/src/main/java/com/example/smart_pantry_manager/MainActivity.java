package com.example.smart_pantry_manager;

import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;
import android.os.Bundle;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private void loadIngredients() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getAllIngredients();

        ArrayList<String> ingredients = new ArrayList<>();
        ArrayList<Integer> ingredientIds = new ArrayList<>();

        while (cursor.moveToNext()) {
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow("name"));

            double quantity = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("quantity"));

            String unit = cursor.getString(
                    cursor.getColumnIndexOrThrow("unit"));

            String item = name + " - " +
                    String.format(Locale.getDefault(), "%s", quantity)
                    + " " + unit;
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );
            ingredientIds.add(id);
            ingredients.add(item);
        }

        cursor.close();
        databaseHelper.close();

        ListView listView = findViewById(R.id.listViewIngredients);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                ingredients
        );

        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener((parent, view, position, id) -> {

            int ingredientId = ingredientIds.get(position);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete Ingredient")
                    .setMessage("Are you sure you want to delete this ingredient?")
                    .setPositiveButton("Delete", (dialog, which) -> {

                        DatabaseHelper db = new DatabaseHelper(MainActivity.this);

                        if (db.deleteIngredient(ingredientId)) {
                            loadIngredients();
                        }

                        db.close();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

            return true;
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        loadIngredients();
        findViewById(R.id.btnAddIngredient).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddIngredientActivity.class);
            startActivity(intent);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadIngredients();
    }
}