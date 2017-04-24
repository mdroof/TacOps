package edu.svsu.tacops.tacops;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.tacops.Client;
import com.tacops.Game;

import static java.security.AccessController.getContext;

public final class JoinGameDialog {

    private static volatile JoinGameDialog instance;
    private View view;
    Game game;

    private String gameName;
    private String password;
    private String gameId;
    Activity activity;

    public JoinGame joinGame;
    public Context context;
    public Activity joinGameActivity;

    private JoinGameDialog(){
        joinGame = new JoinGame();
        game = new Game();


    }

    public void createDialogBox(){

        LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
        View promptView = layoutInflater.inflate(R.layout.join_game_dialog,null);
        final AlertDialog builder = new AlertDialog.Builder(view.getContext()).create();

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        Button joinBtn = (Button) promptView.findViewById(R.id.dialog_ok);
        Button cancelBtn = (Button) promptView.findViewById(R.id.dialog_cancel);


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPassword(editText.getText().toString()) == true){
                    //Add player to game and take to lobby.
                    Intent gameLobbyIntent = new Intent(v.getContext(), GameLobby.class);
                    game.setTeamQuantity(2);
                    gameLobbyIntent.putExtra("game_uid", gameId);
                    gameLobbyIntent.putExtra("game", game); // Pass game object
                    v.getContext().startActivity(gameLobbyIntent);
                    //joinGame.startActivity(new Intent(context, GameLobby.class));
/*                    // Getting current Client's data
                    Client player = getProviderData();

                    Game game = new Game();
                    game.setGame_id(gameId);
                    System.out.println("Game Id: " + gameId);
                    Intent intent = new Intent(v.getContext(), GameLobby.class);
                    intent.putExtra("game", game); // Pass game object
                    intent.putExtra("player", player); // Pass player object
                    v.getContext().startActivity(intent);*/
                }
                else{
                    editText.setError("Incorrect Password");
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        builder.setView(promptView);

        TextView title = new TextView(view.getContext());
        title.setText(gameName + " - "+ gameId.substring(gameId.length() - 4));

        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(22);
        title.setTextColor(Color.BLACK);
        title.setTypeface(null, Typeface.BOLD);

        builder.setCustomTitle(title);
        builder.show();
    }


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

    public static JoinGameDialog getInstance() {
        if (instance == null) {
            synchronized (JoinGameDialog.class) {
                if (instance == null) {
                    instance = new JoinGameDialog();
                }
            }
        }
        return instance;
    }

    public boolean checkPassword(String enteredPassword){

        if(this.password.equals(enteredPassword))
            return true;
        else
            return false;
    }

    public void setView(View view) {this.view = view;}

    public void setPassword(String password) {this.password = password;}

    public void setGameId(String gameId) { this.gameId = gameId;}

    public void setGameName(String gameName) {this.gameName = gameName;}

    public void setGame(Game game) {this.game = game;}



}
