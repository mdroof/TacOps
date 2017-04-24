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

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class GameScreen extends AppCompatActivity {

    private Activity activity;

    Button tv1; // Four scoring buttons on GameScreen
    Button tv2;
    Button tv3;
    Button tv4;
    TextView v1; // Four textviews displaying team name
    TextView v2;
    TextView v3;
    TextView v4;
    int count1 = 0; // These group of counters used for Domination
    int count2 = 0;
    int count3 = 0;
    int count4 = 0;
    int counter1 = 0; // These group of counters used for CTF
    int counter2 = 0;
    int counter3 = 0;
    int counter4 = 0;
    int delay = 0; // delay for 0 sec.
    final int period = 1000; // repeat every sec.
    Timer timer1 = new Timer(); //Group of timers for Domination points
    Timer timer2 = new Timer();
    Timer timer3 = new Timer();
    Timer timer4 = new Timer();
    boolean toggle1 = false; // Group of booleans for Domination
    boolean toggle2 = false;
    boolean toggle3 = false;
    boolean toggle4 = false;

    Button buttonStartMission;              // Yet to be implemented
    Button buttonEndMission;                // Yet to be implemented
    TextView textViewShowTime;              // will show the time
    CountDownTimer countDownTimer;          // built in android class CountDownTimer
    long totalTimeCountInMilliseconds;      // total count down time in milliseconds
    long timeBlinkInMilliseconds;           // start time of start blinking
    boolean blink = false;                  // controls the blinking .. on and off

    private DatabaseReference mDatabase;    //Firebase Reference var
    private AlertManager alert_manager;
    private SoundManager sound_manager;
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
        sound_manager = SoundManager.getInstance(GameScreen.this);

        //Set Firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference("game_list/" + game_uid);

        getReferenceOfViews();
        buttonStartMission.setVisibility(View.INVISIBLE);

        // Hide buttons/views based on number of teams
        if (currentGame.getTeamQuantity() == 2)
        {
            tv3.setVisibility(View.INVISIBLE);
            v3.setVisibility(View.INVISIBLE);
            tv4.setVisibility(View.INVISIBLE);
            v4.setVisibility(View.INVISIBLE);
        }

        if (currentGame.getTeamQuantity() == 3)
        {
            tv4.setVisibility(View.INVISIBLE);
            v4.setVisibility(View.INVISIBLE);
        }

        timerEvent(currentGame);// Starts and handles the timer

        setActivity(this);

        // Changes the click listeners depending on which game mode
        if(currentGame.getName().equals("Capture the Flag")) {
            setScoreListeners(currentGame);
        } else if (currentGame.getName().equals("Domination")){
            setIncrementListeners();
        }
    }

    /**
     * Creates and deals with the score listeners on the Game Screen
     * @param currentGame
     */
    private void setScoreListeners(Game currentGame) {
        final int scoreLimit = currentGame.getScore_limit();
        tv1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                counter1++;
                tv1.setText(Integer.toString(counter1));
                //scoreText.setBackgroundColor(Color.RED);
                alert_manager.sendAlert("Team 1", "Scored a point!");
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                counter2++;
                tv2.setText(Integer.toString(counter2));
                //scoreText.setBackgroundColor(Color.RED);
                alert_manager.sendAlert("Team 2", "Scored a point!");
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                counter3++;
                tv3.setText(Integer.toString(counter3));
                //scoreText.setBackgroundColor(Color.RED);
                alert_manager.sendAlert("Team 3", "Scored a point!");
            }
        });

        tv4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                counter4++;
                tv4.setText(Integer.toString(counter4));
                //scoreText.setBackgroundColor(Color.RED);
                alert_manager.sendAlert("Team 4", "Scored a point!");
            }
        });
    }

    private void setIncrementListeners() {
        tv1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                if (toggle1) {
                        pauseThread1();
                        toggle1 = false;
                } else {
                    toggle1 = true; //Domination mode
                    scoreTimer1();
                }
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                if (toggle2) {
                    pauseThread2();
                    toggle2 = false;
                } else {
                    toggle2 = true; //Domination mode
                    scoreTimer2();
                }
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                if (toggle3) {
                    pauseThread3();
                    toggle3 = false;
                } else {
                    toggle3 = true; //Domination mode
                    scoreTimer3();
                }
            }
        });

        tv4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                if (toggle4) {
                    pauseThread4();
                    toggle4 = false;
                } else {
                    toggle4 = true; //Domination mode
                    scoreTimer4();
                }
            }
        });
    }

    // Allows the UI to update on the main thread - shows count increment on button
    private void setText(final TextView text,final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    private void scoreTimer1(){
        timer1.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                while(toggle1) {
                    try{
                        count1++; //Score every sec
                        setText(tv1, String.valueOf(count1));
                        sleep(period);
                        //if score has reached - end
                    } catch(Exception e) {e.printStackTrace();}
                }
            }
        }, delay, period);
    }

    private void scoreTimer2(){
        timer2.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                while(toggle2) {
                    try{
                        count2++; //Score every sec
                        setText(tv2, String.valueOf(count2));
                        sleep(period);
                        //if score has reached - end
                    } catch(Exception e) {e.printStackTrace();}
                }
            }
        }, delay, period);
    }

    private void scoreTimer3(){
        timer3.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                while(toggle3) {
                    try{
                        count3++; //Score every sec
                        setText(tv3, String.valueOf(count3));
                        sleep(period);
                        //if score has reached - end
                    } catch(Exception e) {e.printStackTrace();}
                }
            }
        }, delay, period);
    }

    private void scoreTimer4(){
        timer4.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                while(toggle4) {
                    try{
                        count4++; //Score every sec
                        setText(tv4, String.valueOf(count4));
                        sleep(period);
                        //if score has reached - end
                    } catch(Exception e) {e.printStackTrace();}
                }
            }
        }, delay, period);
    }

    public void pauseThread1() {
        toggle1 = false;
    }

    public void pauseThread2() {
        toggle2 = false;
    }

    public void pauseThread3() {
        toggle3 = false;
    }

    public void pauseThread4() {
        toggle4 = false;
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
        tv1 = (Button)findViewById(R.id.tvScoreCount1);
        tv2 = (Button) findViewById(R.id.tvScoreCount2);
        tv3 = (Button) findViewById(R.id.tvScoreCount3);
        tv4 = (Button) findViewById(R.id.tvScoreCount4);
        v1 = (TextView) findViewById(R.id.tvTeam1);
        v2 = (TextView) findViewById(R.id.tvTeam2);
        v3 = (TextView) findViewById(R.id.tvTeam3);
        v4 = (TextView) findViewById(R.id.tvTeam4);
        buttonStartMission = (Button) findViewById(R.id.btnStart);
        buttonEndMission = (Button) findViewById(R.id.btnEnd);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

} // End GameScreen
