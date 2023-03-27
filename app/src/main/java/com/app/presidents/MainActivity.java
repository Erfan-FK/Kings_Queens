package com.app.presidents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    RecyclerView rcV;
    RecyclerViewAdapter rcAdapter;
    RecyclerView.LayoutManager rcLayoutManager;
    FloatingActionButton fabV;
    List<President> presidentList;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper dbHelper = new DbHelper(MainActivity.this);
        presidentList = dbHelper.getPresidents();

        rcV = findViewById(R.id.rcv);
        fabV = findViewById(R.id.fab);

        rcV.setHasFixedSize(true);

        rcLayoutManager = new LinearLayoutManager(MainActivity.this);
        rcV.setLayoutManager(rcLayoutManager);

        rcAdapter = new RecyclerViewAdapter(presidentList, MainActivity.this);
        rcV.setAdapter(rcAdapter);

        fabV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddPresident.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sort_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DbHelper dbHelper = new DbHelper(MainActivity.this);

        switch (item.getItemId()){
            case R.id.nameAsc:
                presidentList = dbHelper.sort(DbHelper.Columns.NAME, "ASC");
                rcAdapter.swapData(presidentList);
                Toast.makeText(MainActivity.this, "Ascending by Name", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nameDesc:
                presidentList = dbHelper.sort(DbHelper.Columns.NAME, "DESC");
                rcAdapter.swapData(presidentList);
                Toast.makeText(MainActivity.this, "Descending by Name", Toast.LENGTH_SHORT).show();
                break;

            case R.id.dateAsc:
                presidentList = dbHelper.sort(DbHelper.Columns.DATE, "ASC");
                rcAdapter.swapData(presidentList);
                Toast.makeText(MainActivity.this, "Ascending by Date", Toast.LENGTH_SHORT).show();
                break;

            case R.id.dateDesc:
                presidentList = dbHelper.sort(DbHelper.Columns.DATE, "DESC");
                rcAdapter.swapData(presidentList);
                Toast.makeText(MainActivity.this, "Descending by Date", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}