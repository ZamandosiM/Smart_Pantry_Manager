package com.example.smart_pantry_manager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.Cursor;
import android.widget.TextView;

public class RecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_details);
        TextView textRecipeName = findViewById(R.id.textRecipeName);
        TextView textRecipeIngredients = findViewById(R.id.textRecipeIngredients);
        TextView textRecipeInstructions = findViewById(R.id.textRecipeInstructions);

        long recipeId = getIntent().getLongExtra("recipe_id", -1);

        if (recipeId == -1) {
            finish();
            return;
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

// Retrieve recipe name and instructions
        Cursor recipeCursor = databaseHelper.getRecipeById(recipeId);

        if (recipeCursor.moveToFirst()) {
            String name = recipeCursor.getString(
                    recipeCursor.getColumnIndexOrThrow("name")
            );

            String instructions = recipeCursor.getString(
                    recipeCursor.getColumnIndexOrThrow("instructions")
            );

            textRecipeName.setText(name);
            textRecipeInstructions.setText(instructions);
        } else {
            recipeCursor.close();
            finish();
            return;
        }

        recipeCursor.close();

// Retrieve recipe ingredients
        Cursor ingredientsCursor = databaseHelper.getRecipeIngredients(recipeId);

        StringBuilder ingredients = new StringBuilder();

        while (ingredientsCursor.moveToNext()) {
            String ingredientName = ingredientsCursor.getString(
                    ingredientsCursor.getColumnIndexOrThrow("ingredient_name")
            );

            double quantity = ingredientsCursor.getDouble(
                    ingredientsCursor.getColumnIndexOrThrow("quantity")
            );

            String unit = ingredientsCursor.getString(
                    ingredientsCursor.getColumnIndexOrThrow("unit")
            );

            ingredients.append("• ")
                    .append(ingredientName)
                    .append(" - ")
                    .append(quantity)
                    .append(" ")
                    .append(unit)
                    .append("\n");
        }

        ingredientsCursor.close();

        textRecipeIngredients.setText(ingredients.toString());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}