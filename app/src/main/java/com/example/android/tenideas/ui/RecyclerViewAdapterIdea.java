package com.example.android.tenideas.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.tenideas.R;
import com.example.android.tenideas.TenIdeasActivity;
import com.example.android.tenideas.model.Idea;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapterIdea extends RecyclerView.Adapter<RecyclerViewAdapterIdea.ViewHolder> {
    public TenIdeasActivity context;
    private List<Idea> ideaList;

    public RecyclerViewAdapterIdea(Context context, List<Idea> ideaList) {
        this.context = (TenIdeasActivity) context;
        this.ideaList = ideaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.idea_row, parent, false);
        return new ViewHolder(view, new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("ROM", "onBindViewHolder: " + position);
        Idea idea = ideaList.get(position);
        Boolean isFavourite = idea.getFavourite();
        holder.number.setText(MessageFormat.format("{0}.", position + 1));
        holder.setPosition(holder.getAdapterPosition());
        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.editText.setText(idea.getIdea());
        if (isFavourite)
            holder.favouriteButton.setImageResource(R.drawable.ic_star_favourite);
    }

    @Override
    public int getItemCount() {
        return ideaList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        boolean isPressed = false;
        int position;
        TextView number;
        EditText editText;
        MyCustomEditTextListener myCustomEditTextListener;
        ImageButton favouriteButton;

        ViewHolder(@NonNull View itemView, MyCustomEditTextListener listener) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            editText = itemView.findViewById(R.id.idea);
            editText.addTextChangedListener(listener);
            favouriteButton = itemView.findViewById(R.id.button_favourite);
            favouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchFavourite();
                    save();
                }
            });
            myCustomEditTextListener = listener;
        }
        void setPosition(int position){
            this.position = position;
        }
        private void save() {
            Idea idea = ideaList.get(position);
            idea.setFavourite(isPressed);
            ideaList.set(position, idea);
            context.saveIdea(ideaList.get(position));
        }

        private void switchFavourite() {
            if (isPressed){
                favouriteButton.setImageResource(R.drawable.ic_star_unfavourite);
            } else  {
                favouriteButton.setImageResource(R.drawable.ic_star_favourite);
            }
            isPressed = !isPressed;
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d("DING", "onTextChanged: " + position);
            Idea idea = ideaList.get(position);
            idea.setIdea(s.toString());
            ideaList.set(position, idea);
            context.saveIdea(ideaList.get(position));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
