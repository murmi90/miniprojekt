package ch.ost.rj.mge.testat2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends BaseListFragment {

    ListView listView;
    SQLiteDatabase db;
    TextView emptyList;

    private static final int CALLBACK_CODE = 1;
    String permission1 = Manifest.permission.ACCESS_COARSE_LOCATION;


    String service = Context.LOCATION_SERVICE;
    double longitude;
    double latitude;
    LocationListener locationListener;
    LocationManager mgr;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_search, container, false);
        // Inflate the layout for this fragment

        int status1 = ActivityCompat.checkSelfPermission(getActivity(), permission1);
        mgr = (LocationManager) getActivity().getSystemService(service);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                updateList();
            }
        };

        if (status1 != PackageManager.PERMISSION_GRANTED) {
            if(shouldShowRequestPermissionRationale(permission1)){

            }
            requestPermissions(new String[]{permission1}, CALLBACK_CODE);
        }else{
            mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        }

        dbHelper = new ContentDBHelper(getActivity());
        db = dbHelper.getReadableDatabase();

        toastAddToFavorite = Toast.makeText(getActivity().getApplicationContext(), "Add to Favorite", Toast.LENGTH_SHORT);
        toastRemoveFromFavorite = Toast.makeText(getActivity().getApplicationContext(), "Removed from Favorite", Toast.LENGTH_SHORT);

        listView = (ListView) fragment.findViewById(R.id.listViewForSearch);
        emptyList = (TextView)fragment.findViewById(R.id.emptyListElement);
        listView.setEmptyView(fragment.findViewById(R.id.emptyListElement));

        return fragment;
    }

    public void updateList(){
        Cursor cursor = db.query(
                "entry",
                new String[]{ "_id", "name", "ort", "isFavorite", "levelMin", "levelMax" },
                null,
                null,
                null,
                null,
                "(longitude - " + longitude + ") * (longitude - " + longitude + ") + (latitude - " + latitude + ") * (latitude - " + latitude + ") DESC");

        if(cursor.getCount() == 0){
            emptyList.setText(R.string.noEntryFound);
        }else{
            CustomCursorAdapter listAdapter = new CustomCursorAdapter(getContext(), cursor);
            listView.setAdapter(listAdapter);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results){
        super.onRequestPermissionsResult(requestCode, permissions, results);

        if(requestCode != CALLBACK_CODE){
            return;
        }

        if(results.length == 0){
            return;
        }

        if(results[0] == PackageManager.PERMISSION_GRANTED){
            updateList();
        }else{
            emptyList.setText(R.string.locationAccessDenied);
        }
    }

    @SuppressLint("NewApi")
    private void requestPermission(String permission, int requestCode) {
        if (checkPermission(permission))
            return;

        if (shouldShowRequestPermissionRationale(permission)) {
            Toast.makeText(getActivity(), "Erklärung anzeigen für: " + permission, Toast.LENGTH_SHORT).show();
        }

        requestPermissions(new String[]{ permission }, requestCode);
    }

    private boolean checkPermission(int[] grantResults) {
        if (grantResults.length == 0) {
            return false;
        } else {
            return grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }
}