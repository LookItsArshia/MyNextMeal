package com.example.mynextmeal;

import java.util.List;

public class Recipe {
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
}
