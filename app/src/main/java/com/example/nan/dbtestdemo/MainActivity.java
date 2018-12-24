package com.example.nan.dbtestdemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.example.nan.dbtestdemo.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DBManager dbManager;
    private ListView listView;
    private MyAdapter adapter;
    private List<Daily> dailies;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        // 初始化DBManager
        dbManager = new DBManager(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity.this,MoreFunction.class);
                intent.putExtra("id",dailies.get(i).getId());
                intent.putExtra("title",dailies.get(i).getTitle());
                intent.putExtra("date",dailies.get(i).getDate());
                intent.putExtra("content",dailies.get(i).getContents());
                startActivity(intent);
            }
        });
        querydaily();


        Intent intent3 = new Intent(this, MusicService.class);
        conn = new MyConnection();
        //使用混合的方法开启服务，
        startService(intent3);
        bindService(intent3, conn, BIND_AUTO_CREATE);

    }


    @Override
    protected void onResume() {
        super.onResume();
        querydaily();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hello__db, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(MainActivity.this,AddDaily.class);
        startActivity(intent);
        return  true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        dbManager.closeDB();// 释放数据库资源
        unbindService(conn);
    }

    public void add(View view)
    {
        ArrayList<Person> persons = new ArrayList<Person>();

        Person person1 = new Person("Ella", 22, "lively girl");
        Person person2 = new Person("Jenny", 22, "beautiful girl");
        Person person3 = new Person("Jessica", 23, "sexy girl");
        Person person4 = new Person("Kelly", 23, "hot baby");
        Person person5 = new Person("Jane", 25, "a pretty woman");

        persons.add(person1);
        persons.add(person2);
        persons.add(person3);
        persons.add(person4);
        persons.add(person5);

        dbManager.add(persons);
    }

    public void update(View view)
    {
        // 把Jane的年龄改为30（注意更改的是数据库中的值，要查询才能刷新ListView中显示的结果）
        Person person = new Person();
        person.name = "Jane";
        person.age = 30;
       // dbManager.updateAge(person);
    }

    public void delete(View view)
    {
        // 删除所有三十岁以上的人（此操作在update之后进行，Jane会被删除（因为她的年龄被改为30））
        // 同样是查询才能查看更改结果
        Person person = new Person();
        person.age = 30;
        //dbManager.deleteOldPerson(person);
    }

    public void query(View view)
    {
        List<Person> persons = dbManager.query();
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Person person : persons)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", person.name);
            map.put("info", person.age + " years old, " + person.info);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2, new String[] { "name",
                "info" }, new int[] { android.R.id.text1,
                android.R.id.text2 });
        listView.setAdapter(adapter);
    }

    public void querydaily()
    {
         dailies = dbManager.querydaily();
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Daily dailiy : dailies)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", dailiy.getName());
            map.put("title", dailiy.getTitle());
            map.put("content", dailiy.getContents());
            map.put("date", dailiy.getDate());
            list.add(map);
        }
       /* SimpleAdapter adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2, new String[] { "title",
                "content"}, new int[] { android.R.id.text1,
                android.R.id.text2 });*/
       adapter=new MyAdapter(dailies,this);
        Log.i("多少数据",""+dailies.size());
        listView.setAdapter(adapter);
    }

    @SuppressWarnings("deprecation")
    public void queryTheCursor(View view)
    {
        Cursor c = dbManager.queryTheCursor();
        startManagingCursor(c); // 托付给activity根据自己的生命周期去管理Cursor的生命周期
        CursorWrapper cursorWrapper = new CursorWrapper(c)
        {
            @Override
            public String getString(int columnIndex)
            {
                // 将简介前加上年龄
                if (getColumnName(columnIndex).equals("info"))
                {
                    int age = getInt(getColumnIndex("age"));
                    return age + " years old, " + super.getString(columnIndex);
                }
                return super.getString(columnIndex);
            }
        };
        // 确保查询结果中有"_id"列
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursorWrapper,
                new String[] { "name", "info" }, new int[] {
                android.R.id.text1, android.R.id.text2 });
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void toadddaily(View view) {
        Intent intent=new Intent(MainActivity.this,AddDaily.class);
        startActivity(intent);
    }




        private MyConnection conn;

        private MusicService.MyBinder musicControl;


        private static final int UPDATE_PROGRESS = 0;



        //使用handler定时更新进度条

        private Handler handler = new Handler() {

            @Override

            public void handleMessage(Message msg) {

                switch (msg.what) {

                    case UPDATE_PROGRESS:


                        break;

                }

            }

        };





        private class MyConnection implements ServiceConnection {
            //服务启动完成后会进入到这个方法
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //获得service中的MyBinder
                musicControl = (MusicService.MyBinder) service;
                musicControl.play();
            }



            @Override

            public void onServiceDisconnected(ComponentName name) {
            }

        }

}
