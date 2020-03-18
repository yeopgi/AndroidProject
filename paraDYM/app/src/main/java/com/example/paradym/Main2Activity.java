package com.example.paradym;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private TextView titleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ActionBar bar = getSupportActionBar();              // MainActivity의 타이틀바를
        bar.hide();


        SetTitleText();
    }

    public void SetTitleText() {
        String titleText = new String("동양미래대학교");
        titleTextView = (TextView)findViewById(R.id.textView2);
        titleTextView.setText(titleText);
        titleTextView.setTextColor(Color.RED);
    }
}
