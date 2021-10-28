package ch.ost.rj.mge.testat2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class AddLocationActivity extends AppCompatActivity {

    EditText inputName;
    EditText inputOrt;
    EditText inputLevelMin;
    EditText inputLevelMax;
    Button createButton;
    String name;
    String ort;
    String minS;
    String maxS;
    int max;
    int min;
    boolean gotName;
    boolean gotOrt;
    boolean gotMin;
    boolean gotMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        gotName = gotOrt = gotMin = gotMax = false;
        inputName = findViewById(R.id.name_input);
        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = inputName.getText().toString();
                if(TextUtils.isEmpty((name))){
                    inputName.setError("Name required");
                    gotName = false;
                }else{
                    inputName.setError(null);
                    gotName = true;
                }
                updateLoginButton();
            }
        });
        inputOrt = findViewById(R.id.ort_input);
        inputOrt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ort = inputOrt.getText().toString();
                if(TextUtils.isEmpty((ort))){
                    inputOrt.setError("Location required");
                    gotOrt = false;
                }else{
                    inputOrt.setError(null);
                    gotOrt = true;
                }
                updateLoginButton();
            }
        });
        inputLevelMin = findViewById(R.id.min_input);
        inputLevelMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                minS = inputLevelMin.getText().toString();
                maxS = inputLevelMax.getText().toString();
                if(!TextUtils.isEmpty(minS)) {
                    min = Integer.parseInt(minS);
                    if(!TextUtils.isEmpty((maxS))){
                        max = Integer.parseInt(maxS);
                        if(min > 0 && min < 10 && min <= max){
                            inputLevelMin.setError(null);
                            gotMin = true;
                        }else{
                            inputLevelMin.setError("invalid input");
                            gotMin = false;
                        }
                    }else{
                        if(min > 0 && min < 10){
                            inputLevelMin.setError(null);
                            gotMin = true;
                        }else{
                            inputLevelMin.setError("invalid input");
                            gotMin = false;
                        }
                    }
                }else{
                    inputLevelMin.setError("invalid input");
                    gotMin = false;
                }
                updateLoginButton();
            }
        });
        inputLevelMax = findViewById(R.id.max_input);
        inputLevelMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                minS = inputLevelMin.getText().toString();
                maxS = inputLevelMax.getText().toString();
                if(!TextUtils.isEmpty(maxS)) {
                    max = Integer.parseInt(maxS);
                    if(!TextUtils.isEmpty((minS))){
                        min = Integer.parseInt(minS);
                        if(max > 0 && max < 10 && max >= min){
                            inputLevelMax.setError(null);
                            gotMax = true;
                        }else{
                            inputLevelMax.setError("invalid input");
                            gotMax = false;
                        }
                    }else{
                        if(max > 0 && max < 10){
                            inputLevelMax.setError(null);
                            gotMax = true;
                        }else{
                            inputLevelMax.setError("invalid input");
                            gotMax = false;
                        }
                    }
                }else{
                    inputLevelMax.setError("invalid input");
                    gotMax = false;
                }
                updateLoginButton();
            }
        });
        createButton = findViewById(R.id.create_new_entry_Button);
        createButton.setEnabled(false);
        createButton.setOnClickListener(v -> createNewEntry());

    }

    private void updateLoginButton() {
        boolean buttonIsEnabled = gotMax && gotMin && gotOrt && gotName;
        createButton.setEnabled(buttonIsEnabled);
    }

    private void createNewEntry(){
        name = inputName.getText().toString();
        ort = inputOrt.getText().toString();
        int minLevel;
        int maxLevel;
        try{
            minLevel = Integer.parseInt(inputLevelMin.getText().toString());
            maxLevel = Integer.parseInt(inputLevelMax.getText().toString());
        } catch (Exception e){
            minLevel = 1;
            maxLevel = 9;
        }
        Log.d(null, name + ort + minLevel + maxLevel);

        ContentDBHelper dbHelper = new ContentDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("ort", ort);
        values.put("isFavorite", 0);
        values.put("levelMin", minLevel);
        values.put("levelMax", maxLevel);
        db.insert("entry", null, values);

        Intent testActivityIntent = new Intent(this, MainActivity.class);
        startActivity(testActivityIntent);
    }
}