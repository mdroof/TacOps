package edu.svsu.tacops.tacops;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tacops.Game;

import java.util.List;

/**
 * Created by Terry on 3/6/2017.
 */

public class JoinGameAdapter extends RecyclerView.Adapter<JoinGameAdapter.MyViewHolder> {

    private List<Game> gameList;
    private Context context;
    private  JoinGameDialog dialog;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name, capacity, game_id;
        private Button join;
        private EditText edittext;
        private JoinGameDialog  dialog;

        public MyViewHolder(View view) {

            super(view);

            name = (TextView) view.findViewById(R.id.name);
            capacity = (TextView) view.findViewById(R.id.capacity);
            game_id = (TextView) view.findViewById(R.id.game_id);
            join = (Button) view.findViewById(R.id.joinbtn);

            edittext = new EditText(view.getContext());

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            edittext.setLayoutParams(lp);

            view.setOnClickListener(this);
            join.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            String filler;

            if(v.getId() == join.getId()){

                //Once a Join button is clicked get the position and put the value in the
                //gamePosition variable
                String gamePosition = String.valueOf(getAdapterPosition());

                //Using gamePosition, check the gameList to check if the game that was clicked
                //has a password. If the game has a password, show the dialog box. Else, immediately
                //take to the game lobby.
                if(gameList.get((Integer.parseInt(gamePosition))).getPassword().equals(""))
                    //filler is literally filler. The filler = "Filler" line can be deleted and replaced
                    //with a line that takes the user to the game lobby screen.
                    filler = "Filler";
                else {

                    //Checks if an instance of JoinGameDialog is created and get the instance if
                    //it is. If not, create the object.
                    dialog = dialog.getInstance();

                    dialog.setView(v);

                    dialog.setGameName(gameList.get((Integer.parseInt(gamePosition))).getName());
                    dialog.setGameId(gameList.get((Integer.parseInt(gamePosition))).getGame_id());
                    dialog.setPassword(gameList.get((Integer.parseInt(gamePosition))).getPassword());

                    dialog.createDialogBox();

                }
            }
        }

    }

    public JoinGameAdapter(List<Game> gamesList) {
        this.gameList = gamesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.join_game_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.name.setText(game.getName());
        holder.capacity.setText(" 0/" + Integer.toString(game.getMax_players()));
        holder.game_id.setText(game.getGame_id());
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }
}
