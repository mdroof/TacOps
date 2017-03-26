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

import com.tacops.Game;

public class GameScreen extends AppCompatActivity {

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    TextView textTitle;
    TextView scoreText;
    TextView textTitle1;
    TextView scoreText1;
    TextView timer; // Added timer
    int counter = 0;
    int counter1 = 0;

    Button buttonResetScore;
    Button buttonStartTime;                 // clicking this button will start time count down
    TextView textViewShowTime;              // will show the time
    CountDownTimer countDownTimer;          // built in android class CountDownTimer
    long totalTimeCountInMilliseconds;      // total count down time in milliseconds
    long timeBlinkInMilliseconds;           // start time of start blinking
    boolean blink = false;                  // controls the blinking .. on and off

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        btn1 = (Button)findViewById(R.id.addButton);
        btn2 = (Button)findViewById(R.id.subtractButton);
        btn3 = (Button)findViewById(R.id.resetButton);
        btn4 = (Button)findViewById(R.id.addButton1);
        btn5 = (Button)findViewById(R.id.subtractButton1);
        btn6 = (Button)findViewById(R.id.resetButton1);
        timer = (TextView)findViewById(R.id.tvTimeCount); // Added timer view id
        scoreText = (TextView)findViewById(R.id.scoreText);
        textTitle = (TextView)findViewById(R.id.myTextTitle);
        scoreText1 = (TextView)findViewById(R.id.scoreText1);
        textTitle1 = (TextView)findViewById(R.id.myTextTitle1);

        // change font size of the text
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        textTitle1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        timer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        scoreText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        scoreText1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        getReferenceOfViews ();
        setActionListeners ();

        // Getting the current game passed from GameSettings
        Intent intent = getIntent();
        Game currentGame = (Game)intent.getSerializableExtra("game");
        Double timeLimit = currentGame.getTime_limit();
        timer.setText(timeLimit.toString()); // Sets timer text to Game's time attribute

        totalTimeCountInMilliseconds = timeLimit.longValue() * 60000;
        timeBlinkInMilliseconds = 1 * 60000;
    }

    private void setActionListeners() {
        buttonStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textViewShowTime.setTextAppearance(getApplicationContext(), R.style.normalText);

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
                        // this function will be called when the timecount is finished
                        textViewShowTime.setText("Times up!");
                        textViewShowTime.setVisibility(View.VISIBLE);
                    }

                }.start();
            }
        });
        buttonResetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //—set on click listeners on the buttons—–
                btn1.setOnClickListener(this);
                btn2.setOnClickListener(this);
                btn3.setOnClickListener(this);
                btn4.setOnClickListener(this);
                btn5.setOnClickListener(this);
                btn6.setOnClickListener(this);

                if (view == btn1) {
                    counter++;
                    scoreText.setText(Integer.toString(counter));
                    scoreText.setBackgroundColor(Color.CYAN);
                }
                if (view == btn2) {
                    counter--;
                    scoreText.setText(Integer.toString(counter));
                    scoreText.setBackgroundColor(Color.GREEN);
                }

                if (view == btn3) {
                    counter = 0;
                    scoreText.setText(Integer.toString(counter));
                    scoreText.setBackgroundColor(Color.RED);
                }
                if (view == btn4) {
                    counter1++;
                    scoreText1.setText(Integer.toString(counter1));
                    scoreText1.setBackgroundColor(Color.CYAN);
                }
                if (view == btn5) {
                    counter1--;
                    scoreText1.setText(Integer.toString(counter1));
                    scoreText1.setBackgroundColor(Color.GREEN);
                }

                if (view == btn6) {
                    counter1 = 0;
                    scoreText1.setText(Integer.toString(counter1));
                    scoreText1.setBackgroundColor(Color.RED);
                }
            }
        });
    }

    private void getReferenceOfViews() {
        buttonResetScore = (Button) findViewById(R.id.resetButton);
        buttonStartTime = (Button) findViewById(R.id.btnStartTime);
        textViewShowTime = (TextView) findViewById(R.id.tvTimeCount);
    }
}
