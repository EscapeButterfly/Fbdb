package com.coursework.joulis1derful.notes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.coursework.joulis1derful.notes.R;
import com.coursework.joulis1derful.notes.model.Note;

import java.util.List;

import static com.coursework.joulis1derful.notes.activity.MainActivity.NOTES;


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
    private Button delButton;

    ViewHolder(View itemView) {
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.title);
        body = (TextView) itemView.findViewById(R.id.body);
        delButton = (Button) itemView.findViewById(R.id.deleteBtn);
    }

    void bind(final Note note) {
        title.setText(note.getTitle());
        body.setText(note.getBody());
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String keyToRemove = note.getFirebaseKey();
                FirebaseDatabase ref = FirebaseDatabase.getInstance();
                ref.getReference().child(NOTES).child(keyToRemove).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError != null) {
                            //notify user
                        } else {
                        }
                    }
                });
            }
        });
    }
}

}

