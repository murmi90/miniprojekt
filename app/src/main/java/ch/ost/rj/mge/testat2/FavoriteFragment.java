package ch.ost.rj.mge.testat2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ContentDBHelper dbHelper;
    Toast toastAddToFavorite;
    Toast toastRemoveFromFavorite;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        toastAddToFavorite = Toast.makeText(getActivity().getApplicationContext(), "Add to Favorite", Toast.LENGTH_SHORT);
        toastRemoveFromFavorite = Toast.makeText(getActivity().getApplicationContext(), "Removed from Favorite", Toast.LENGTH_SHORT);

        dbHelper = new ContentDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "entry", // Tabellenname
                new String[]{ "_id", "name", "ort", "isFavorite" }, // Spaltennamen
                "isFavorite = 1",
                null,
                null,
                null,
                "_id ASC"); // Spaltenname
        ListView listView = (ListView) view.findViewById(R.id.listViewForFavorites);
        CustomCursorAdapter listAdapter = new CustomCursorAdapter(getContext(), cursor);
        // Attach cursor adapter to the ListView
        listView.setAdapter(listAdapter);

        return view;
    }

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
            // Find fields to populate in inflated template
            TextView listName = (TextView) view.findViewById(R.id.list_item_name);
            TextView listOrt = (TextView) view.findViewById(R.id.list_item_ort);
            // Extract properties from cursor
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String ort = cursor.getString(cursor.getColumnIndexOrThrow("ort"));
            int isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite"));
            // Populate fields with extracted properties
            listName.setText(name);
            listOrt.setText(ort);

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
        }
    }

    public void updateIsFavorite(int id, ImageButton addToFavoriteButton){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(
                "entry", // Tabellenname
                new String[]{ "_id", "name", "ort", "isFavorite"}, // Spaltennamen
                "_id = " +id,
                null,
                null,
                null,
                "_id ASC"); // Spaltenname
        int isFavorite = 0;
        while(cursor.moveToNext()){
            isFavorite = cursor.getInt(3);
            Log.d(null, "" + isFavorite);
            Log.d(null, cursor.getInt(0) + " | " + cursor.getString(1) + " | " + cursor.getInt(3));
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