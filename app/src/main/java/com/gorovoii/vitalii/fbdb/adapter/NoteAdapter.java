package com.gorovoii.vitalii.fbdb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gorovoii.vitalii.fbdb.R;
import com.gorovoii.vitalii.fbdb.model.Note;

import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> dataList;
    private Context mContext;

    public NoteAdapter(Context context, List<Note> data) {
        this.dataList = data;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

class ViewHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView body;

    ViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.title);
        body = (TextView) itemView.findViewById(R.id.body);
    }

    void bind(final Note note) {
        title.setText(note.getTitle());
        body.setText(note.getBody());
    }
}

}

