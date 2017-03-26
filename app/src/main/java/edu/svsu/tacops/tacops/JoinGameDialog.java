package edu.svsu.tacops.tacops;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public final class JoinGameDialog {

    private static volatile JoinGameDialog instance;
    private View view;

    private String gameName;
    private String password;
    private String gameId;

    private JoinGameDialog(){

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

                if (checkPassword(editText.getText().toString()) == false){
                    editText.setError("Incorrect Password");
                }
                //else
                //Add player to game and take to lobby.

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



}
