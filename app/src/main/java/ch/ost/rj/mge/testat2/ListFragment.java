package ch.ost.rj.mge.testat2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Console;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends BaseListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<LocationDataStruckture> dataModel;
    ArrayList<ContentDBHelper> list;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
        View fragment = inflater.inflate(R.layout.fragment_list, container, false);

        dbHelper = new ContentDBHelper(getActivity());
        // Daten auslesen

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "entry", // Tabellenname
                new String[]{ "_id", "name", "ort", "isFavorite", "levelMin", "levelMax" }, // Spaltennamen
                null,
                null,
                null,
                null,
                "_id ASC"); // Spaltenname

        toastAddToFavorite = Toast.makeText(getActivity().getApplicationContext(), "Add to Favorite", Toast.LENGTH_SHORT);
        toastRemoveFromFavorite = Toast.makeText(getActivity().getApplicationContext(), "Removed from Favorite", Toast.LENGTH_SHORT);

        ListView listView = (ListView) fragment.findViewById(R.id.listViewForList);
        listView.setEmptyView(fragment.findViewById(R.id.emptyListElement));
        CustomCursorAdapter listAdapter = new CustomCursorAdapter(getContext(), cursor);
        // Attach cursor adapter to the ListView
        listView.setAdapter(listAdapter);
        //cursor.close();
        //db.close();

        FloatingActionButton addButton = (FloatingActionButton) fragment.findViewById(R.id.addLocation);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(null, "dubdiduh!!");
                Intent testActivityIntent = new Intent(getActivity(), AddLocationActivity.class);
                startActivity(testActivityIntent);
            }
        });

        return fragment;
    }





} //end fragment
