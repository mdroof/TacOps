package edu.svsu.tacops.tacops;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.TypedValue;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.graphics.Color;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tacops.Game;

import java.util.Timer;
import java.util.TimerTask;

public class GameScreen extends AppCompatActivity {

    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    int counter1 = 0;
    int counter2 = 0;
    int counter3 = 0;
    int counter4 = 0;

    Button buttonStartTime;                 // clicking this button will enable the score listeners
    Button buttonPauseTime;                 // Yet to be implemented
    Button buttonEndMission;                // Yet to be implemented
    TextView textViewShowTime;              // will show the time
    CountDownTimer countDownTimer;          // built in android class CountDownTimer
    long totalTimeCountInMilliseconds;      // total count down time in milliseconds
    long timeBlinkInMilliseconds;           // start time of start blinking
    boolean blink = false;                  // controls the blinking .. on and off

    private DatabaseReference mDatabase;    //Firebase Reference var
    private AlertManager alert_manager;
    private String game_uid;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        // Getting the current game passed from GameSettings
        Intent intent = getIntent();
        Game currentGame = (Game)intent.getSerializableExtra("game");
        game_uid = (String)intent.getSerializableExtra("game_uid");     //Setting the Game_uid for Firebase Reference

        alert_manager = AlertManager.getInstance(game_uid);             //Making new AlertManager global instance

        //Set Firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference("game_list/" + game_uid);

        getReferenceOfViews();
        timerEvent(currentGame);// Starts and handles the timer
        textViewShowTime.setTextAppearance(getApplicationContext(), R.style.normalText);
        tv1.setTextAppearance(getApplicationContext(), R.style.normalText);
        tv2.setTextAppearance(getApplicationContext(), R.style.normalText);
        tv3.setTextAppearance(getApplicationContext(), R.style.normalText);
        tv4.setTextAppearance(getApplicationContext(), R.style.normalText);
        // change font size of the text
//        textViewShowTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        setActionListeners(currentGame);
    }

    private void setActionListeners(Game currentGame) {
        final int scoreLimit = currentGame.getScore_limit();
        // Button listeners are enabled when the start(pause) button is pressed
        buttonStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //—set on click listeners on the buttons—–
                tv1.setOnClickListener(this); // team1 +1
                tv2.setOnClickListener(this); // team2 +1
                tv3.setOnClickListener(this); // team3 +1
                tv4.setOnClickListener(this); // team4 +1

                if (view == tv1) {
                    counter1++;
                    tv1.setText(Integer.toString(counter1));
                    //scoreText.setBackgroundColor(Color.RED);
                    alert_manager.sendAlert("Team 1", "Scored a point!");
                }
                if (view == tv2) {
                    counter2++;
                    tv2.setText(Integer.toString(counter2));
                    //scoreText.setBackgroundColor(Color.BLUE);
                    alert_manager.sendAlert("Team 2", "Scored a point!");
                }

                if (view == tv3) {
                    counter3++;
                    tv3.setText(Integer.toString(counter3));
                    //scoreText.setBackgroundColor(Color.GREEN);
                    alert_manager.sendAlert("Team 3", "Scored a point!");
                }
                if (view == tv4) {
                    counter4++;
                    tv4.setText(Integer.toString(counter4));
                    //scoreText1.setBackgroundColor(Color.Yellow);
                    alert_manager.sendAlert("Team 4", "Scored a point!");
                }

                //End Game if score limit reached
                if (counter1 == scoreLimit) {
                    //End game
                    tv1.setText("WIN");
                    System.out.println("Score Limit Reached");
                }else if ((counter2 == scoreLimit) ){
                    //End game
                    tv2.setText("WIN");
                    System.out.println("Score Limit Reached");
                }else if ((counter3 == scoreLimit) ){
                    //End Game
                    tv3.setText("WIN");
                    System.out.println("Score Limit Reached");
                }else if ((counter4 == scoreLimit) ){
                    //End Game
                    tv4.setText("WIN");
                    System.out.println("Score Limit Reached");
                }
            }
        });
    }

    private void OnHold(){
        // Code for onHold
        int delay = 5000; // delay for 5 sec.
        int period = 1000; // repeat every sec.
        int count = 0;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                // Your code
                //count++;
            }
        }, delay, period);
    }

 /*   private void EndGameListener(){
        buttonEndMission.setOnClickListener(new View.OnClickListener(){

            @Override
            public void OnClick(View view){
                System.out.println();
            }
        });    }*/



    /**
     * Runs the timer as soon as the GameScreen is created
     */
    public void timerEvent(Game currentGame){
        Double timeLimit = currentGame.getTime_limit(); // Gets time limit set from game settings
        textViewShowTime.setText(timeLimit.toString()); // Sets timer text to Game's time attribute

        totalTimeCountInMilliseconds = timeLimit.longValue() * 60000;
        timeBlinkInMilliseconds = 1 * 60000;
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 500) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;

                if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {
                    textViewShowTime.setTextAppearance(getApplicationContext(), R.style.blinkText);
                    // change the style of the textview .. giving a red alert style

                    if (blink) {
                        textViewShowTime.setVisibility(View.VISIBLE);
                        // if blink is true, textview will be visible
                    } else {
                        textViewShowTime.setVisibility(View.VISIBLE);
                    }

                    blink = !blink;         // toggle the value of blink
                }

                textViewShowTime.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));
                // format the textview to show the easily readable format
            }

            @Override
            public void onFinish() {
                // this function will be called when the timeCount is finished
                textViewShowTime.setText("Times up!");
                textViewShowTime.setVisibility(View.VISIBLE);
            }

        }.start();
    }

    private void getReferenceOfViews() {
        textViewShowTime = (TextView) findViewById(R.id.tvTimeCount);
        tv1 = (TextView)findViewById(R.id.tvScoreCount1);
        tv2 = (TextView) findViewById(R.id.tvScoreCount2);
        tv3 = (TextView) findViewById(R.id.tvScoreCount3);
        tv4 = (TextView) findViewById(R.id.tvScoreCount4);
        buttonStartTime = (Button) findViewById(R.id.btnStart);
        buttonPauseTime = (Button) findViewById(R.id.btnPause);
        buttonEndMission = (Button) findViewById(R.id.btnEnd);
    }
} // End GameScreen
