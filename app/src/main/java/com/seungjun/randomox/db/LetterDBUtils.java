package com.seungjun.randomox.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.seungjun.randomox.utils.D;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LetterDBUtils {
    private static String TAG = "LetterDBUtils";

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

        values.put("letter_req_date", dateFormat.format(date));
        values.put("letter_req_text", text);
        values.put("letter_read", "N");

        D.log(TAG, "db Insert");

        db.insert(RandomOxDBHelper.LETTER_TB_NAME, null, values);
    }

    public void allDeleteLetter(){
        db.delete(RandomOxDBHelper.LETTER_TB_NAME, null, null);
    }


    public void updateLetterReadState(int letter_id, String read){

        String where = "_id = " + letter_id;

        ContentValues values = new ContentValues();
        values.put("letter_read", read);

        D.log(TAG, "db update");

        db.update(RandomOxDBHelper.LETTER_TB_NAME, values, where, null);


    }

    public ArrayList<LetterDBData> getAllData(){
        ArrayList<LetterDBData> list = new ArrayList<>();

        String query = "SELECT * FROM " + RandomOxDBHelper.LETTER_TB_NAME + " ORDER BY letter_req_date DESC ";

        try{
            db.beginTransaction();
            Cursor cursor = db.rawQuery(query, null);

            while (cursor.moveToNext()){
                LetterDBData data = new LetterDBData();
                data.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                data.setLetter_req_date(cursor.getString(cursor.getColumnIndex("letter_req_date")));
                data.setLetter_req_text(cursor.getString(cursor.getColumnIndex("letter_req_text")));
                data.setLetter_read(cursor.getString(cursor.getColumnIndex("letter_read")));
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
