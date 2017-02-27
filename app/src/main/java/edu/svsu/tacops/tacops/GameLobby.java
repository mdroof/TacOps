package edu.svsu.tacops.tacops;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tacops.Client;
import com.tacops.Game;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GameLobby extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView game_title_view;
    private Button start_game_button;
    static int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        //Set Firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        game_title_view = (TextView) findViewById(R.id.game_title);

        // Getting the current game passed from GameSettings
        Intent intent = getIntent();
        Game currentGame = (Game)intent.getSerializableExtra("game");
        game_title_view.setText("Game ID: " + currentGame.getGame_id());

        // Initializing array of default teams
        final ArrayList<String> teams = createTeams(currentGame);

        // Creating player list
        ArrayList<Client> player_list = new ArrayList<>(currentGame.getMax_players());
        // Adding a player to the list
        addPlayer(teams, player_list);

        final ListView lv1 = (ListView) findViewById(R.id.player_list);
        lv1.setAdapter(new CustomListAdapter(this, player_list));
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv1.getItemAtPosition(position);
                Client playerItem = (Client) o;
                // Change team
                changeTeam(playerItem, teams);
                // Update the changes
                ((BaseAdapter) lv1.getAdapter()).notifyDataSetChanged();
                System.out.println(playerItem.getTeam());
                // Get rid of toast later
                Toast.makeText(GameLobby.this, "Changed Team For:" + " " + playerItem.getClientName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        start_game_button = (Button) findViewById(R.id.start_button);

        start_game_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(v.getContext(), GameScreen.class);
                startActivity(intent);
            }
        });

    } // End onCreate

    private void changeTeam (Client player, ArrayList teamArray){
        String currentTeam = player.getTeam();
        int k = teamArray.indexOf(currentTeam); // looks for team
        if(++k >= teamArray.size())
            k = 0;
        player.setTeam(teamArray.get(k).toString());
//        System.out.println("Change Team: " + currentTeam);
//        System.out.println("Player's team: " + player.getTeam());
//        System.out.println(k);
    }

    // Creates the default number of teams
    private ArrayList createTeams (Game currentGame){
        ArrayList<String> team_array = new ArrayList<>();
        int numOfTeams = currentGame.getTeamQuantity();

        // Create teams based on number of teams
        for(int i=1; i<=numOfTeams; i++)
        {
            team_array.add("Team" + i);
        }
        return team_array;
    }

    // Add a player to the game lobby list
    private void addPlayer(ArrayList teamArray, ArrayList player_list) {
        Client client = getProviderData(); // Get the client's info
        if (index > teamArray.size())
            index = 0;
        client.setTeam(teamArray.get(index).toString()); //assign default team
        index++;
        player_list.add(client);
    }

    // Gets the currently signed-in user's profile data based on provider and return it
    private Client getProviderData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Client client = new Client();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {

                // Id of the provider (ex: google.com)
                client.setClientId(profile.getProviderId());

                // Name tied to account
                client.setClientName(profile.getDisplayName());
            }
        }
        return client;
    }

} // End GameLobby
