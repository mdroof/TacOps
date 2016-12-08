package edu.svsu.tacops.tacops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    Button create_game_button;
    Button join_game_button;
    Button settings_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        create_game_button = (Button) findViewById(R.id.create_game_button);
        join_game_button = (Button) findViewById(R.id.join_game_button);
        settings_button = (Button) findViewById(R.id.settings_button);

        create_game_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(v.getContext(), GameSettings.class);
                startActivity(intent);
            }
        });

        join_game_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                /*
                Intent intent = new Intent(v.getContext(), GameSettings.class);
                startActivity(intent);
                */
            }
        });

        settings_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                /*
                Intent intent = new Intent(v.getContext(), GameSettings.class);
                startActivity(intent);
                */
            }
        });
    }

}
