package com.example.smart_pantry_manager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.Cursor;
import android.widget.ListView;
import java.util.ArrayList;
import android.content.Intent;

public class SuggestRecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggest_recipes);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        ListView listViewRecipes = findViewById(R.id.listViewRecipes);

        ArrayList<String> recipeNames = new ArrayList<>();
        ArrayList<Long> recipeIds = new ArrayList<>();

        Cursor cursor = databaseHelper.getSuggestedRecipes();

        while (cursor.moveToNext()) {
            String recipeName = cursor.getString(
                    cursor.getColumnIndexOrThrow("name")
            );

            recipeNames.add(recipeName);

            long recipeId = cursor.getLong(
                    cursor.getColumnIndexOrThrow("id")
            );
            recipeIds.add(recipeId);
        }

        cursor.close();

        RecipeAdapter adapter = new RecipeAdapter(this, recipeNames);
        listViewRecipes.setAdapter(adapter);

        listViewRecipes.setAdapter(adapter);

        listViewRecipes.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(
                    SuggestRecipesActivity.this,
                    RecipeDetailsActivity.class
            );

            intent.putExtra("recipe_id", recipeIds.get(position));
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}