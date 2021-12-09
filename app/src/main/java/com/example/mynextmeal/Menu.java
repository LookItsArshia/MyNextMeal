package com.example.mynextmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {

    private ListView mListView;
    private MenuAdapter menuAdapter;
    private ArrayList<String> mArrayList;
    private Button submitBtn;
    private List<Recipe> recipes;
    private List<String> newRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
//        SpoonacularAPI getRecipes = new SpoonacularAPI();
//        getRecipes()
        mArrayList = new ArrayList<String>();
        submitBtn = findViewById(R.id.checkBtn);

        mListView = findViewById(R.id.mListView);

        menuAdapter = new MenuAdapter(this);

        newRecipe = getIntent().getStringArrayListExtra("recipes");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                newRecipe );
        mListView.setAdapter(arrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WebView mWebview = new WebView(Menu.this);
                setContentView(mWebview);
                Log.i("web", "Loading " + newRecipe.get(position));
                mWebview.loadUrl("http://www.google.com/search?q="+newRecipe.get(position));
//                Intent m = new Intent(Menu.this, Browser.class);
//                m.putExtra("item",newRecipe.get(position));
//                startActivity(m);
            }
        });



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArrayList.add("Sample Recipe Name " + String.valueOf(mArrayList.size()));
                menuAdapter.add("Sample Recipe Name " + String.valueOf(mArrayList.size()));

            }
        });

    }


    private class MenuAdapter extends ArrayAdapter<String> {
        public MenuAdapter(Context ctx){
            super(ctx, 0);
        }

        public String getRecipe(int pos){

            return mArrayList.get(pos);
        }
        @Override
        public View getView(int pos, View convertView, ViewGroup parent){
            LayoutInflater inflater = Menu.this.getLayoutInflater();
            View output;


            output = inflater.inflate(R.layout.menu_recipe, null);
            TextView recipeName = output.findViewById(R.id.menu_recipe_title);
            recipeName.setText(getRecipe(pos));
            return output;
        }
    }
}