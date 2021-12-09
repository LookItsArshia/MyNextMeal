package com.example.mynextmeal;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpoonacularAPI extends AsyncTask<List<String>, Integer, String> {

    OkHttpClient client = new OkHttpClient();
    List<Recipe> recipes = new ArrayList<>();
    String ingredients;



    @Override
    protected String doInBackground(List<String>... strings) {

        Log.i("strings", strings.toString());
        for(int i = 0;i<strings.length;i++){
            ingredients = ingredients+strings[i];
//            Log.i("ingredients", strings[i]);
        }
//        ingredients = String.join(",", strings);
        Log.i("INGREDIENTS!!: ",ingredients);

//        String baseUrl = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=" + ingredients + "&number=10&limitLicense=true&ranking=1&ignorePantry=false&apiKey=a95b70309cfe4cb986bdea28a29a03b5";
        String url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=carrots,tomatoes&number=10&limitLicense=true&ranking=1&ignorePantry=false&apiKey=a95b70309cfe4cb986bdea28a29a03b5";
        Request request = new Request.Builder()
                .url(url)
                .build();

        Log.i("Spoon request",request.toString());
        try {
            Response response = client.newCall(request).execute();

            JSONArray jsonArr = new JSONArray(response.body().string());
            Log.i("jsonarr", jsonArr.toString());
            for(int i =0;i<jsonArr.length();i++){
                JSONObject recipe = jsonArr.getJSONObject(i);
                formatRecipe(recipe);
            }

            return "done";
        }
        catch (Exception e){
            Log.i("SpoonacularAPI","failed");
            Log.e("Spoon",e.toString());
            return "fail";
        }
    }

    public void formatRecipe(JSONObject recipe) throws JSONException {
        List<String> ingredients = new ArrayList<>();
        JSONArray usedIngredients = recipe.getJSONArray("usedIngredients");
        for(int i=0;i<usedIngredients.length();i++){
            JSONObject item = usedIngredients.getJSONObject(i);
            ingredients.add(item.getString("name"));
        }
        JSONArray missedIngredients = recipe.getJSONArray("missedIngredients");
        for(int i=0;i<missedIngredients.length();i++){
            JSONObject item = missedIngredients.getJSONObject(i);
            ingredients.add(item.getString("name"));
        }

        String title = recipe.getString("title");
        String image = recipe.getString("image");
        Log.i("title", title);
        Log.i("image", image);
        Log.i("format recipe", recipe.toString());
        Recipe newRecipe = new Recipe(title, image, ingredients);

        Log.i("creaedREcipe", newRecipe.title);
        recipes.add(newRecipe);
    }
}
