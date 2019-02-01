package com.seungjun.randomox.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RandomOxDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "randomox.db";
    private static final int DB_VERSION = 2;

    public static final String LETTER_TB_NAME = "letterTB";

    private Context context;

    public RandomOxDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE letterTB (");
        sb.append(" _id INTEGER PRIMARY KEY AUTOINCREMENT, "); // 인덱스
        sb.append(" letter_req_date  DATETIME, "); // 받은 날짜
        sb.append(" letter_req_text  TEXT, "); // 받은 내용
        sb.append(" letter_read  VARCHAR(10) ) "); // 받은 내용

        sqLiteDatabase.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) {
        if (newV > oldV) {
            sqLiteDatabase.execSQL("ALTER TABLE letterTB ADD COLUMN letter_read VARCHAR(10)");
        }

    }
}
