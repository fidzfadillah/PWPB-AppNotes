package com.fizha.appnotes;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    public TextView mTitle, mDesc, mDate;
    public CardView card;

    public NoteViewHolder(View itemView){
        super(itemView);
        mTitle = (TextView)itemView.findViewById(R.id.txt_title);
        mDesc = (TextView)itemView.findViewById(R.id.txt_desc);
        mDate = (TextView)itemView.findViewById(R.id.txt_date);
        card = (CardView)itemView.findViewById(R.id.card);
    }
}
