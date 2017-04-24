package edu.svsu.tacops.tacops;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tacops.Alert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by YogaPad-PC on 3/21/2017.
 */

public class AlertManager {
    private DatabaseReference mDatabase;
    private List<Alert> alert_log;
    private List<String> alert_context;

    private static AlertManager instance = null;

    public static AlertManager getInstance(String game_uid){
        if(instance == null){
            instance = new AlertManager(game_uid);
        }
        return instance;
    }

    private AlertManager(String game_uid) {
        alert_log = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("game_list/" + game_uid + "/alert_log");
        mDatabase.setValue(alert_log);

        ValueEventListener alertListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                alert_log = new ArrayList<>();

                for (DataSnapshot details : dataSnapshot.getChildren()) {

                    Alert alert = new Alert(details.child("timestamp").getValue(Date.class),
                            details.child("object").getValue(String.class),
                            details.child("event").getValue(String.class));
                    alert_log.add(alert);

                }
                //joinGameAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        mDatabase.addValueEventListener(alertListener);

    }
    public void sendAlert(String object, String event){
        Alert alert = new Alert(object, event);
        alert_log.add(alert);
        mDatabase.setValue(alert_log);

    }
}
