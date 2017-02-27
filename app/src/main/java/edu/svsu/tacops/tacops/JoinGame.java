package edu.svsu.tacops.tacops;
import com.tacops.Game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
;

import java.util.ArrayList;
import java.util.List;

public class JoinGame extends AppCompatActivity {


    //Gui Stuff
    TextView nameTextView;


    List<Game> missions = new ArrayList<Game>();

    String temp1;
    String desc, game_id, name, password;
    String max_players, score_limit, time_limit, teamQuantity;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;
    private ValueEventListener mgameListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        //Initializing Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("game_list");

        //Initializing TextView

        nameTextView = (TextView) findViewById(R.id.name) ;
    }

    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener gameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot details : dataSnapshot.getChildren()) {
                    temp1 = details.child("name").getValue(String.class);

                    //arraylist crashes program so I just commented it out for now
                    //and set the text
                    //missions.add(new Game(temp1));


                    nameTextView.setText(details.child("name").getValue(String.class));
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.addValueEventListener(gameListener);

    }

}