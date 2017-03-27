package edu.svsu.tacops.tacops;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
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
    Spinner time_limit_spinner;
    Spinner score_limit_spinner;
    Spinner max_clients_spinner;
    Spinner team_quantity_spinner;
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

        description_textview = (TextView) findViewById(R.id.description_textview);
        missionDescription_textview = (TextView) findViewById(R.id.missionDescription_textview);
        done_button = (Button)findViewById(R.id.done_button);

        // Saves new game settings and transfers control to game lobby
        done_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                // Creates new game on firebase with desired settings
                Game game = createGame();
                Toast.makeText(v.getContext(), "Settings Saved",
                        Toast.LENGTH_SHORT).show();

                // Transfer control to game lobby
                Intent intent = new Intent(v.getContext(), GameLobby.class);
                intent.putExtra("game", game); // Pass game object
                startActivity(intent);

            }
        });
    } // End onCreate

    // Creates the default game settings to be added to Firebase later
    // Run once to re-create TacOps games
    private void writeGame(){
        Game game = new Game();
        Map<String, Game> missions = new HashMap<>();
        Map<String, Integer> time_limits = new HashMap<>();
        //ArrayList<Integer> timeLimitList = new ArrayList<>();
        time_limits.put("option1", 5);
        time_limits.put("option2", 10);
        time_limits.put("option3", 15);
        time_limits.put("option4", 20);
        time_limits.put("option5", 30);
        time_limits.put("option6", 60);
        mDatabase.child("time_limits").setValue(time_limits);

        //Capture the Flag
        game.setDescription("Opposing teams attempt to capture their enemies flag while defending their own flag.");
        game.setGame_id("b08cc26548aee0bff438c252deea3ff7");
        game.setName("Capture the Flag");
        game.setScore_limit(3);
        //game.setTimeLimitList(timeLimitList);
        game.setTime_limit(20.00);
        game.setMax_players(16);
        game.setTeamQuantity(2);
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
        missions.put("Domination", game2);
        mDatabase.child("missions").setValue(missions);

    } // End writeGame

    // Sets new game settings, saves to database, and returns the game
    private Game createGame() {
        Game game = new Game();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Set mission
        Spinner missionTypeSpinner = (Spinner)findViewById(R.id.mission_type_spinner);
        String missionType = missionTypeSpinner.getSelectedItem().toString();
        game.setName(missionType);

        //Set password
        EditText password = (EditText)findViewById(R.id.password_edittext);
        game.setPassword(password.getText().toString());

        //Set time_limit
        Spinner timeLimitSpinner = (Spinner)findViewById(R.id.time_limit_spinner);  // Get
        Double time_limit = (Double) timeLimitSpinner.getSelectedItem();            // Cast
        game.setTime_limit(time_limit);                                             // Set

        //Set score_limit
        Spinner scoreLimitSpinner = (Spinner)findViewById(R.id.score_limit_spinner);
        Integer score_limit = (Integer) scoreLimitSpinner.getSelectedItem();
        game.setScore_limit(score_limit);

        // Set max_players
        Spinner maxPlayersSpinner = (Spinner)findViewById(R.id.max_clients_spinner);
        Integer max_players = (Integer) maxPlayersSpinner.getSelectedItem();
        game.setMax_players(max_players);

        // Set team_quanity
        Spinner teamQuantitySpinner = (Spinner)findViewById(R.id.team_quantity_spinner);
        Integer team_quantity = (Integer) teamQuantitySpinner.getSelectedItem();
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

    //
    private void setupDataListeners(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("missions");

        mission_spinner = (Spinner)findViewById(R.id.mission_type_spinner);
        time_limit_spinner = (Spinner) findViewById(R.id.time_limit_spinner);
        score_limit_spinner = (Spinner) findViewById(R.id.score_limit_spinner);
        team_quantity_spinner = (Spinner) findViewById(R.id.team_quantity_spinner);
        max_clients_spinner = (Spinner) findViewById(R.id.max_clients_spinner);

        // Retrieve the mission types from Firebase
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> mission_types = new ArrayList<>();

                for (DataSnapshot missionSnapshot: dataSnapshot.getChildren()) {
                        String missionName = missionSnapshot.child("name").getValue(String.class);
                        Game mission = missionSnapshot.getValue(Game.class);
                        mission_types.add(missionName);
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
        //Creating and adding time limits
        List<Double> time_limits = new ArrayList<>();
        time_limits.add(5.00);
        time_limits.add(10.00);
        time_limits.add(15.00);
        time_limits.add(20.00);
        time_limits.add(30.00);
        time_limits.add(60.00);

        // Populate the time_limit_spinner with time limits
        ArrayAdapter<Double> timeAdapter = new ArrayAdapter<>(GameSettings.this, android.R.layout.simple_spinner_item, time_limits);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_limit_spinner.setAdapter(timeAdapter);

        //Creating and adding Max Clients
        List<Integer> max_clients = new ArrayList<>();
        max_clients.add(8);
        max_clients.add(16);
        max_clients.add(24);
        max_clients.add(32);

        // Populate the max_clients_spinner with max client quantites
        ArrayAdapter<Integer> clientsAdapter = new ArrayAdapter<>(GameSettings.this, android.R.layout.simple_spinner_item, max_clients);
        clientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        max_clients_spinner.setAdapter(clientsAdapter);

        //Creating and adding team quantities
        List<Integer> team_quantities = new ArrayList<>();
        team_quantities.add(2);
        team_quantities.add(3);
        team_quantities.add(4);

        // Populate the team_quantity_spinner with team quantities
        ArrayAdapter<Integer> teamAdapter = new ArrayAdapter<>(GameSettings.this, android.R.layout.simple_spinner_item, team_quantities);
        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        team_quantity_spinner.setAdapter(teamAdapter);


    } // End setupDataListeners

    // Retrieve Default settings from Firebase for selected mission
    private void retrieveMissionData(){

        //Creating and adding score limit for CTF
        final List<Integer> score_limit_ctf = new ArrayList<>();
        score_limit_ctf.add(3);
        score_limit_ctf.add(4);
        score_limit_ctf.add(5);

        //Creating and adding score limit for Domination
        final List<Integer> score_limit_domination = new ArrayList<>();
        score_limit_domination.add(300);
        score_limit_domination.add(400);
        score_limit_domination.add(500);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = mission_spinner.getSelectedItem().toString();
                Game game = dataSnapshot.child(key).getValue(Game.class);
                time_limit_spinner.setSelection(1);
                if (key.equals("Capture the Flag") ) {
                    System.out.println("Capture the Flag selected");

                    // Populate the score_limit_spinner with score limits for CTF
                    final ArrayAdapter<Integer> scoreAdapterCTF= new ArrayAdapter<>(GameSettings.this, android.R.layout.simple_spinner_item, score_limit_ctf);
                    scoreAdapterCTF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    score_limit_spinner.setAdapter(scoreAdapterCTF);
                }
                else if (key.equals("Domination")) {
                    System.out.println("Domination selected");

                    // Populate the score_limit_spinner with score limits for Domination
                    final ArrayAdapter<Integer> scoreAdapterDom= new ArrayAdapter<>(GameSettings.this, android.R.layout.simple_spinner_item, score_limit_domination);
                    scoreAdapterDom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    score_limit_spinner.setAdapter(scoreAdapterDom);
                }
                //time_limit_spinner.setText(Double.toString(game.getTime_limit()));
               //score_limit_edittext.setText(Integer.toString(game.getScore_limit()));
                max_clients_spinner.setSelection(1);
                //max_clients_edittext.setText(Integer.toString(game.getMax_players()));
                team_quantity_spinner.setSelection(0);
                //team_quantity_edittext.setText(Integer.toString(game.getTeamQuantity()));
                missionDescription_textview.setText(game.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } // End retrieveMissionData


} // End GameSettings
