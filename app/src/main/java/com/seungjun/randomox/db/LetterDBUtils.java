package com.seungjun.randomox.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LetterDBUtils {

    private static LetterDBUtils instance;
    private static SQLiteDatabase db;


    public static LetterDBUtils getInstance(Context context){

        if(instance == null){
            instance = new LetterDBUtils();
            RandomOxDBHelper dbHelper = new RandomOxDBHelper(context);
            db = dbHelper.getWritableDatabase();
        }


        return instance;
    }


    public void saveLetterData(String text){
        ContentValues values = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        values.put("_id", 0);
        values.put("letter_req_date", dateFormat.format(date));
        values.put("letter_req_text", text);

        db.insert(RandomOxDBHelper.LETTER_TB_NAME, null, values);
    }

    public void allDeleteLetter(){
        db.delete(RandomOxDBHelper.LETTER_TB_NAME, null, null);
    }


    public ArrayList<LetterDBData> getAllData(){
        ArrayList<LetterDBData> list = new ArrayList<>();

        String query = "SELECT * FROM " + RandomOxDBHelper.LETTER_TB_NAME;

        try{
            db.beginTransaction();
            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()){
                LetterDBData data = new LetterDBData();
                data.setLetter_req_date(cursor.getString(cursor.getColumnIndex("letter_req_date")));
                data.setLetter_req_text(cursor.getString(cursor.getColumnIndex("letter_req_text")));
                list.add(data);
            }

            db.setTransactionSuccessful();

        }catch (Exception e){
            e.printStackTrace();

        } finally {
            db.endTransaction();
        }

        return list;
    }
}