package ch.ost.rj.mge.testat2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LocationDetailActivity extends AppCompatActivity {

    ContentDBHelper dbHelper;
    int id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        id = this.getIntent().getIntExtra("id", 1);

        dbHelper = new ContentDBHelper(this);
        Log.d(null, "id = " + id);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "entry",
                new String[]{ "_id", "name", "ort", "isFavorite", "levelMin", "levelMax" },
                "_id = " + id,
                null,
                null,
                null,
                "_id ASC");
        TextView name = findViewById(R.id.locationName);
        TextView ort = findViewById(R.id.locationOrtValue);
        TextView level = findViewById(R.id.locationLevelValue);
        while(cursor.moveToNext()){
            Log.d(null, "name = " + cursor.getString(1));
            name.setText(cursor.getString(1));
            ort.setText(cursor.getString(2));
            String s = cursor.getString(4) + " - " + cursor.getString(5);
            level.setText(s);
        }


    }

}