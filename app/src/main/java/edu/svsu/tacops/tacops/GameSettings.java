package edu.svsu.tacops.tacops;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tacops.Game;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameSettings extends AppCompatActivity {

    private DatabaseReference mDatabase;

    //GUI components
    Spinner mission_spinner;
    EditText time_limit_edittext;
    EditText score_limit_edittext;
    EditText max_clients_edittext;
    EditText team_quantity_edittext;
    TextView description_textview;
    TextView missionDescription_textview;
    Button done_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        //Set Firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Uncomment to re-write the default game settings
        // writeGame();

        setupDataListeners();
        //Set Title on Activity
        //setTitle("Game Settings");
        mission_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                retrieveMissionData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        time_limit_edittext = (EditText) findViewById(R.id.time_limit_edittext);
        score_limit_edittext = (EditText) findViewById(R.id.score_limit_edittext);
        max_clients_edittext = (EditText) findViewById(R.id.max_clients_edittext);
        team_quantity_edittext = (EditText) findViewById(R.id.team_quantity_edittext);
        description_textview = (TextView) findViewById(R.id.description_textview);
        missionDescription_textview = (TextView) findViewById(R.id.missionDescription_textview);
        done_button = (Button)findViewById(R.id.done_button);

        // Saves new game settings and transfers control to game lobby
        done_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                // Creates new game on firebase with desired settings
                Game game = new Game();
                game = createGame();
                Toast.makeText(v.getContext(), "Settings Saved",
                        Toast.LENGTH_SHORT).show();

                // Transfer control to game lobby
                Intent intent = new Intent(v.getContext(), GameLobby.class);
                intent.putExtra("game", game);
                startActivity(intent);

            }
        });
    } // End onCreate

    // Creates the default game settings to be added to Firebase later
    // Run once to re-create TacOps games
    private void writeGame(){
        Game game = new Game();
        Map<String, Game> missions = new HashMap<>();

        //Capture the Flag
        game.setDescription("Opposing teams attempt to capture their enemies flag while defending their own flag.");
        game.setGame_id("b08cc26548aee0bff438c252deea3ff7");
        game.setName("Capture the Flag");
        game.setScore_limit(3);
        game.setTime_limit(20.00);
        game.setMax_players(16);
        game.setTeamQuantity(2);
        //missions.put("ctf", game);
        missions.put("Capture the Flag", game);
        mDatabase.child("missions").setValue(missions);

        Game game2 = new Game();

        // Domination
        game2.setDescription("Opposing teams attempt to capture and defend strategic locations to gain points for their team.");
        game2.setGame_id("5d64d34bcc4f53b9102de956debe2702");
        game2.setName("Domination");
        game2.setScore_limit(300);
        game2.setTime_limit(15.00);
        game2.setMax_players(16);
        game2.setTeamQuantity(2);
        //missions.put("dom", game2);
        missions.put("Domination", game2);
        mDatabase.child("missions").setValue(missions);

    } // End writeGame

    // Sets new game settings, saves to database, and returns the game
    private Game createGame() {
        Game game = new Game();

        //Set mission
        Spinner missionTypeSpinner = (Spinner)findViewById(R.id.mission_type_spinner);
        String missionType = missionTypeSpinner.getSelectedItem().toString();
        game.setName(missionType);

        //Set password
        EditText password = (EditText)findViewById(R.id.password_edittext);
        game.setPassword(password.getText().toString());

        //Set time_limit
        EditText timeLimit = (EditText)findViewById(R.id.time_limit_edittext);  // Get
        Double time_limit = Double.parseDouble(timeLimit.getText().toString()); // Cast
        game.setTime_limit(time_limit);                                         // Set

        // Set score_limit
        EditText scoreLimit = (EditText)findViewById(R.id.score_limit_edittext);
        Integer score_limit = Integer.parseInt(scoreLimit.getText().toString());
        game.setScore_limit(score_limit);

        // Set max_players
        EditText maxPlayers = (EditText)findViewById(R.id.max_clients_edittext);
        int max_players = Integer.parseInt(maxPlayers.getText().toString());
        game.setMax_players(max_players);

        // Set team_quanity
        EditText teamQuantity = (EditText)findViewById(R.id.team_quantity_edittext);
        Integer team_quantity = Integer.parseInt(teamQuantity.getText().toString());
        game.setTeamQuantity(team_quantity);

        // Setting unique game_id
        String key = mDatabase.child("game_list").push().getKey();
        game.setGame_id(key);
        String game_id = game.getGame_id();

        // Creating game and adding to game list with settings defined above
        // Does not overwrite the game_list
        Map<String, Object> games = new HashMap<>();
        games.put("/game_list/" + game_id, game);
        mDatabase.updateChildren(games);
        return game;
    }

    private void setupDataListeners(){
        mission_spinner = (Spinner)findViewById(R.id.mission_type_spinner);

        //mDatabase.addValueEventListener(new ValueEventListener() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> mission_types = new ArrayList<>();
                String missionName1 = "";

                /*for (DataSnapshot missionSnapshot: dataSnapshot.getChildren()) {
                    String missionName = missionSnapshot.child("missions").getValue(String.class);
                    mission_types.add(missionName);
                }*/

                for (DataSnapshot missionSnapshot: dataSnapshot.getChildren()) {
                    for(DataSnapshot typeSnapshot: missionSnapshot.getChildren()){
                        //int z = 0;
                         //missionName1 = missionSnapshot.child("name").getValue(String.class);
                        String missionName = typeSnapshot.child("name").getValue(String.class);
                        mission_types.add(missionName);
                       System.out.println(missionName);
                    }
                    //System.out.println(missionName1);
                }
                // Populate the mission_spinner with mission_types
                ArrayAdapter<String> missionAdapter = new ArrayAdapter<>(GameSettings.this, android.R.layout.simple_spinner_item, mission_types);
                missionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mission_spinner.setAdapter(missionAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    } // End setupDataListeners

    // Retrieve Default settings from Firebase for selected mission
    private void retrieveMissionData(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = mission_spinner.getSelectedItem().toString();
                Game game = dataSnapshot.child("missions").child(key).getValue(Game.class);
                time_limit_edittext.setText(Double.toString(game.getTime_limit()));
                time_limit_edittext.setText(Double.toString(game.getTime_limit()));
                score_limit_edittext.setText(Integer.toString(game.getScore_limit()));
                max_clients_edittext.setText(Integer.toString(game.getMax_players()));
                team_quantity_edittext.setText(Integer.toString(game.getTeamQuantity()));
                missionDescription_textview.setText(game.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } // End retrieveMissionData


} // End GameSettings
