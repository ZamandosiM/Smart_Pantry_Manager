package com.example.smart_pantry_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL)";

        db.execSQL(createTable);

        String createRecipesTable = "CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "instructions TEXT NOT NULL)";

        db.execSQL(createRecipesTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            String createRecipesTable = "CREATE TABLE IF NOT EXISTS recipes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "instructions TEXT NOT NULL)";

            db.execSQL(createRecipesTable);
        }

        if (oldVersion < 3) {
            String createRecipeIngredientsTable =
                    "CREATE TABLE IF NOT EXISTS recipe_ingredients (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "recipe_id INTEGER NOT NULL, " +
                            "ingredient_name TEXT NOT NULL, " +
                            "quantity REAL NOT NULL, " +
                            "unit TEXT NOT NULL)";

            db.execSQL(createRecipeIngredientsTable);
        }
    }
    public boolean addIngredient(String name, double quantity, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);

        long result = db.insert("ingredients", null, values);
        db.close();

        return result != -1;
    }
    public Cursor getAllIngredients() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM ingredients ORDER BY name ASC",
                null
        );
    }
    public boolean deleteIngredient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                "ingredients",
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return result > 0;
    }
    public boolean updateIngredient(int id, String name, double quantity, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);

        int result = db.update(
                "ingredients",
                values,
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return result > 0;
    }
    public Cursor getIngredientById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM ingredients WHERE id = ?",
                new String[]{String.valueOf(id)}
        );
    }
    public long addRecipe(String name, String instructions) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("instructions", instructions);

        long recipeId = db.insert("recipes", null, values);

        db.close();

        return recipeId;
    }
    public boolean addRecipeIngredient(long recipeId, String ingredientName,
                                       double quantity, String unit) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("ingredient_name", ingredientName);
        values.put("quantity", quantity);
        values.put("unit", unit);

        long result = db.insert("recipe_ingredients", null, values);

        db.close();

        return result != -1;
    }
    public boolean hasRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM recipes",
                null
        );

        boolean hasRecipes = false;

        if (cursor.moveToFirst()) {
            hasRecipes = cursor.getInt(0) > 0;
        }

        cursor.close();

        return hasRecipes;
    }
    public void preloadRecipes() {

        // Keep existing recipes and add only missing ones.
        String[] names = {
                "Pancakes",
                "Scrambled Eggs",
                "Boiled Eggs",
                "Fried Eggs",
                "French Toast",
                "Omelette",
                "Toast with Butter",
                "Egg Sandwich",
                "Cheese Sandwich",
                "Grilled Cheese",
                "Tomato Sandwich",
                "Rice",
                "Egg Fried Rice",
                "Tomato Pasta",
                "Butter Pasta",
                "Mashed Potatoes",
                "Roasted Potatoes",
                "Vegetable Soup",
                "Banana Smoothie",
                "Fruit Salad"
        };

        String[] instructions = {
                "1. Mix flour and sugar.\n2. Add eggs and milk.\n3. Stir until smooth.\n4. Cook in a lightly greased pan until golden.",
                "1. Beat eggs with milk.\n2. Melt butter in a pan.\n3. Cook the eggs gently, stirring until set.",
                "1. Place eggs in water.\n2. Bring to a boil.\n3. Simmer until cooked.\n4. Cool and peel.",
                "1. Heat butter in a pan.\n2. Crack eggs into the pan.\n3. Cook until the whites are set.",
                "1. Beat eggs with milk.\n2. Dip bread into the mixture.\n3. Fry in butter until golden on both sides.",
                "1. Beat eggs with milk.\n2. Heat butter in a pan.\n3. Add eggs and cheese.\n4. Fold when set.",
                "1. Toast bread.\n2. Spread butter over the toast.\n3. Serve warm.",
                "1. Boil eggs and peel them.\n2. Mash with mayonnaise.\n3. Spread between bread slices.",
                "1. Place cheese between two slices of bread.\n2. Cut and serve.",
                "1. Put cheese between bread slices.\n2. Butter the outside.\n3. Toast in a pan until golden and melted.",
                "1. Slice tomato.\n2. Place tomato between bread slices.\n3. Serve.",
                "1. Rinse rice.\n2. Add rice and water to a pot.\n3. Simmer covered until cooked.",
                "1. Cook rice and allow it to cool.\n2. Scramble eggs in oil.\n3. Add rice and stir-fry.",
                "1. Boil pasta until tender.\n2. Heat tomato sauce.\n3. Combine pasta and sauce.",
                "1. Cook pasta until tender.\n2. Drain.\n3. Mix with melted butter.",
                "1. Peel and chop potatoes.\n2. Boil until soft.\n3. Mash with milk and butter.",
                "1. Chop potatoes.\n2. Coat with oil.\n3. Roast at 200°C until tender and golden.",
                "1. Chop carrots, potatoes and onion.\n2. Add water.\n3. Simmer until vegetables are tender.",
                "1. Peel banana.\n2. Blend with milk until smooth.\n3. Serve chilled.",
                "1. Wash and chop apple, banana and orange.\n2. Mix together and serve."
        };

        String[][] ingredients = {
                {"Flour", "Milk", "Eggs", "Sugar"},
                {"Eggs", "Milk", "Butter"},
                {"Eggs"},
                {"Eggs", "Butter"},
                {"Bread", "Eggs", "Milk", "Butter"},
                {"Eggs", "Milk", "Butter", "Cheese"},
                {"Bread", "Butter"},
                {"Eggs", "Mayonnaise", "Bread"},
                {"Bread", "Cheese"},
                {"Bread", "Cheese", "Butter"},
                {"Bread", "Tomato"},
                {"Rice", "Water"},
                {"Rice", "Eggs", "Oil"},
                {"Pasta", "Tomato Sauce"},
                {"Pasta", "Butter"},
                {"Potatoes", "Milk", "Butter"},
                {"Potatoes", "Oil"},
                {"Carrots", "Potatoes", "Onion", "Water"},
                {"Banana", "Milk"},
                {"Apple", "Banana", "Orange"}
        };

        double[][] quantities = {
                {200, 250, 2, 20},
                {2, 30, 10},
                {2},
                {2, 10},
                {2, 1, 50, 10},
                {2, 30, 10, 30},
                {2, 10},
                {2, 20, 2},
                {2, 40},
                {2, 40, 10},
                {2, 1},
                {150, 300},
                {150, 2, 15},
                {150, 150},
                {150, 20},
                {300, 50, 20},
                {300, 20},
                {100, 200, 1, 500},
                {1, 250},
                {1, 1, 1}
        };

        String[][] units = {
                {"Grams", "Millilitres", "Pieces", "Grams"},
                {"Pieces", "Millilitres", "Grams"},
                {"Pieces"},
                {"Pieces", "Grams"},
                {"Pieces", "Pieces", "Millilitres", "Grams"},
                {"Pieces", "Millilitres", "Grams", "Grams"},
                {"Pieces", "Grams"},
                {"Pieces", "Grams", "Pieces"},
                {"Pieces", "Grams"},
                {"Pieces", "Grams", "Grams"},
                {"Pieces", "Pieces"},
                {"Grams", "Millilitres"},
                {"Grams", "Pieces", "Millilitres"},
                {"Grams", "Grams"},
                {"Grams", "Grams"},
                {"Grams", "Millilitres", "Grams"},
                {"Grams", "Millilitres"},
                {"Grams", "Grams", "Pieces", "Millilitres"},
                {"Pieces", "Millilitres"},
                {"Pieces", "Pieces", "Pieces"}
        };

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        try {
            for (int i = 0; i < names.length; i++) {

                Cursor existing = db.rawQuery(
                        "SELECT id FROM recipes WHERE name = ? LIMIT 1",
                        new String[]{names[i]}
                );

                boolean alreadyExists = existing.moveToFirst();
                existing.close();

                if (alreadyExists) {
                    continue;
                }

                ContentValues recipeValues = new ContentValues();
                recipeValues.put("name", names[i]);
                recipeValues.put("instructions", instructions[i]);

                long recipeId = db.insert(
                        "recipes", null, recipeValues
                );

                if (recipeId == -1) {
                    throw new IllegalStateException(
                            "Could not save recipe: " + names[i]
                    );
                }

                for (int j = 0; j < ingredients[i].length; j++) {
                    ContentValues ingredientValues = new ContentValues();

                    ingredientValues.put("recipe_id", recipeId);
                    ingredientValues.put("ingredient_name", ingredients[i][j]);
                    ingredientValues.put("quantity", quantities[i][j]);
                    ingredientValues.put("unit", units[i][j]);

                    if (db.insert("recipe_ingredients", null, ingredientValues) == -1) {
                        throw new IllegalStateException(
                                "Could not save ingredient for " + names[i]
                        );
                    }
                }
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }
    public Cursor getAllRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT id, name, instructions FROM recipes",
                null
        );
    }
    public Cursor getRecipeById(long recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT id, name, instructions FROM recipes WHERE id = ?",
                new String[]{String.valueOf(recipeId)}
        );
    }
    public Cursor getRecipeIngredients(long recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT ingredient_name, quantity, unit " +
                        "FROM recipe_ingredients WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeId)}
        );
    }
    public Cursor getSuggestedRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query =
                "SELECT r.id, r.name, r.instructions " +
                        "FROM recipes r " +
                        "WHERE NOT EXISTS (" +
                        " SELECT 1 FROM recipe_ingredients ri " +
                        " WHERE ri.recipe_id = r.id " +
                        " AND NOT EXISTS (" +
                        "   SELECT 1 FROM ingredients p " +
                        "   WHERE LOWER(TRIM(p.name)) = LOWER(TRIM(ri.ingredient_name)) " +
                        "   AND LOWER(TRIM(p.unit)) = LOWER(TRIM(ri.unit)) " +
                        "   GROUP BY LOWER(TRIM(p.name)), LOWER(TRIM(p.unit)) " +
                        "   HAVING SUM(p.quantity) >= (" +
                        "       SELECT SUM(ri2.quantity) " +
                        "       FROM recipe_ingredients ri2 " +
                        "       WHERE ri2.recipe_id = r.id " +
                        "       AND LOWER(TRIM(ri2.ingredient_name)) = LOWER(TRIM(ri.ingredient_name)) " +
                        "       AND LOWER(TRIM(ri2.unit)) = LOWER(TRIM(ri.unit))" +
                        "   )" +
                        " )" +
                        ") ORDER BY r.name";

        return db.rawQuery(query, null);
    }
}