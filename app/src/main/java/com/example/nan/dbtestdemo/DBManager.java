package com.example.nan.dbtestdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context)
    {
        Log.d(AppConstants.LOG_TAG, "com.example.nan.dbtestdemo.DBManager --> Constructor");
        helper = new DatabaseHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }


    public void adddaily(Daily daily)
    {
        Log.d(AppConstants.LOG_TAG, "com.example.nan.dbtestdemo.DBManager --> add");
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try
        {
            db.execSQL("INSERT INTO " + DatabaseHelper.DAILY_LIST
                    + " VALUES(null, ?, ?, ?,?)", new Object[] { daily.getName(),daily.getTitle(),
                    daily.getContents(), daily.getDate() });
            Log.i("info","插入成功");
            db.setTransactionSuccessful(); // 设置事务成功完成
        }
        finally
        {
            db.endTransaction(); // 结束事务
        }
    }

    public void add(List<Person> persons)
    {
        Log.d(AppConstants.LOG_TAG, "com.example.nan.dbtestdemo.DBManager --> add");
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try
        {
            for (Person person : persons)
            {
                db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
                        + " VALUES(null, ?, ?, ?)", new Object[] { person.name,
                        person.age, person.info });
                // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
                // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
                // 使用占位符有效区分了这种情况
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        }
        finally
        {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * update person's age
     *
     * @param person
     */
    public void updateAge(String title,String content)
    {
        Log.d(AppConstants.LOG_TAG, "com.example.nan.dbtestdemo.DBManager --> updateAge");
        ContentValues cv = new ContentValues();
        cv.put("content", content);
        db.update(DatabaseHelper.DAILY_LIST, cv, "title = ?",
                new String[] { title });
    }

    /**
     * delete old person
     *
     *
     */
    public void deleteOldPerson(String title)
    {
        Log.d(AppConstants.LOG_TAG, "com.example.nan.dbtestdemo.DBManager --> deleteOldPerson");
        db.delete(DatabaseHelper.DAILY_LIST, "title= ?",
                new String[] { title });
    }

    /**
     * query all persons, return list
     *
     * @return List<com.example.nan.dbtestdemo.Person>
     */
    public List<Person> query()
    {
        Log.d(AppConstants.LOG_TAG, "com.example.nan.dbtestdemo.DBManager --> query");
        ArrayList<Person> persons = new ArrayList<Person>();
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
            Person person = new Person();
            person._id = c.getInt(c.getColumnIndex("_id"));
            person.name = c.getString(c.getColumnIndex("name"));
            person.age = c.getInt(c.getColumnIndex("age"));
            person.info = c.getString(c.getColumnIndex("info"));
            persons.add(person);
        }
        c.close();
        return persons;
    }


    public List<Daily> querydaily()
    {
        Log.d(AppConstants.LOG_TAG, "com.example.nan.dbtestdemo.DBManager --> query");
        ArrayList<Daily> dailies = new ArrayList<Daily>();
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
            Daily daily = new Daily();
            daily.setId(c.getInt(c.getColumnIndex("_id")));
            daily.setName(c.getString(c.getColumnIndex("name")));
            daily.setTitle(c.getString(c.getColumnIndex("title")));
            daily.setContents(c.getString(c.getColumnIndex("content")));
            daily.setDate(c.getString(c.getColumnIndex("date")));
            dailies.add(daily);
        }
        c.close();
        return dailies;
    }


    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor()
    {
        Log.d(AppConstants.LOG_TAG, "com.example.nan.dbtestdemo.DBManager --> queryTheCursor");
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.DAILY_LIST,
                null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB()
    {
        Log.d(AppConstants.LOG_TAG, "com.example.nan.dbtestdemo.DBManager --> closeDB");
        // 释放数据库资源
        db.close();
    }


}
