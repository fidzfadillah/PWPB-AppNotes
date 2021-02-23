package com.fizha.appnotes;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> implements Filterable{

    private Context context;
    private ArrayList<NoteModel> listNote;
    private ArrayList<NoteModel> mArrayList;

    private DatabaseHelper mDataBase;

    public NoteAdapter(Context context, ArrayList<NoteModel> listNote) {
        this.context = context;
        this.listNote = listNote;
        this.mArrayList = listNote;
        mDataBase = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        final NoteModel model = listNote.get(position);

        holder.mTitle.setText(model.getTitle());
        holder.mDesc.setText(model.getDesc());
        holder.mDate.setText(model.getDate());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDialog(model);
            }
        });
    }

    private void editDialog(final NoteModel noteModel){
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.add_note_layout, null);

        final TextInputEditText txtTitle = (TextInputEditText)subView.findViewById(R.id.et_title);
        final TextInputEditText txtDesc = (TextInputEditText)subView.findViewById(R.id.et_desc);

        if (noteModel != null){
            txtTitle.setText(noteModel.getTitle());
            txtDesc.setText(noteModel.getDesc());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit note");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String eTitle = txtTitle.getText().toString();
                final String eDesc = txtDesc.getText().toString();
                final String tgl = getDateTime();

                if (TextUtils.isEmpty(eTitle)){
                    Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                } else {
                    mDataBase.update(new NoteModel(noteModel.getId(), eTitle, eDesc, tgl));
                    ((Activity)context).finish();
                    context.startActivity(((Activity)context).getIntent());
                }
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDataBase.delete(String.valueOf(noteModel.getId()));
                ((Activity)context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
        builder.show();
    }

    private String getDateTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "E, dd MMMM yyyy - HH:mm", Locale.getDefault()
        );
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return listNote.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listNote = mArrayList;
                } else {
                    ArrayList<NoteModel> filteredList = new ArrayList<>();
                    for (NoteModel model : mArrayList){
                        if (model.getTitle().toLowerCase().contains(charString)
                                || model.getDesc().toLowerCase().contains(charString)){
                            filteredList.add(model);
                        }
                    }
                    listNote = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listNote;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listNote = (ArrayList<NoteModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

