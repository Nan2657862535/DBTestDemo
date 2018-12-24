package com.example.nan.dbtestdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DailyDisplay extends AppCompatActivity {

    private TextView allinfo;
    private DBManager dbManager;
    private  String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_display);
        id=getIntent().getStringExtra("id");
        String title=getIntent().getStringExtra("title");
        String date=getIntent().getStringExtra("date");
        String content=getIntent().getStringExtra("content");
        allinfo=(TextView)findViewById(R.id.allinfo);
        allinfo.setText(title+"\n"+date+"\n"+content+"\n");
        dbManager=new DBManager(this);
    }

    public void returnromain(View view) {
        Intent intent = new Intent(DailyDisplay.this, MainActivity.class);
        startActivity(intent);
        DailyDisplay.this.finish();
    }


}
