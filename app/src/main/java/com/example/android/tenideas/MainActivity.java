package com.example.android.tenideas;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.tenideas.data.DatabaseHandler;
import com.example.android.tenideas.model.DateEntry;
import com.example.android.tenideas.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<DateEntry> dateEntryList;
    private DatabaseHandler databaseHandler;
    private RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = createEntry();
                if (id > 0)
                    openIdeas((int) id);
            }
        });

        databaseHandler = new DatabaseHandler(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //databaseHandler.deleteAllDateEntry();
        dateEntryList = new ArrayList<>();
        dateEntryList = databaseHandler.getAllDates();

        recyclerViewAdapter = new RecyclerViewAdapter(this, dateEntryList, databaseHandler);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dateEntryList = databaseHandler.getAllDates();
        recyclerViewAdapter = new RecyclerViewAdapter(this, dateEntryList, databaseHandler);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void openIdeas(int id) {
        Intent intent = new Intent(this, TenIdeasActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    //Display favourited items
    private void openFavourites() {
        if (databaseHandler.getAllIdeasWhereFavourite().size() > 0) {
            Intent intent = new Intent(this, TenIdeasActivity.class);
            intent.putExtra("favourites", true);
            startActivity(intent);
        } else {
            Toast.makeText(this, "You have no favourites", Toast.LENGTH_LONG).show();

        }
    }

    //Create new ten ideas list
    private long createEntry() {
        boolean found = false;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String formatDate = dateFormat.format(new Date(System.currentTimeMillis()).getTime());
        return databaseHandler.addDate();

        /*if (dateEntryList.size() > 0) {
            for (DateEntry dateEntry : dateEntryList) {
                if (dateEntry.getDateAdded().equals(formatDate)) {
                    Toast.makeText(this, "You can only make one entry per day", Toast.LENGTH_LONG).show();
                    found = true;
                }
            }
        }
        if (!found) {
            return databaseHandler.addDate();
        }
        return -1;*/
    }

    public void deleteEntry(int id){
        databaseHandler.deleteAllDateEntryWhere(id);
        databaseHandler.deleteDateEntry(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favourites) {
            openFavourites();
        }

        return super.onOptionsItemSelected(item);
    }
}
