package com.example.nan.dbtestdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDaily extends AppCompatActivity {

    private EditText title;
    private EditText content;
    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily);
        // 初始化DBManager
        dbManager = new DBManager(this);
        title=(EditText)findViewById(R.id.title);
        content=(EditText)findViewById(R.id.content);
    }

    public void submit(View view) {
        Daily daily=new Daily();
        daily.setTitle(title.getText().toString());
        daily.setContents(content.getText().toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
       daily.setDate("日期时间:"+simpleDateFormat.format(date));
        dbManager.adddaily(daily);
        Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();

        /*Bundle bundle=new Bundle();
        bundle.putString("title",daily.getTitle());
        bundle.putString("date",daily.getDate());
        bundle.putString("content",daily.getContents());*/

        Intent intent=new Intent(AddDaily.this,DailyDisplay.class);
        intent.putExtra("title",daily.getTitle());
        intent.putExtra("date",daily.getDate());
        intent.putExtra("content",daily.getContents());
        startActivity(intent);
    }
}
