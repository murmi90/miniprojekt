package ch.ost.rj.mge.testat2;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    ContentDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //to be continued...

        switch(item.getItemId()){
            case R.id.menuSettings:

                break;

            case R.id.menuAbout:

                break;


        }
        return true;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void createDB(){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        values.put("name", "Bärenfels");
        values.put("ort", "Basel");
        values.put("isFavorite", 0);
        values.put("levelMin", 3);
        values.put("levelMax", 7);
        values.put("longitude", 7.37037);
        values.put("latitude", 47.27431);
        db.insert("entry", null, values);
        values.put("name", "Pelzli");
        values.put("ort", "Basel");
        values.put("isFavorite", 0);
        values.put("levelMin", 4);
        values.put("levelMax", 9);
        values.put("longitude", 7.36487);
        values.put("latitude", 47.26137);
        db.insert("entry", null, values);
        values.put("name", "Ponte Brolla");
        values.put("ort", "Tessin");
        values.put("isFavorite", 0);
        values.put("levelMin", 4);
        values.put("levelMax", 8);
        values.put("longitude", 8.45051);
        values.put("latitude", 46.11197);
        db.insert("entry", null, values);
        values.put("name", "Maloja");
        values.put("ort", "Graubünden");
        values.put("isFavorite", 0);
        values.put("levelMin", 5);
        values.put("levelMax", 7);
        values.put("longitude", 9.42060);
        values.put("latitude", 46.24113);
        db.insert("entry", null, values);
        values.put("name", "Bramois");
        values.put("ort", "Wallis");
        values.put("isFavorite", 0);
        values.put("levelMin", 4);
        values.put("levelMax", 7);
        values.put("longitude", 7.24235);
        values.put("latitude", 46.13262);
        db.insert("entry", null, values);
        db.close();
    }

    private void readDB(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "entry",
                new String[]{ "_id", "name", "ort", "isFavorite"},
                null,
                null,
                null,
                null,
                "_id ASC");
        while(cursor.moveToNext()) {
            Log.d(null, cursor.getInt(0) + " | " + cursor.getString(1) + " | " + cursor.getString(2) + " | " + cursor.getInt(3));
        }
        cursor.close();
        db.close();
    }

    public boolean firstAppStart(){
        boolean first = false;
        SharedPreferences sharedPreferences = getSharedPreferences("firstStart", MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        if (!sharedPreferences.getBoolean("firstStart", false)){
            first = true;
            sharedPreferencesEditor.putBoolean("firstStart", true);
            sharedPreferencesEditor.commit();
        }
        return first;
    }

}