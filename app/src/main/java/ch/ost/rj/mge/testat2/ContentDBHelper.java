package ch.ost.rj.mge.testat2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContentDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LocationDB.db";
    public ContentDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE entry (" +
                " _id INTEGER PRIMARY KEY," +
                " name TEXT," + " ort TEXT," + " isFavorite INTEGER," +
                " levelMin INTEGER," + " levelMax INTEGER," + " longitude DOUBLE," + " latitude DOUBLE)");
    }
    public void onUpgrade(SQLiteDatabase db, int old, int neww) {
// Falls nötig: Migration zu neuer Datenbank-Version
    }
    public void onDowngrade(SQLiteDatabase db, int old, int neww) {
// Falls nötig: Migration zu alter Datenbank-Version
    }
}

