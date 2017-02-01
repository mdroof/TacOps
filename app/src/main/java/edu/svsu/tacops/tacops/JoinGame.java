package edu.svsu.tacops.tacops;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class JoinGame extends AppCompatActivity {

    private ListView game_List_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        game_List_listView = (ListView) findViewById(R.id.list_of_games);

        //Instanciating the player_list array with dummy data
        List<String> game_list = new ArrayList<>();
        game_list.add("Game1");
        game_list.add("Game2");
        game_list.add("Game3");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, game_list);

        game_List_listView.setAdapter(arrayAdapter);


    }
}
