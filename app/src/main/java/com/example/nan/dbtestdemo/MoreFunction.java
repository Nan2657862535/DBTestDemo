package com.example.nan.dbtestdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MoreFunction extends Activity {
    private DBManager dbManager;
private String id;
private EditText title1;
    private EditText content1;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_function);
        id=getIntent().getStringExtra("id");
        title=getIntent().getStringExtra("title");
        String date=getIntent().getStringExtra("date");
        String content=getIntent().getStringExtra("content");
        title1=(EditText)findViewById(R.id.title1);
        content1=(EditText)findViewById(R.id.content1);
        title1.setText(title);
        content1.setText(content);
        dbManager=new DBManager(this);
    }
    public void del(View view) {
        dbManager.deleteOldPerson(title);
        Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MoreFunction.this, MainActivity.class);
        startActivity(intent);
        MoreFunction.this.finish();
    }

    public void save(View view) {
        dbManager.updateAge(title1.getText().toString(),content1.getText().toString());
        Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MoreFunction.this, MainActivity.class);
        startActivity(intent);
        MoreFunction.this.finish();
    }
}
