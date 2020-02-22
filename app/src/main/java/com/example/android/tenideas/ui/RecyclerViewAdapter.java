package com.example.android.tenideas.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.tenideas.MainActivity;
import com.example.android.tenideas.R;
import com.example.android.tenideas.TenIdeasActivity;
import com.example.android.tenideas.data.DatabaseHandler;
import com.example.android.tenideas.model.DateEntry;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<DateEntry> dateEntryList;
    DatabaseHandler db;

    public RecyclerViewAdapter(Context context, List<DateEntry> dateEntryList, DatabaseHandler db) {
        this.context = context;
        this.dateEntryList = dateEntryList;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateEntry dateEntry = dateEntryList.get(position);
        holder.ideaDate.setText(MessageFormat.format("{0}", dateEntry.getDateAdded()));
        holder.setGroupId(dateEntry.getId());
    }

    @Override
    public int getItemCount() {
        return dateEntryList.size();
    }

    //Facilitate data and interactions with a date entry
    class ViewHolder extends RecyclerView.ViewHolder {
        int groupId;
        LinearLayout layout;
        TextView ideaDate;

        ViewHolder(@NonNull View itemView, final Context c) {
            super(itemView);
            context = c;

            layout = itemView.findViewById(R.id.linear_layout);
            ideaDate = itemView.findViewById(R.id.card_date);

            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    dateEntryList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, dateEntryList.size());
                    db.deleteDateEntry(groupId);
                    return false;
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "onClick: " + "TEST");
                    Intent intent = new Intent(c, TenIdeasActivity.class);
                    intent.putExtra("id", groupId);
                    Log.d("RAJ", "onClick: " + groupId);
                    intent.putExtra("existing", true);
                    c.startActivity(intent);
                }
            });
        }

        void setGroupId(int id) {
            groupId = id;
        }
    }
}
