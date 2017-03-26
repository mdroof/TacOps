package edu.svsu.tacops.tacops;
import com.tacops.Game;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class JoinGame extends AppCompatActivity  {

    private Game game;


    List<Game> openGames = new ArrayList<Game>();
    public RecyclerView recyclerView;
    private JoinGameAdapter joinGameAdapter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private ValueEventListener mgameListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        //Initializing Database
        mDatabase = FirebaseDatabase.getInstance().getReference("game_list");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        joinGameAdapter = new JoinGameAdapter(openGames);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(joinGameAdapter);

        addData();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void addData(){

        ValueEventListener gameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot details : dataSnapshot.getChildren()) {
                    Game  game = new Game(details.child("name").getValue(String.class),
                            details.child("game_id").getValue(String.class),
                            details.child("max_players").getValue(Integer.class),
                            details.child("password").getValue(String.class));
                    openGames.add(game);

                }
                joinGameAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        mDatabase.addValueEventListener(gameListener);

    }

    public Context getContext(){
        return getApplicationContext();
    }

}