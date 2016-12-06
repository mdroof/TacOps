package edu.svsu.tacops.tacops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

    }

    public void createGameIntent(View v){
        Intent intent = new Intent(v.getContext(), GameSettings.class);
        startActivity(intent);
    }

    public void joinGameIntent(View v){
        Intent intent = new Intent(v.getContext(), GameSettings.class);
        startActivity(intent);
    }

    public void settingsIntent(View v){
        Intent intent = new Intent(v.getContext(), GameSettings.class);
        startActivity(intent);
    }
}
