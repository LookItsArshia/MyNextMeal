package com.example.mynextmeal;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Recipe extends ArrayList<String> {
    String title;
    String image;
    List<String> ingredients;

    public Recipe(String title, String image,List<String> ingredients){
        this.title= title;
        this.image=image;
        this.ingredients = ingredients;
    }

    public void addIngredients(String ingred){
        this.ingredients.add(ingred);
    }

    @NonNull
    @Override
    public Stream<String> stream() {
        return null;
    }
}
