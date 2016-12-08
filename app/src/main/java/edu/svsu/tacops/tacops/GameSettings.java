package edu.svsu.tacops.tacops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tacops.Game;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        //Set Firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //writeGame();
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
    }

    private void writeGame(){
        Game game = new Game();
        game.setDescription("Opposing teams attempt to capture their enemies while defending their own flag.");
        game.setGame_id("b08cc26548aee0bff438c252deea3ff7");
        game.setName("Capture the Flag");
        game.setScore_limit(3);
        game.setTime_limit(20.00);
        Map<String, Game> missions = new HashMap<String, Game>();
        missions.put("ctf", game);

        game.setDescription("Opposing teams attempt to capture and defend strategic locations to gain points for their team.");
        game.setGame_id("5d64d34bcc4f53b9102de956debe2702");
        game.setName("Domination");
        game.setScore_limit(300);
        game.setTime_limit(15.00);

        missions.put("dom", game);
        mDatabase.child("missions").setValue(missions);

    }
    private void setupDataListeners(){
        mission_spinner = (Spinner)findViewById(R.id.mission_type_spinner);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> mission_types = new ArrayList<String>();

                for (DataSnapshot missionSnapshot: dataSnapshot.getChildren()) {
                    for(DataSnapshot typeSnapshot: missionSnapshot.getChildren()){
                        int z = 0;
                        //String areaName = missionSnapshot.child("name").getValue(String.class);
                        String areaName = typeSnapshot.child("name").getValue(String.class);
                        mission_types.add(areaName);
                    }

                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(GameSettings.this, android.R.layout.simple_spinner_item, mission_types);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mission_spinner.setAdapter(areasAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void retrieveMissionData(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = mission_spinner.getSelectedItem().toString();
                Game game = dataSnapshot.child("missions").child("ctf").getValue(Game.class);
                time_limit_edittext.setText(Double.toString(game.getTime_limit()));
                score_limit_edittext.setText(Double.toString(game.getScore_limit()));
                max_clients_edittext.setText(Integer.toString(game.getMax_players()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
