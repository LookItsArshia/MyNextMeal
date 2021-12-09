package com.example.mynextmeal;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Ingredients extends AppCompatActivity {
    private ListView lv;
    public List<String> your_array_list;
    Button m;
    List<String> res = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        lv = findViewById(R.id.listViewVeggies);

        m = findViewById(R.id.button);

        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecipe();
            }
        });
        your_array_list = getIntent().getStringArrayListExtra("veggie");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );

        lv.setAdapter(arrayAdapter);

    }


    void updateListView(){

    }

    void getRecipe() {
        List<String> strings = your_array_list;


        new Spoonacular().execute(strings);

        Log.i("homepage", String.valueOf(res));

        while(res == null){
            continue;
        }

        List<String> recipeee = new ArrayList<String>();
        recipeee = res;//api.titles;
        Log.i("ASDAS",res.toString());

//        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                recipeee );
//
    }



     class Spoonacular extends AsyncTask<List<String>, Integer, List<String>> {

        OkHttpClient client = new OkHttpClient();
        List<Recipe> recipes = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        String ingredients = "";



        @Override
        protected List<String> doInBackground(List<String>... strings) {

            Log.i("strings", strings.toString());
            for(int i = 0;i<strings[0].size();i++){

                if (ingredients==""){
                    ingredients= strings[0].get(i);
                }
                else{
                    ingredients = ingredients + ',' +strings[0].get(i);
                }
            }
//        Log.i("INGREDIENTS!!: ",ingredients);

            String url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=" + ingredients + "&number=10&limitLicense=true&ranking=1&ignorePantry=false&apiKey=a95b70309cfe4cb986bdea28a29a03b5";
//        String url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients=carrots,tomatoes&number=10&limitLicense=true&ranking=1&ignorePantry=false&apiKey=a95b70309cfe4cb986bdea28a29a03b5";
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

                return titles;
            }
            catch (Exception e){
                Log.i("SpoonacularAPI","failed");
                Log.e("Spoon",e.toString());
                return titles;
            }
        }


         @Override
         protected void onPostExecute(List<String> strings) {
             super.onPostExecute(strings);
             Intent m = new Intent(Ingredients.this, Menu.class);

             m.putStringArrayListExtra("recipes", (ArrayList<String>)res);
             startActivity(m);

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
            res.add(title);
            Log.i("creaedREcipe", newRecipe.title);
            recipes.add(newRecipe);
        }
    }
}