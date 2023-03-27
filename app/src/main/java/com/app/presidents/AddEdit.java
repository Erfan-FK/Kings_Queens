package com.app.presidents;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class AddEdit extends AppCompatActivity implements OnDataAvailable {

    ImageView imageView;
    EditText name, date, country;
    Button update, delete;
    String nameReceived;
    String newName;
    String newCountry;
    String newDate;
    AlertDialog dialog;
    boolean changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Delete");

        final Boolean[] nameChanged = {false};

        imageView = findViewById(R.id.ad_image);
        name = findViewById(R.id.ad_name);
        date = findViewById(R.id.ad_date);
        country = findViewById(R.id.ad_country);
        update = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);

        Intent intent = getIntent();
        nameReceived = intent.getStringExtra("name");

        DbHelper dbHelper = new DbHelper(AddEdit.this);
        President president = dbHelper.getOne(nameReceived);
        name.setText(president.getName());
        date.setText(president.getDate());
        country.setText(president.getCountry());
        Glide.with(AddEdit.this).load(president.getImageURL()).into(imageView);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//              Nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                changed = true;
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCountry = country.getText().toString();
                newDate = date.getText().toString();

                if (changed){
                    newName = name.getText().toString();
                    GetData getData = new GetData(AddEdit.this);
                    getData.execute(newName);
                }
                else{
                    Toast.makeText(AddEdit.this, "Nothing Changed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(AddEdit.this);

        View view = getLayoutInflater().inflate(R.layout.delete_dialog, null);
        view.setBackgroundResource(R.drawable.dialog_shape);

        Button yes = view.findViewById(R.id.yes);
        Button no = view.findViewById(R.id.no);

        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deletePresident(name.getText().toString());
                Toast.makeText(AddEdit.this, "Deleted.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddEdit.this, MainActivity.class));
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    @Override
    public void onDataAvailable(DOWNLOAD_STATUS downloadStatus, String imageURI) {
        if(downloadStatus == DOWNLOAD_STATUS.SUCCEED) {
            President president = new President(newName, imageURI, newDate, newCountry);
            DbHelper dbHelper = new DbHelper(AddEdit.this);
            dbHelper.updatePresident(nameReceived, president);
            Glide.with(AddEdit.this).load(imageURI).into(imageView);
            Toast.makeText(AddEdit.this, "Updated.", Toast.LENGTH_LONG).show();
        }
//      else
//          Download Failed.
    }
}