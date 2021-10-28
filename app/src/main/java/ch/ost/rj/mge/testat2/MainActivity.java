package ch.ost.rj.mge.testat2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.util.Log;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ContentDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ContentDBHelper(this);
        if(firstAppStart()){
            createDB();
        }
        readDB();



        openFragment(FavoriteFragment.newInstance("",""));
;       BottomNavigationView bottom_navigation = this.findViewById(R.id.bottom_navigation_view);
        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_favorites:
                        openFragment(FavoriteFragment.newInstance("",""));
                        return true;
                    case R.id.navigation_search:
                        openFragment(SearchFragment.newInstance("",""));
                        return true;
                    case R.id.navigation_list:
                        openFragment(ListFragment.newInstance("",""));
                        return true;
                    case R.id.navigation_map:
                        openFragment(MapFragment.newInstance("",""));
                        return true;
                }
                return false;
            }
        });

    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void createDB(){
        // Daten einfügen
        ContentValues values = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        values.put("name", "blubname1"); // Spaltenname
        values.put("ort", "blubort1");
        values.put("isFavorite", 0);
        values.put("levelMin", 3);
        values.put("levelMax", 7);
        db.insert("entry", null, values); // Tabellenname
        values.put("name", "blubname2");
        values.put("ort", "blubort2");
        values.put("isFavorite", 0);
        values.put("levelMin", 5);
        values.put("levelMax", 9);
        db.insert("entry", null, values);
        values.put("name", "blubname3");
        values.put("ort", "blubort3");
        values.put("isFavorite", 0);
        values.put("levelMin", 4);
        values.put("levelMax", 8);
        db.insert("entry", null, values);
        values.put("name", "blubname4");
        values.put("ort", "blubort4");
        values.put("isFavorite", 0);
        values.put("levelMin", 5);
        values.put("levelMax", 7);
        db.insert("entry", null, values);
        values.put("name", "blubname5");
        values.put("ort", "blubort5");
        values.put("isFavorite", 0);
        values.put("levelMin", 4);
        values.put("levelMax", 7);
        db.insert("entry", null, values);
        db.close();
    }

    private void readDB(){
        // Daten auslesen
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "entry", // Tabellenname
                new String[]{ "_id", "name", "ort", "isFavorite"}, // Spaltennamen
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

    public boolean firstAppStart(){
        boolean first = false;
        SharedPreferences sharedPreferences = getSharedPreferences("firstStart", MODE_PRIVATE); //erstellt/get preference mit name und mode
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();    //zum bearbeiten der Datei
        if (sharedPreferences.getBoolean("firstStart", false) == false){    //fals noch kein wert gespeichert
            first = true;
            sharedPreferencesEditor.putBoolean("firstStart", true);
            sharedPreferencesEditor.commit();   //speichert wert effectiv
        }
        return first;
    }

}