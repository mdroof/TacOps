package edu.svsu.tacops.tacops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        //---- https://www.bignerdranch.com/blog/splash-screens-the-right-way/
        */

        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, Authentication.class);
        startActivity(intent);
        finish();
    }
}
