package com.example.myapplication;
// import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2500;

    View first, second, third, fourth, fifth, sixth;
    TextView txt2;
    ImageView txt1;
    Animation top, bottom, middle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        middle = AnimationUtils.loadAnimation(this, R.anim.middle_animation);

        first = findViewById(R.id.first_line);
        second = findViewById(R.id.second_line);
        third = findViewById(R.id.third_line);
        fourth = findViewById(R.id.fourth_line);
        fifth = findViewById(R.id.first_line);
        sixth = findViewById(R.id.sixth_line);
        txt1 = findViewById(R.id.middle);
        txt2 = findViewById(R.id.botom);

        first.setAnimation(top);
        second.setAnimation(top);
        third.setAnimation(top);
        fourth.setAnimation(top);
        fifth.setAnimation(top);
        sixth.setAnimation(top);
        txt1.setAnimation(middle);
        txt2.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
