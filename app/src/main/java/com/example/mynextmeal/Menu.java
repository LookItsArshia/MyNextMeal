package com.example.mynextmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Menu extends AppCompatActivity {

    private ListView mListView;
    private MenuAdapter menuAdapter;
    private ArrayList<String> mArrayList;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mArrayList = new ArrayList<String>();
        submitBtn = findViewById(R.id.checkBtn);

        mListView = findViewById(R.id.mListView);

        menuAdapter = new MenuAdapter(this);
        mListView.setAdapter(menuAdapter);



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