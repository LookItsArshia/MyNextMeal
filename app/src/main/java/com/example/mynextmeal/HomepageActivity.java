package com.example.mynextmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

public class HomepageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }


    public void scanIngredients(View view) {
        Log.i("Location:","in Scan Ingredients");
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(cameraIntent);

    }
    public void openMyMenu(View view) {
        Log.i("Location:","in openMyMenu");
        Intent menu = new Intent(this.getApplicationContext(), Menu.class);
        startActivity(menu);
    }

    public void spoonacular(View view) throws IOException {
        SpoonacularAPI api = new SpoonacularAPI();
        AsyncTask<String, Integer, String> res = api.execute();
        Log.i("homepage", String.valueOf(res));
        Log.i("Homepage", "called spoon");
    }
}