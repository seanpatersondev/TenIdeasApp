package com.example.android.tenideas;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.tenideas.data.DatabaseHandler;
import com.example.android.tenideas.model.Idea;
import com.example.android.tenideas.ui.RecyclerViewAdapterIdea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TenIdeasActivity extends AppCompatActivity {
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ten_ideas);

        Intent intent = this.getIntent();
        int id = intent.getIntExtra("id", 0);
        boolean existing = intent.getBooleanExtra("existing", false);
        boolean favourites = intent.getBooleanExtra("favourites", false);

        databaseHandler = new DatabaseHandler(this);

        TextView textView = findViewById(R.id.date_entry);
        RecyclerView recyclerView = findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Idea> ideaList;
        if (existing) {
            ideaList = getIdeas(id);
            textView.setText(databaseHandler.getDateEntry(id).getDateAdded());
        } else if (favourites){
            textView.setText(R.string.favourites);
            ideaList = getFavourites();
        } else {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
            String formatDate = dateFormat.format(new Date(System.currentTimeMillis()).getTime());
            textView.setText(formatDate);
            ideaList = generateIdeas(id);
        }

        RecyclerViewAdapterIdea recyclerViewAdapter = new RecyclerViewAdapterIdea(this, ideaList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public void saveIdea(Idea idea) {
        databaseHandler.updateIdea(idea);
    }

    private List<Idea> getFavourites() {
        return databaseHandler.getAllIdeasWhereFavourite();
    }

    private List<Idea> getIdeas(int id) {
        return databaseHandler.getAllIdeasWhere(id);
    }

    private List<Idea> generateIdeas(int id) {
        List<Idea> ideas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Idea idea = new Idea();
            idea.setIdea("");
            idea.setFavourite(false);
            idea.setDateId(id);
            idea.setId((int) databaseHandler.addIdea(idea));
            ideas.add(idea);
        }

        return ideas;
    }
}
