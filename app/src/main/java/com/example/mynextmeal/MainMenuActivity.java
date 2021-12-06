package com.example.mynextmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ImageView backButton = (ImageView) findViewById(R.id.backButton);

        TextView title = (TextView) findViewById(R.id.toolbar_title);

        title.setText("Main Menu");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });

    }

    public void goToScan(View view) {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }

    public void goToMyMenu(View view) {
        Intent intent = new Intent(this, MyMenu.class);
        startActivity(intent);

    }


    public void goToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

}