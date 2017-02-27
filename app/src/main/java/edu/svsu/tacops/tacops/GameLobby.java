package edu.svsu.tacops.tacops;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class GameLobby extends AppCompatActivity {

    private ListView player_list_listView;
    private ListView team_list_listView;
    private Button start_game_button;

    // Profile data to be made into separate class later
    private String providerId ="";
    private String uid = "";
    private String name = "";
    private String email = "";
    private Uri photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        player_list_listView = (ListView) findViewById(R.id.player_list_listView);
        team_list_listView = (ListView) findViewById(R.id.team_list_listView);

        //Instanciating the player_list array with dummy data
        List<String> player_list = new ArrayList<>();
        player_list.add("Player1");
        player_list.add("Player2");

        //Instanciating the team_list array with dummy data
        List<String> team_list = new ArrayList<>();
        team_list.add("Team1");
        team_list.add("Team2");
        team_list.add("Team1");

        //Player list adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, player_list );

        player_list_listView.setAdapter(arrayAdapter);

        // Team list adapter
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, team_list );

        team_list_listView.setAdapter(arrayAdapter1);

        getProviderData();
        player_list.add(name);

        start_game_button = (Button) findViewById(R.id.start_button);

        start_game_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(v.getContext(), GameScreen.class);
                startActivity(intent);
            }
        });

    } // End onCreate

    // Gets the currently signed-in user's profile data based on provider
    private void getProviderData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                providerId = profile.getProviderId();

                // UID specific to the provider
                uid = profile.getUid();

                // Name, email address, and profile photo Url
                name = profile.getDisplayName();
                email = profile.getEmail();
                photoUrl = profile.getPhotoUrl();
            }

            }
        }
} // End GameLobby
