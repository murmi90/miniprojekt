package ch.ost.rj.mge.testat2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class BaseListFragment extends Fragment {

    Toast toastAddToFavorite;
    Toast toastRemoveFromFavorite;
    ContentDBHelper dbHelper;

    public class CustomCursorAdapter extends CursorAdapter {


        public CustomCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        // The bindView method is used to bind all data to a given view
        // such as setting the text on a TextView.
        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView listName = (TextView) view.findViewById(R.id.list_item_name);
            TextView listOrt = (TextView) view.findViewById(R.id.list_item_ort);
            TextView listLevel = (TextView) view.findViewById(R.id.list_item_level);

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String ort = cursor.getString(cursor.getColumnIndexOrThrow("ort"));
            int isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite"));
            int levelMin = cursor.getInt(cursor.getColumnIndexOrThrow("levelMin"));
            int levelMax = cursor.getInt(cursor.getColumnIndexOrThrow("levelMax"));
            String level = levelMin + " - " + levelMax;

            listName.setText(name);
            listOrt.setText(ort);
            listLevel.setText(level);

            ImageButton addToFavoriteButton = (ImageButton) view.findViewById(R.id.add_favourite_btn);
            if(isFavorite == 0){
                addToFavoriteButton.setImageResource(R.drawable.ic_star);
            }else{
                addToFavoriteButton.setImageResource(R.drawable.ic_star_filled);
            }
            addToFavoriteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    ContentValues values = new ContentValues();
                    updateIsFavorite(id, addToFavoriteButton);
                }
            });
            RelativeLayout item = (RelativeLayout) view.findViewById(R.id.list_item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent testActivityIntent = new Intent(getActivity(), LocationDetailActivity.class);
                    testActivityIntent.putExtra("id", id);
                    startActivity(testActivityIntent);
                }
            });
        }

    }

    public void updateIsFavorite(int id, ImageButton addToFavoriteButton){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(
                "entry",
                new String[]{ "_id", "name", "ort", "isFavorite"},
                "_id = " +id,
                null,
                null,
                null,
                "_id ASC");
        int isFavorite = 0;
        while(cursor.moveToNext()){
            isFavorite = cursor.getInt(3);
        }
        if(isFavorite == 1){
            addToFavoriteButton.setImageResource(R.drawable.ic_star);
            addToFavoriteButton.setTag(R.drawable.ic_star);
            values.put("isFavorite", 0);
            toastRemoveFromFavorite.show();
        }else{
            addToFavoriteButton.setImageResource(R.drawable.ic_star_filled);
            addToFavoriteButton.setTag(R.drawable.ic_star_filled);
            values.put("isFavorite", 1);
            toastAddToFavorite.show();
        }
        db.update("entry", values, "_id = ?", new String[]{String.valueOf(id)});
    }
}
