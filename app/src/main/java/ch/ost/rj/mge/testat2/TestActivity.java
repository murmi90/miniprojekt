package ch.ost.rj.mge.testat2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    ContentDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        dbHelper = new ContentDBHelper(this);
        //createDB();
        readDB();


    }

    private void createDB(){
        dbHelper = new ContentDBHelper(this);
        // Daten einfügen
        ContentValues values = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        values.put("name", "blubname1"); // Spaltenname
        values.put("ort", "blubort1");
        db.insert("entry", null, values); // Tabellenname
        values.put("name", "blubname2");
        values.put("ort", "blubort2");
        db.insert("entry", null, values);
        values.put("name", "blubname3");
        values.put("ort", "blubort3");
        db.insert("entry", null, values);
        values.put("name", "blubname4");
        values.put("ort", "blubort4");
        db.insert("entry", null, values);
        values.put("name", "blubname5");
        values.put("ort", "blubort5");
        db.insert("entry", null, values);
        db.close();
    }

    private void readDB(){
        // Daten auslesen
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "entry", // Tabellenname
                new String[]{ "_id", "name", "ort" , "isFavorite"}, // Spaltennamen
                null,
                null,
                null,
                null,
                "_id ASC"); // Spaltenname
        while(cursor.moveToNext()) {
            Log.d(null, cursor.getInt(0) + " | " + cursor.getString(1) + " | " + cursor.getString(2) + " | " + cursor.getInt(3));
        }
        cursor.close();
        db.close();
    }

    private void writeDatabase(){
        //create instanze of DB
        EntryDatabase db = Room.databaseBuilder(
                this,
                EntryDatabase.class,
                "room.db")
                .build();
        EntryDao dao = db.entryDao();
        // Daten einfügen
        Entry entry = new Entry();
        entry.id = 1;
        entry.name = "blahname1";
        entry.ort = "blahort1";
        dao.insert(entry);
        // Daten auslesen
        List<Entry> entries = dao.getEntries();
        for (Entry entry2 : entries) {
            Log.d(null, entry2.name + " | " + entry2.ort);
        }
        // Aufräumen
        db.close();
    }
}