package com.example.paradym;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar bar = getSupportActionBar();              // MainActivity의 타이틀바를
        bar.hide();                                         // 제거하기 위한 코드
    }

    public void onButton1Clicked(View v) {
        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        startActivityForResult(intent, REQUEST_CODE_MENU);
        //startActivity(intent);
        //finish();
    }
}
