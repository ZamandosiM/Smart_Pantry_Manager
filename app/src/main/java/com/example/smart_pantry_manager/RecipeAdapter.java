package com.example.smart_pantry_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RecipeAdapter extends ArrayAdapter<String> {

    public RecipeAdapter(Context context, ArrayList<String> recipes) {
        super(context, 0, recipes);
    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_recipe, parent, false);
        }

        TextView textRecipeName =
                convertView.findViewById(R.id.textRecipeItemName);

        String recipeName = getItem(position);
        textRecipeName.setText(recipeName);

        return convertView;
    }
}
