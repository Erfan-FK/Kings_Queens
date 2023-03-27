package com.app.presidents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPresident extends AppCompatActivity implements OnDataAvailable {

    private static final String TAG = "AddPresident";
    EditText nameV, dateV, countryV;
    Button addV, exitV;
    private String name;
    private String country;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_president);

        nameV = findViewById(R.id.name);
        dateV = findViewById(R.id.year);
        countryV = findViewById(R.id.country);
        addV = findViewById(R.id.add);
        exitV = findViewById(R.id.exit);

        addV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Starts");
                name = nameV.getText().toString().trim();
                date = dateV.getText().toString().trim().isEmpty() ? "????" : dateV.getText().toString().trim();
                country = countryV.getText().toString().trim();

                if(name.isEmpty())
                    nameV.setError("Name cannot be empty!");
                if(country.isEmpty())
                    countryV.setError("Country cannot be empty!");
                else{
                    GetData getData = new GetData(AddPresident.this);
                    getData.execute(name);
                }
            }
        });

        exitV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddPresident.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onDataAvailable(DOWNLOAD_STATUS downloadStatus, String imageURI) {
        if(downloadStatus == DOWNLOAD_STATUS.SUCCEED) {
            President president = new President(name, imageURI, date, country);
            Toast.makeText(AddPresident.this, "Added.", Toast.LENGTH_LONG).show();
            DbHelper dbHelper = new DbHelper(AddPresident.this);
            dbHelper.addPresident(president);
        }
//      else
//          Download Failed.
    }
}