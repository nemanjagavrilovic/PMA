package com.projekat.pma;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.projekat.pma.model.Notification;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "notifications";

    private static final String COL1 = "title";
    private static final String COL2 = "text";
    private static final String COL3 = "identificator";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable ="CREATE TABLE "+ TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL1 + " Text,"+ COL2+"  TEXT,"+ COL3+"  TEXT )";
        db.execSQL(createTable);

        ContentValues values = new ContentValues();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Notification> getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Notification> notificationList = new ArrayList<Notification>();
        if ( cursor.getCount() > 0 ) {
            while (cursor.moveToNext()) {
                Notification notification = new Notification();
                notification.setTitle(cursor.getString(1));
                notification.setText(cursor.getString(2));
                notification.setIdentificator(cursor.getLong(3));
                notificationList.add(notification);
            }
        }
        cursor.close();
        db.close();
        return  notificationList;
    }


    public Notification getNotificationByIdentificator(Long identifcator){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE identificator=" + identifcator;
        Cursor cursor = db.rawQuery(query, null);

        Notification notification = new Notification();
        if ( cursor.getCount() > 0 ) {
            while (cursor.moveToNext()) {

                notification.setTitle(cursor.getString(1));
                notification.setText(cursor.getString(2));
                notification.setIdentificator(cursor.getLong(3));
            }
        }
        cursor.close();
        db.close();

        return notification;

    }
    public boolean addData(Notification item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL1, item.getTitle());
        values.put(COL2, item.getText());
        values.put(COL3, item.getIdentificator());
        long result = db.insert(TABLE_NAME, null, values);

        if ( result == -1 ) {
            return false;
        } else {
            return true;
        }
    }
}
